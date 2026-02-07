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
 * Precount entity - represents imported precount data from CSV.
 */
@Table("precount")
public class Precount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("election_process_id")
    private Long electionProcessId;

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

    @Column("imported_at")
    private Instant importedAt;

    @Transient
    private ElectionProcess electionProcess;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Precount id(Long id) {
        this.id = id;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public Precount electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public Precount depCode(String depCode) {
        this.depCode = depCode;
        return this;
    }

    public String getMunCode() {
        return munCode;
    }

    public void setMunCode(String munCode) {
        this.munCode = munCode;
    }

    public Precount munCode(String munCode) {
        this.munCode = munCode;
        return this;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Precount zone(String zone) {
        this.zone = zone;
        return this;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Precount station(String station) {
        this.station = station;
        return this;
    }

    public String getVotingTable() {
        return votingTable;
    }

    public void setVotingTable(String votingTable) {
        this.votingTable = votingTable;
    }

    public Precount votingTable(String votingTable) {
        this.votingTable = votingTable;
        return this;
    }

    public String getDivipolKey() {
        return divipolKey;
    }

    public void setDivipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
    }

    public Precount divipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
        return this;
    }

    public Integer getTotalVoters() {
        return totalVoters;
    }

    public void setTotalVoters(Integer totalVoters) {
        this.totalVoters = totalVoters;
    }

    public Precount totalVoters(Integer totalVoters) {
        this.totalVoters = totalVoters;
        return this;
    }

    public Integer getTotalBallotBoxVotes() {
        return totalBallotBoxVotes;
    }

    public void setTotalBallotBoxVotes(Integer totalBallotBoxVotes) {
        this.totalBallotBoxVotes = totalBallotBoxVotes;
    }

    public Precount totalBallotBoxVotes(Integer totalBallotBoxVotes) {
        this.totalBallotBoxVotes = totalBallotBoxVotes;
        return this;
    }

    public Instant getImportedAt() {
        return importedAt;
    }

    public void setImportedAt(Instant importedAt) {
        this.importedAt = importedAt;
    }

    public Precount importedAt(Instant importedAt) {
        this.importedAt = importedAt;
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

    public Precount electionProcess(ElectionProcess electionProcess) {
        this.setElectionProcess(electionProcess);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Precount)) return false;
        return id != null && id.equals(((Precount) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Precount{" +
            "id=" + id +
            ", divipolKey='" + divipolKey + "'" +
            ", votingTable='" + votingTable + "'" +
            ", totalVoters=" + totalVoters +
            "}";
    }
}
