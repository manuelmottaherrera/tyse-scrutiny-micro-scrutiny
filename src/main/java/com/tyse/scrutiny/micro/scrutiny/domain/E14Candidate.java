package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * E14 Candidate entity - represents a candidate's votes in an E14 party.
 */
@Table("e14_candidate")
public class E14Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("e14_party_id")
    private Long e14PartyId;

    @NotNull
    @Size(max = 20)
    @Column("candidate_id")
    private String candidateId;

    @Column("votes")
    private Integer votes = 0;

    @Column("needs_audit")
    private Boolean needsAudit = false;

    @Transient
    private E14Party e14Party;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public E14Candidate id(Long id) {
        this.id = id;
        return this;
    }

    public Long getE14PartyId() {
        return e14PartyId;
    }

    public void setE14PartyId(Long e14PartyId) {
        this.e14PartyId = e14PartyId;
    }

    public E14Candidate e14PartyId(Long e14PartyId) {
        this.e14PartyId = e14PartyId;
        return this;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public E14Candidate candidateId(String candidateId) {
        this.candidateId = candidateId;
        return this;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public E14Candidate votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public Boolean getNeedsAudit() {
        return needsAudit;
    }

    public void setNeedsAudit(Boolean needsAudit) {
        this.needsAudit = needsAudit;
    }

    public E14Candidate needsAudit(Boolean needsAudit) {
        this.needsAudit = needsAudit;
        return this;
    }

    public E14Party getE14Party() {
        return e14Party;
    }

    public void setE14Party(E14Party e14Party) {
        this.e14Party = e14Party;
        if (e14Party != null) {
            this.e14PartyId = e14Party.getId();
        }
    }

    public E14Candidate e14Party(E14Party e14Party) {
        this.setE14Party(e14Party);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof E14Candidate)) return false;
        return id != null && id.equals(((E14Candidate) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "E14Candidate{" +
            "id=" + id +
            ", e14PartyId=" + e14PartyId +
            ", candidateId='" + candidateId + "'" +
            ", votes=" + votes +
            "}";
    }
}
