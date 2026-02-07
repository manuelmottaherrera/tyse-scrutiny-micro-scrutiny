package com.tyse.scrutiny.micro.scrutiny.service;

import com.tyse.scrutiny.micro.scrutiny.domain.ScrutinyDay;
import com.tyse.scrutiny.micro.scrutiny.repository.ScrutinyDayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service for managing scrutiny days.
 */
@Service
@Transactional
public class ScrutinyDayService {

    private static final Logger LOG = LoggerFactory.getLogger(ScrutinyDayService.class);
    private static final DateTimeFormatter LABEL_FORMATTER = DateTimeFormatter.ofPattern("'ESC'ddMMMyyyy");

    private final ScrutinyDayRepository scrutinyDayRepository;

    public ScrutinyDayService(ScrutinyDayRepository scrutinyDayRepository) {
        this.scrutinyDayRepository = scrutinyDayRepository;
    }

    /**
     * Save a scrutiny day.
     */
    public Mono<ScrutinyDay> save(ScrutinyDay scrutinyDay) {
        LOG.debug("Request to save ScrutinyDay : {}", scrutinyDay);
        if (scrutinyDay.getLabel() == null && scrutinyDay.getDate() != null) {
            scrutinyDay.setLabel(scrutinyDay.getDate().format(LABEL_FORMATTER).toUpperCase());
        }
        return scrutinyDayRepository.save(scrutinyDay);
    }

    /**
     * Get all scrutiny days for an election process.
     */
    @Transactional(readOnly = true)
    public Flux<ScrutinyDay> findByElectionProcess(Long electionProcessId) {
        LOG.debug("Request to get ScrutinyDays for ElectionProcess : {}", electionProcessId);
        return scrutinyDayRepository.findByElectionProcessIdOrderByDayNumberAsc(electionProcessId);
    }

    /**
     * Get or create a scrutiny day for today.
     */
    public Mono<ScrutinyDay> getOrCreateForToday(Long electionProcessId) {
        LocalDate today = LocalDate.now();
        return scrutinyDayRepository.findByElectionProcessIdAndDate(electionProcessId, today)
            .switchIfEmpty(
                scrutinyDayRepository.countByElectionProcessId(electionProcessId)
                    .flatMap(count -> {
                        ScrutinyDay newDay = new ScrutinyDay()
                            .electionProcessId(electionProcessId)
                            .date(today)
                            .dayNumber(count.intValue() + 1)
                            .label(today.format(LABEL_FORMATTER).toUpperCase());
                        return scrutinyDayRepository.save(newDay);
                    })
            );
    }

    /**
     * Get the current (latest) scrutiny day.
     */
    @Transactional(readOnly = true)
    public Mono<ScrutinyDay> findCurrent(Long electionProcessId) {
        LOG.debug("Request to get current ScrutinyDay for ElectionProcess : {}", electionProcessId);
        return scrutinyDayRepository.findFirstByElectionProcessIdOrderByDayNumberDesc(electionProcessId);
    }

    /**
     * Get one scrutiny day by id.
     */
    @Transactional(readOnly = true)
    public Mono<ScrutinyDay> findOne(Long id) {
        LOG.debug("Request to get ScrutinyDay : {}", id);
        return scrutinyDayRepository.findById(id);
    }

    /**
     * Delete the scrutiny day by id.
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete ScrutinyDay : {}", id);
        return scrutinyDayRepository.deleteById(id);
    }
}
