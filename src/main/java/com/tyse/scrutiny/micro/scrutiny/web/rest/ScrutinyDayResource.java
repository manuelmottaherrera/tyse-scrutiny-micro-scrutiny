package com.tyse.scrutiny.micro.scrutiny.web.rest;

import com.tyse.scrutiny.micro.scrutiny.domain.ScrutinyDay;
import com.tyse.scrutiny.micro.scrutiny.service.ScrutinyDayService;
import com.tyse.scrutiny.micro.scrutiny.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing scrutiny days.
 */
@RestController
@RequestMapping("/api/scrutiny-days")
public class ScrutinyDayResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScrutinyDayResource.class);
    private static final String ENTITY_NAME = "scrutinyDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScrutinyDayService scrutinyDayService;

    public ScrutinyDayResource(ScrutinyDayService scrutinyDayService) {
        this.scrutinyDayService = scrutinyDayService;
    }

    /**
     * POST /api/scrutiny-days : Create a new scrutiny day.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ScrutinyDay>> createScrutinyDay(
        @Valid @RequestBody ScrutinyDay scrutinyDay
    ) throws URISyntaxException {
        LOG.debug("REST request to save ScrutinyDay : {}", scrutinyDay);
        if (scrutinyDay.getId() != null) {
            throw new BadRequestAlertException(
                "A new scrutinyDay cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        return scrutinyDayService
            .save(scrutinyDay)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/scrutiny-days/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(
                            applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * GET /api/scrutiny-days : Get all scrutiny days for an election process.
     */
    @GetMapping("")
    public Flux<ScrutinyDay> getAllScrutinyDays(
        @RequestParam("electionProcessId") Long electionProcessId
    ) {
        LOG.debug("REST request to get ScrutinyDays for ElectionProcess : {}", electionProcessId);
        return scrutinyDayService.findByElectionProcess(electionProcessId);
    }

    /**
     * GET /api/scrutiny-days/current : Get or create the current scrutiny day.
     */
    @GetMapping("/current")
    public Mono<ResponseEntity<ScrutinyDay>> getCurrentScrutinyDay(
        @RequestParam("electionProcessId") Long electionProcessId
    ) {
        LOG.debug("REST request to get current ScrutinyDay for ElectionProcess : {}", electionProcessId);
        return scrutinyDayService
            .getOrCreateForToday(electionProcessId)
            .map(ResponseEntity::ok);
    }

    /**
     * GET /api/scrutiny-days/{id} : Get one scrutiny day by id.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ScrutinyDay>> getScrutinyDay(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScrutinyDay : {}", id);
        return ResponseUtil.wrapOrNotFound(scrutinyDayService.findOne(id));
    }

    /**
     * DELETE /api/scrutiny-days/{id} : Delete a scrutiny day.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteScrutinyDay(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScrutinyDay : {}", id);
        return scrutinyDayService
            .delete(id)
            .then(Mono.just(ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(
                    applicationName, true, ENTITY_NAME, id.toString()))
                .build()));
    }
}
