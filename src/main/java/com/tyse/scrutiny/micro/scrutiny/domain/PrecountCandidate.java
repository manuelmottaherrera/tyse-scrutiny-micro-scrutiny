package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Precount Candidate entity - represents a candidate's votes in precount data.
 */
@Table("precount_candidate")
public class PrecountCandidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("precount_id")
    private Long precountId;

    @NotNull
    @Size(max = 10)
    @Column("party_number")
    private String partyNumber;

    @Size(max = 200)
    @Column("party_name")
    private String partyName;

    @Size(max = 20)
    @Column("candidate_id")
    private String candidateId;

    @Column("candidate_code")
    private Integer candidateCode;

    @Column("ballot_position")
    private Integer ballotPosition;

    @Size(max = 100)
    @Column("first_name")
    private String firstName;

    @Size(max = 100)
    @Column("last_name")
    private String lastName;

    @Column("votes")
    private Integer votes = 0;

    @Transient
    private Precount precount;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrecountCandidate id(Long id) {
        this.id = id;
        return this;
    }

    public Long getPrecountId() {
        return precountId;
    }

    public void setPrecountId(Long precountId) {
        this.precountId = precountId;
    }

    public PrecountCandidate precountId(Long precountId) {
        this.precountId = precountId;
        return this;
    }

    public String getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
    }

    public PrecountCandidate partyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
        return this;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public PrecountCandidate partyName(String partyName) {
        this.partyName = partyName;
        return this;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public PrecountCandidate candidateId(String candidateId) {
        this.candidateId = candidateId;
        return this;
    }

    public Integer getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(Integer candidateCode) {
        this.candidateCode = candidateCode;
    }

    public PrecountCandidate candidateCode(Integer candidateCode) {
        this.candidateCode = candidateCode;
        return this;
    }

    public Integer getBallotPosition() {
        return ballotPosition;
    }

    public void setBallotPosition(Integer ballotPosition) {
        this.ballotPosition = ballotPosition;
    }

    public PrecountCandidate ballotPosition(Integer ballotPosition) {
        this.ballotPosition = ballotPosition;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public PrecountCandidate firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PrecountCandidate lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public PrecountCandidate votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public Precount getPrecount() {
        return precount;
    }

    public void setPrecount(Precount precount) {
        this.precount = precount;
        if (precount != null) {
            this.precountId = precount.getId();
        }
    }

    public PrecountCandidate precount(Precount precount) {
        this.setPrecount(precount);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrecountCandidate)) return false;
        return id != null && id.equals(((PrecountCandidate) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PrecountCandidate{" +
            "id=" + id +
            ", precountId=" + precountId +
            ", partyNumber='" + partyNumber + "'" +
            ", candidateId='" + candidateId + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", votes=" + votes +
            "}";
    }
}
