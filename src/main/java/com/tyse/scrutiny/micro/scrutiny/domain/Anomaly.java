package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Anomaly entity - represents detected anomalies in electoral data.
 */
@Table("anomaly")
public class Anomaly implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("election_process_id")
    private Long electionProcessId;

    @NotNull
    @Size(max = 50)
    @Column("type")
    private String type;

    @NotNull
    @Size(max = 10)
    @Column("severity")
    private String severity;

    @NotNull
    @Size(max = 30)
    @Column("divipol_key")
    private String divipolKey;

    @Size(max = 5)
    @Column("dep_code")
    private String depCode;

    @Size(max = 5)
    @Column("mun_code")
    private String munCode;

    @Size(max = 5)
    @Column("voting_table")
    private String votingTable;

    @Size(max = 10)
    @Column("party_number")
    private String partyNumber;

    @Size(max = 20)
    @Column("candidate_id")
    private String candidateId;

    @Size(max = 100)
    @Column("candidate_first_name")
    private String candidateFirstName;

    @Size(max = 100)
    @Column("candidate_last_name")
    private String candidateLastName;

    @Column("precount_votes")
    private Integer precountVotes;

    @Column("scrutiny_votes")
    private Integer scrutinyVotes;

    @Column("difference")
    private Integer difference;

    @Column("scrutiny_day_id")
    private Long scrutinyDayId;

    @Column("description")
    private String description;

    @Size(max = 20)
    @Column("status")
    private String status = "NEW";

    @Size(max = 50)
    @Column("reviewed_by")
    private String reviewedBy;

    @Column("review_date")
    private Instant reviewDate;

    @Column("notes")
    private String notes;

    @Column("detected_at")
    private Instant detectedAt;

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

    public Anomaly id(Long id) {
        this.id = id;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public Anomaly electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Anomaly type(String type) {
        this.type = type;
        return this;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Anomaly severity(String severity) {
        this.severity = severity;
        return this;
    }

    public String getDivipolKey() {
        return divipolKey;
    }

    public void setDivipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
    }

    public Anomaly divipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
        return this;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public Anomaly depCode(String depCode) {
        this.depCode = depCode;
        return this;
    }

    public String getMunCode() {
        return munCode;
    }

    public void setMunCode(String munCode) {
        this.munCode = munCode;
    }

    public Anomaly munCode(String munCode) {
        this.munCode = munCode;
        return this;
    }

    public String getVotingTable() {
        return votingTable;
    }

    public void setVotingTable(String votingTable) {
        this.votingTable = votingTable;
    }

    public Anomaly votingTable(String votingTable) {
        this.votingTable = votingTable;
        return this;
    }

    public String getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
    }

    public Anomaly partyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
        return this;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Anomaly candidateId(String candidateId) {
        this.candidateId = candidateId;
        return this;
    }

    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public void setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;
    }

    public Anomaly candidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;
        return this;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public void setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;
    }

    public Anomaly candidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;
        return this;
    }

    public Integer getPrecountVotes() {
        return precountVotes;
    }

    public void setPrecountVotes(Integer precountVotes) {
        this.precountVotes = precountVotes;
    }

    public Anomaly precountVotes(Integer precountVotes) {
        this.precountVotes = precountVotes;
        return this;
    }

    public Integer getScrutinyVotes() {
        return scrutinyVotes;
    }

    public void setScrutinyVotes(Integer scrutinyVotes) {
        this.scrutinyVotes = scrutinyVotes;
    }

    public Anomaly scrutinyVotes(Integer scrutinyVotes) {
        this.scrutinyVotes = scrutinyVotes;
        return this;
    }

    public Integer getDifference() {
        return difference;
    }

    public void setDifference(Integer difference) {
        this.difference = difference;
    }

    public Anomaly difference(Integer difference) {
        this.difference = difference;
        return this;
    }

    public Long getScrutinyDayId() {
        return scrutinyDayId;
    }

    public void setScrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
    }

    public Anomaly scrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Anomaly description(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Anomaly status(String status) {
        this.status = status;
        return this;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Anomaly reviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
        return this;
    }

    public Instant getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Anomaly reviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Anomaly notes(String notes) {
        this.notes = notes;
        return this;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Anomaly detectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
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

    public Anomaly electionProcess(ElectionProcess electionProcess) {
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

    public Anomaly scrutinyDay(ScrutinyDay scrutinyDay) {
        this.setScrutinyDay(scrutinyDay);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Anomaly)) return false;
        return id != null && id.equals(((Anomaly) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Anomaly{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", severity='" + severity + "'" +
            ", divipolKey='" + divipolKey + "'" +
            ", difference=" + difference +
            ", status='" + status + "'" +
            "}";
    }
}
