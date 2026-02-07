package com.tyse.scrutiny.micro.scrutiny.web.rest;

import com.tyse.scrutiny.micro.scrutiny.domain.Anomaly;
import com.tyse.scrutiny.micro.scrutiny.service.AnomalyService;
import com.tyse.scrutiny.micro.scrutiny.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing anomalies.
 */
@RestController
@RequestMapping("/api/anomalies")
public class AnomalyResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnomalyResource.class);
    private static final String ENTITY_NAME = "anomaly";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnomalyService anomalyService;

    public AnomalyResource(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    /**
     * GET /api/anomalies : Get all anomalies for an election process.
     */
    @GetMapping("")
    public Mono<ResponseEntity<Flux<Anomaly>>> getAllAnomalies(
        @RequestParam("electionProcessId") Long electionProcessId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "severity", required = false) String severity
    ) {
        LOG.debug("REST request to get Anomalies for ElectionProcess : {}", electionProcessId);

        Flux<Anomaly> anomalies;
        if (status != null) {
            anomalies = anomalyService.findByStatus(electionProcessId, status, PageRequest.of(page, size));
        } else if (type != null) {
            anomalies = anomalyService.findByType(electionProcessId, type, PageRequest.of(page, size));
        } else if (severity != null) {
            anomalies = anomalyService.findBySeverity(electionProcessId, severity, PageRequest.of(page, size));
        } else {
            anomalies = anomalyService.findByElectionProcess(electionProcessId, PageRequest.of(page, size));
        }

        return anomalyService.count(electionProcessId)
            .map(total -> ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .body(anomalies));
    }

    /**
     * GET /api/anomalies/new : Get new (unreviewed) anomalies.
     */
    @GetMapping("/new")
    public Flux<Anomaly> getNewAnomalies(
        @RequestParam("electionProcessId") Long electionProcessId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        LOG.debug("REST request to get new Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyService.findNewAnomalies(electionProcessId, PageRequest.of(page, size));
    }

    /**
     * GET /api/anomalies/high-severity : Get high severity anomalies.
     */
    @GetMapping("/high-severity")
    public Flux<Anomaly> getHighSeverityAnomalies(
        @RequestParam("electionProcessId") Long electionProcessId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        LOG.debug("REST request to get high severity Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyService.findHighSeverity(electionProcessId, PageRequest.of(page, size));
    }

    /**
     * GET /api/anomalies/{id} : Get one anomaly by id.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Anomaly>> getAnomaly(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Anomaly : {}", id);
        return ResponseUtil.wrapOrNotFound(anomalyService.findOne(id));
    }

    /**
     * PATCH /api/anomalies/{id}/status : Update anomaly status.
     */
    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Anomaly>> updateAnomalyStatus(
        @PathVariable("id") Long id,
        @RequestBody AnomalyStatusUpdate statusUpdate
    ) {
        LOG.debug("REST request to update Anomaly status : {} to {}", id, statusUpdate.status());
        return anomalyService
            .updateStatus(id, statusUpdate.status(), statusUpdate.reviewedBy(), statusUpdate.notes())
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * DELETE /api/anomalies/{id} : Delete an anomaly.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAnomaly(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Anomaly : {}", id);
        return anomalyService
            .delete(id)
            .then(Mono.just(ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(
                    applicationName, true, ENTITY_NAME, id.toString()))
                .build()));
    }

    /**
     * DTO for anomaly status update.
     */
    public record AnomalyStatusUpdate(String status, String reviewedBy, String notes) {}
}
