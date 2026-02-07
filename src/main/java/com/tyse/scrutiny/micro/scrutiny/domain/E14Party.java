package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * E14 Party entity - represents a party's votes in an E14 form.
 */
@Table("e14_party")
public class E14Party implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("e14_form_id")
    private Long e14FormId;

    @NotNull
    @Size(max = 10)
    @Column("party_number")
    private String partyNumber;

    @Size(max = 200)
    @Column("party_name")
    private String partyName;

    @Size(max = 50)
    @Column("vote_type")
    private String voteType;

    @Column("party_only_votes")
    private Integer partyOnlyVotes = 0;

    @Column("total_party_candidate_votes")
    private Integer totalPartyCandidateVotes = 0;

    @Column("needs_audit")
    private Boolean needsAudit = false;

    @Transient
    private E14Form e14Form;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public E14Party id(Long id) {
        this.id = id;
        return this;
    }

    public Long getE14FormId() {
        return e14FormId;
    }

    public void setE14FormId(Long e14FormId) {
        this.e14FormId = e14FormId;
    }

    public E14Party e14FormId(Long e14FormId) {
        this.e14FormId = e14FormId;
        return this;
    }

    public String getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
    }

    public E14Party partyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
        return this;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public E14Party partyName(String partyName) {
        this.partyName = partyName;
        return this;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public E14Party voteType(String voteType) {
        this.voteType = voteType;
        return this;
    }

    public Integer getPartyOnlyVotes() {
        return partyOnlyVotes;
    }

    public void setPartyOnlyVotes(Integer partyOnlyVotes) {
        this.partyOnlyVotes = partyOnlyVotes;
    }

    public E14Party partyOnlyVotes(Integer partyOnlyVotes) {
        this.partyOnlyVotes = partyOnlyVotes;
        return this;
    }

    public Integer getTotalPartyCandidateVotes() {
        return totalPartyCandidateVotes;
    }

    public void setTotalPartyCandidateVotes(Integer totalPartyCandidateVotes) {
        this.totalPartyCandidateVotes = totalPartyCandidateVotes;
    }

    public E14Party totalPartyCandidateVotes(Integer totalPartyCandidateVotes) {
        this.totalPartyCandidateVotes = totalPartyCandidateVotes;
        return this;
    }

    public Boolean getNeedsAudit() {
        return needsAudit;
    }

    public void setNeedsAudit(Boolean needsAudit) {
        this.needsAudit = needsAudit;
    }

    public E14Party needsAudit(Boolean needsAudit) {
        this.needsAudit = needsAudit;
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

    public E14Party e14Form(E14Form e14Form) {
        this.setE14Form(e14Form);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof E14Party)) return false;
        return id != null && id.equals(((E14Party) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "E14Party{" +
            "id=" + id +
            ", e14FormId=" + e14FormId +
            ", partyNumber='" + partyNumber + "'" +
            ", partyName='" + partyName + "'" +
            ", partyOnlyVotes=" + partyOnlyVotes +
            ", totalPartyCandidateVotes=" + totalPartyCandidateVotes +
            "}";
    }
}
