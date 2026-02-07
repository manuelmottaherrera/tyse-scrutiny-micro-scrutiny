package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Scrutiny Result entity - represents accumulated votes per candidate per day.
 */
@Table("scrutiny_result")
public class ScrutinyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("election_process_id")
    private Long electionProcessId;

    @NotNull
    @Column("scrutiny_day_id")
    private Long scrutinyDayId;

    @NotNull
    @Size(max = 30)
    @Column("divipol_key")
    private String divipolKey;

    @NotNull
    @Size(max = 10)
    @Column("party_number")
    private String partyNumber;

    @Size(max = 20)
    @Column("candidate_id")
    private String candidateId;

    @Column("votes")
    private Integer votes = 0;

    @Column("e14_form_id")
    private Long e14FormId;

    @Transient
    private ElectionProcess electionProcess;

    @Transient
    private ScrutinyDay scrutinyDay;

    @Transient
    private E14Form e14Form;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScrutinyResult id(Long id) {
        this.id = id;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public ScrutinyResult electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public Long getScrutinyDayId() {
        return scrutinyDayId;
    }

    public void setScrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
    }

    public ScrutinyResult scrutinyDayId(Long scrutinyDayId) {
        this.scrutinyDayId = scrutinyDayId;
        return this;
    }

    public String getDivipolKey() {
        return divipolKey;
    }

    public void setDivipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
    }

    public ScrutinyResult divipolKey(String divipolKey) {
        this.divipolKey = divipolKey;
        return this;
    }

    public String getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
    }

    public ScrutinyResult partyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
        return this;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public ScrutinyResult candidateId(String candidateId) {
        this.candidateId = candidateId;
        return this;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public ScrutinyResult votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public Long getE14FormId() {
        return e14FormId;
    }

    public void setE14FormId(Long e14FormId) {
        this.e14FormId = e14FormId;
    }

    public ScrutinyResult e14FormId(Long e14FormId) {
        this.e14FormId = e14FormId;
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

    public ScrutinyResult electionProcess(ElectionProcess electionProcess) {
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

    public ScrutinyResult scrutinyDay(ScrutinyDay scrutinyDay) {
        this.setScrutinyDay(scrutinyDay);
        return this;
    }

    public E14Form getE14Form() {
        return e14Form;
    }

    public void setE14Form(E14Form e14Form) {
        this.e14Form = e14Form;
        if (e14Form != null) {
            this.e14FormId = e14Form.getId();
        }
    }

    public ScrutinyResult e14Form(E14Form e14Form) {
        this.setE14Form(e14Form);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScrutinyResult)) return false;
        return id != null && id.equals(((ScrutinyResult) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ScrutinyResult{" +
            "id=" + id +
            ", divipolKey='" + divipolKey + "'" +
            ", partyNumber='" + partyNumber + "'" +
            ", candidateId='" + candidateId + "'" +
            ", votes=" + votes +
            "}";
    }
}
