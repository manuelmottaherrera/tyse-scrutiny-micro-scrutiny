package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Processed Document entity - tracks the processing status of PDF documents.
 */
@Table("processed_document")
public class ProcessedDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("pdf_id")
    private UUID pdfId;

    @Size(max = 500)
    @Column("file_name")
    private String fileName;

    @Column("election_process_id")
    private Long electionProcessId;

    @NotNull
    @Size(max = 20)
    @Column("stage")
    private String stage;

    @NotNull
    @Size(max = 20)
    @Column("status")
    private String status;

    @Column("error_message")
    private String errorMessage;

    @Column("uploaded_at")
    private Instant uploadedAt;

    @Column("ocr_completed_at")
    private Instant ocrCompletedAt;

    @Column("cleaning_completed_at")
    private Instant cleaningCompletedAt;

    @Column("persisted_at")
    private Instant persistedAt;

    @Column("analyzed_at")
    private Instant analyzedAt;

    @Column("created_date")
    private Instant createdDate;

    @Column("last_modified")
    private Instant lastModified;

    @Transient
    private ElectionProcess electionProcess;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessedDocument id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getPdfId() {
        return pdfId;
    }

    public void setPdfId(UUID pdfId) {
        this.pdfId = pdfId;
    }

    public ProcessedDocument pdfId(UUID pdfId) {
        this.pdfId = pdfId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProcessedDocument fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public ProcessedDocument electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ProcessedDocument stage(String stage) {
        this.stage = stage;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProcessedDocument status(String status) {
        this.status = status;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ProcessedDocument errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public ProcessedDocument uploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
        return this;
    }

    public Instant getOcrCompletedAt() {
        return ocrCompletedAt;
    }

    public void setOcrCompletedAt(Instant ocrCompletedAt) {
        this.ocrCompletedAt = ocrCompletedAt;
    }

    public ProcessedDocument ocrCompletedAt(Instant ocrCompletedAt) {
        this.ocrCompletedAt = ocrCompletedAt;
        return this;
    }

    public Instant getCleaningCompletedAt() {
        return cleaningCompletedAt;
    }

    public void setCleaningCompletedAt(Instant cleaningCompletedAt) {
        this.cleaningCompletedAt = cleaningCompletedAt;
    }

    public ProcessedDocument cleaningCompletedAt(Instant cleaningCompletedAt) {
        this.cleaningCompletedAt = cleaningCompletedAt;
        return this;
    }

    public Instant getPersistedAt() {
        return persistedAt;
    }

    public void setPersistedAt(Instant persistedAt) {
        this.persistedAt = persistedAt;
    }

    public ProcessedDocument persistedAt(Instant persistedAt) {
        this.persistedAt = persistedAt;
        return this;
    }

    public Instant getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(Instant analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public ProcessedDocument analyzedAt(Instant analyzedAt) {
        this.analyzedAt = analyzedAt;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public ProcessedDocument createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public ProcessedDocument lastModified(Instant lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public ElectionProcess getElectionProcess() {
        return electionProcess;
    }

    public void setElectionProcess(ElectionProcess electionProcess) {
        this.electionProcess = electionProcess;
        if (electionProcess != null) {
            this.electionProcessId = electionProcess.getId();
        }
    }

    public ProcessedDocument electionProcess(ElectionProcess electionProcess) {
        this.setElectionProcess(electionProcess);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessedDocument)) return false;
        return id != null && id.equals(((ProcessedDocument) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProcessedDocument{" +
            "id=" + id +
            ", pdfId=" + pdfId +
            ", fileName='" + fileName + "'" +
            ", stage='" + stage + "'" +
            ", status='" + status + "'" +
            "}";
    }
}
