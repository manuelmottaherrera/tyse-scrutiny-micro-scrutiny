package com.tyse.scrutiny.micro.scrutiny.web.rest;

import com.tyse.scrutiny.micro.scrutiny.domain.ElectionProcess;
import com.tyse.scrutiny.micro.scrutiny.repository.ElectionProcessRepository;
import com.tyse.scrutiny.micro.scrutiny.service.ElectionProcessService;
import com.tyse.scrutiny.micro.scrutiny.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * REST controller for managing election processes.
 */
@RestController
@RequestMapping("/api/election-processes")
public class ElectionProcessResource {

    private static final Logger LOG = LoggerFactory.getLogger(ElectionProcessResource.class);
    private static final String ENTITY_NAME = "electionProcess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElectionProcessService electionProcessService;
    private final ElectionProcessRepository electionProcessRepository;

    public ElectionProcessResource(
        ElectionProcessService electionProcessService,
        ElectionProcessRepository electionProcessRepository
    ) {
        this.electionProcessService = electionProcessService;
        this.electionProcessRepository = electionProcessRepository;
    }

    /**
     * POST /api/election-processes : Create a new election process.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ElectionProcess>> createElectionProcess(
        @Valid @RequestBody ElectionProcess electionProcess
    ) throws URISyntaxException {
        LOG.debug("REST request to save ElectionProcess : {}", electionProcess);
        if (electionProcess.getId() != null) {
            throw new BadRequestAlertException(
                "A new electionProcess cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        return electionProcessService
            .save(electionProcess)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/election-processes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(
                            applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * PUT /api/election-processes/{id} : Updates an existing election process.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ElectionProcess>> updateElectionProcess(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ElectionProcess electionProcess
    ) {
        LOG.debug("REST request to update ElectionProcess : {}, {}", id, electionProcess);
        if (electionProcess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, electionProcess.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return electionProcessRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException(
                        "Entity not found", ENTITY_NAME, "idnotfound"));
                }
                return electionProcessService
                    .update(electionProcess)
                    .map(result -> ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(
                            applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
            });
    }

    /**
     * GET /api/election-processes : Get all election processes.
     */
    @GetMapping("")
    public Mono<ResponseEntity<Flux<ElectionProcess>>> getAllElectionProcesses(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        LOG.debug("REST request to get all ElectionProcesses");
        return electionProcessService.count()
            .map(total -> ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .body(electionProcessService.findAll(PageRequest.of(page, size))));
    }

    /**
     * GET /api/election-processes/active : Get all active election processes.
     */
    @GetMapping("/active")
    public Flux<ElectionProcess> getActiveElectionProcesses() {
        LOG.debug("REST request to get active ElectionProcesses");
        return electionProcessService.findAllActive();
    }

    /**
     * GET /api/election-processes/current : Get the current active election process.
     */
    @GetMapping("/current")
    public Mono<ResponseEntity<ElectionProcess>> getCurrentElectionProcess() {
        LOG.debug("REST request to get current ElectionProcess");
        return ResponseUtil.wrapOrNotFound(electionProcessService.findCurrentActive());
    }

    /**
     * GET /api/election-processes/{id} : Get one election process by id.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ElectionProcess>> getElectionProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ElectionProcess : {}", id);
        return ResponseUtil.wrapOrNotFound(electionProcessService.findOne(id));
    }

    /**
     * DELETE /api/election-processes/{id} : Delete an election process.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteElectionProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ElectionProcess : {}", id);
        return electionProcessService
            .delete(id)
            .then(Mono.just(ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(
                    applicationName, true, ENTITY_NAME, id.toString()))
                .build()));
    }

    /**
     * PATCH /api/election-processes/{id}/deactivate : Deactivate an election process.
     */
    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseEntity<ElectionProcess>> deactivateElectionProcess(@PathVariable("id") Long id) {
        LOG.debug("REST request to deactivate ElectionProcess : {}", id);
        return electionProcessService
            .deactivate(id)
            .map(result -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
