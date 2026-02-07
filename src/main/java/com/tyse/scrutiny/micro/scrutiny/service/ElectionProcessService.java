package com.tyse.scrutiny.micro.scrutiny.service;

import com.tyse.scrutiny.micro.scrutiny.domain.ElectionProcess;
import com.tyse.scrutiny.micro.scrutiny.repository.ElectionProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service for managing election processes.
 */
@Service
@Transactional
public class ElectionProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(ElectionProcessService.class);

    private final ElectionProcessRepository electionProcessRepository;

    public ElectionProcessService(ElectionProcessRepository electionProcessRepository) {
        this.electionProcessRepository = electionProcessRepository;
    }

    /**
     * Save an election process.
     */
    public Mono<ElectionProcess> save(ElectionProcess electionProcess) {
        LOG.debug("Request to save ElectionProcess : {}", electionProcess);
        if (electionProcess.getCreatedDate() == null) {
            electionProcess.setCreatedDate(Instant.now());
        }
        return electionProcessRepository.save(electionProcess);
    }

    /**
     * Update an election process.
     */
    public Mono<ElectionProcess> update(ElectionProcess electionProcess) {
        LOG.debug("Request to update ElectionProcess : {}", electionProcess);
        return electionProcessRepository.save(electionProcess);
    }

    /**
     * Get all election processes.
     */
    @Transactional(readOnly = true)
    public Flux<ElectionProcess> findAll(Pageable pageable) {
        LOG.debug("Request to get all ElectionProcesses");
        return electionProcessRepository.findAllBy(pageable);
    }

    /**
     * Get all active election processes.
     */
    @Transactional(readOnly = true)
    public Flux<ElectionProcess> findAllActive() {
        LOG.debug("Request to get all active ElectionProcesses");
        return electionProcessRepository.findByActiveTrue();
    }

    /**
     * Get the current active election process.
     */
    @Transactional(readOnly = true)
    public Mono<ElectionProcess> findCurrentActive() {
        LOG.debug("Request to get current active ElectionProcess");
        return electionProcessRepository.findFirstByActiveTrueOrderByElectionDateDesc();
    }

    /**
     * Count all election processes.
     */
    @Transactional(readOnly = true)
    public Mono<Long> count() {
        LOG.debug("Request to count ElectionProcesses");
        return electionProcessRepository.count();
    }

    /**
     * Get one election process by id.
     */
    @Transactional(readOnly = true)
    public Mono<ElectionProcess> findOne(Long id) {
        LOG.debug("Request to get ElectionProcess : {}", id);
        return electionProcessRepository.findById(id);
    }

    /**
     * Delete the election process by id.
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete ElectionProcess : {}", id);
        return electionProcessRepository.deleteById(id);
    }

    /**
     * Deactivate an election process.
     */
    public Mono<ElectionProcess> deactivate(Long id) {
        LOG.debug("Request to deactivate ElectionProcess : {}", id);
        return electionProcessRepository.findById(id)
            .flatMap(process -> {
                process.setActive(false);
                return electionProcessRepository.save(process);
            });
    }
}
