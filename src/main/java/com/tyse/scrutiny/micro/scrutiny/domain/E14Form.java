package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * E14 Form entity - represents a processed E14 electoral form.
 */
@Table("e14_form")
public class E14Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("election_process_id")
    private Long electionProcessId;

    @NotNull
    @Column("pdf_id")
    private UUID pdfId;

    @NotNull
    @Size(max = 5)
    @Column("dep_code")
    private String depCode;

    @NotNull
    @Size(max = 5)
    @Column("mun_code")
    private String munCode;

    @NotNull
    @Size(max = 5)
    @Column("zone")
    private String zone;

    @NotNull
    @Size(max = 5)
    @Column("station")
    private String station;

    @NotNull
    @Size(max = 5)
    @Column("voting_table")
    private String votingTable;

    @NotNull
    @Size(max = 30)
    @Column("divipol_key")
    private String divipolKey;

    @Column("total_voters")
    private Integer totalVoters;

    @Column("total_ballot_box_votes")
    private Integer totalBallotBoxVotes;

    @Size(max = 10)
    @Column("total_incinerated")
    private String totalIncinerated;

    @Column("pages_received")
    private Integer pagesReceived;

    @Column("total_pages")
    private Integer totalPages;

    @Column("is_complete")
    private Boolean isComplete = false;

    @Column("scrutiny_day_id")
    private Long scrutinyDayId;

    @Column("ocr_confidence")
    private BigDecimal ocrConfidence;

    @Column("processed_at")
    private Instant processedAt;

    @Column("created_date")
    private Instant createdDate;

    @Transient
    private ElectionProcess electionProcess;

    @Transient
    private ScrutinyDay scrutinyDay;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public E14Form id(Long id) {
        this.id = id;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public E14Form electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public UUID getPdfId() {
        return pdfId;
    }

    public void setPdfId(UUID pdfId) {
        this.pdfId = pdfId;
    }

    public E14Form pdfId(UUID pdfId) {
        this.pdfId = pdfId;
        return this;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public E14Form depCode(String depCode) {
        this.depCode = depCode;
        return this;
    }

    public String getMunCode() {
        return munCode;
    }

    public void setMunCode(String munCode) {
        this.munCode = munCode;
    }

    public E14Form munCode(String munCode) {
        this.munCode = munCode;
        return this;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public E14Form zone(String zone) {
        this.zone = zone;
        return this;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public E14Form station(String station) {
        this.station = station;
        return this;
    }

    public String getVotingTable() {
        return votingTable;
    }

    public void setVotingTable(String votingTable) {
        this.votingTable = votingTable;
    }

    public E14Form votingTable(String votingTable) {
        this.votingTable = votingTable;
        return this;
    }

    public String getDivipolKey() {
        return divipolKey;
    }

    public void setDivipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
    }

    public E14Form divipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
        return this;
    }

    public Integer getTotalVoters() {
        return totalVoters;
    }

    public void setTotalVoters(Integer totalVoters) {
        this.totalVoters = totalVoters;
    }

    public E14Form totalVoters(Integer totalVoters) {
        this.totalVoters = totalVoters;
        return this;
    }

    public Integer getTotalBallotBoxVotes() {
        return totalBallotBoxVotes;
    }

    public void setTotalBallotBoxVotes(Integer totalBallotBoxVotes) {
        this.totalBallotBoxVotes = totalBallotBoxVotes;
    }

    public E14Form totalBallotBoxVotes(Integer totalBallotBoxVotes) {
        this.totalBallotBoxVotes = totalBallotBoxVotes;
        return this;
    }

    public String getTotalIncinerated() {
        return totalIncinerated;
    }

    public void setTotalIncinerated(String totalIncinerated) {
        this.totalIncinerated = totalIncinerated;
    }

    public E14Form totalIncinerated(String totalIncinerated) {
        this.totalIncinerated = totalIncinerated;
        return this;
    }

    public Integer getPagesReceived() {
        return pagesReceived;
    }

    public void setPagesReceived(Integer pagesReceived) {
        this.pagesReceived = pagesReceived;
    }

    public E14Form pagesReceived(Integer pagesReceived) {
        this.pagesReceived = pagesReceived;
        return this;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public E14Form totalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public E14Form isComplete(Boolean isComplete) {
        this.isComplete = isComplete;
        return this;
    }

    public Long getScrutinyDayId() {
        return scrutinyDayId;
    }

    public void setScrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
    }

    public E14Form scrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
        return this;
    }

    public BigDecimal getOcrConfidence() {
        return ocrConfidence;
    }

    public void setOcrConfidence(BigDecimal ocrConfidence) {
        this.ocrConfidence = ocrConfidence;
    }

    public E14Form ocrConfidence(BigDecimal ocrConfidence) {
        this.ocrConfidence = ocrConfidence;
        return this;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public E14Form processedAt(Instant processedAt) {
        this.processedAt = processedAt;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public E14Form createdDate(Instant createdDate) {
        this.createdDate = createdDate;
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

    public E14Form electionProcess(ElectionProcess electionProcess) {
        this.setElectionProcess(electionProcess);
        return this;
    }

    public ScrutinyDay getScrutinyDay() {
        return scrutinyDay;
    }

    public void setScrutinyDay(ScrutinyDay scrutinyDay) {
        this.scrutinyDay = scrutinyDay;
        if (scrutinyDay != null) {
            this.scrutinyDayId = scrutinyDay.getId();
        }
    }

    public E14Form scrutinyDay(ScrutinyDay scrutinyDay) {
        this.setScrutinyDay(scrutinyDay);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof E14Form)) return false;
        return id != null && id.equals(((E14Form) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "E14Form{" +
            "id=" + id +
            ", pdfId=" + pdfId +
            ", divipolKey='" + divipolKey + "'" +
            ", votingTable='" + votingTable + "'" +
            ", totalVoters=" + totalVoters +
            ", isComplete=" + isComplete +
            "}";
    }
}
