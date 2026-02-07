package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.ProcessedDocument;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the ProcessedDocument entity.
 */
@Repository
public interface ProcessedDocumentRepository extends R2dbcRepository<ProcessedDocument, Long> {

    /**
     * Find document by PDF ID.
     */
    Mono<ProcessedDocument> findByPdfId(UUID pdfId);

    /**
     * Find documents by status with pagination.
     */
    Flux<ProcessedDocument> findByStatus(String status, Pageable pageable);

    /**
     * Find documents by stage with pagination.
     */
    Flux<ProcessedDocument> findByStage(String stage, Pageable pageable);

    /**
     * Find documents by election process with pagination.
     */
    Flux<ProcessedDocument> findByElectionProcessId(Long electionProcessId, Pageable pageable);

    /**
     * Find failed documents.
     */
    Flux<ProcessedDocument> findByStatusOrderByLastModifiedDesc(String status);

    /**
     * Find documents in progress.
     */
    Flux<ProcessedDocument> findByStatusAndStage(String status, String stage);

    /**
     * Count documents by status.
     */
    Mono<Long> countByStatus(String status);

    /**
     * Count documents by stage.
     */
    Mono<Long> countByStage(String stage);

    /**
     * Count documents by election process.
     */
    Mono<Long> countByElectionProcessId(Long electionProcessId);

    /**
     * Check if document exists by PDF ID.
     */
    Mono<Boolean> existsByPdfId(UUID pdfId);
}
