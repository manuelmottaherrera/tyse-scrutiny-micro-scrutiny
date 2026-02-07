package com.tyse.scrutiny.micro.scrutiny.domain;

import com.tyse.scrutiny.micro.scrutiny.domain.enumeration.ElectionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Election process entity - represents an electoral event.
 */
@Table("election_process")
public class ElectionProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column("name")
    private String name;

    @NotNull
    @Size(max = 50)
    @Column("type")
    private String type;

    @NotNull
    @Column("election_date")
    private LocalDate electionDate;

    @Column("scrutiny_start_date")
    private LocalDate scrutinyStartDate;

    @Column("scrutiny_end_date")
    private LocalDate scrutinyEndDate;

    @Column("active")
    private Boolean active = true;

    @Column("created_date")
    private Instant createdDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ElectionProcess id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectionProcess name(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ElectionProcess type(String type) {
        this.type = type;
        return this;
    }

    public LocalDate getElectionDate() {
        return electionDate;
    }

    public void setElectionDate(LocalDate electionDate) {
        this.electionDate = electionDate;
    }

    public ElectionProcess electionDate(LocalDate electionDate) {
        this.electionDate = electionDate;
        return this;
    }

    public LocalDate getScrutinyStartDate() {
        return scrutinyStartDate;
    }

    public void setScrutinyStartDate(LocalDate scrutinyStartDate) {
        this.scrutinyStartDate = scrutinyStartDate;
    }

    public ElectionProcess scrutinyStartDate(LocalDate scrutinyStartDate) {
        this.scrutinyStartDate = scrutinyStartDate;
        return this;
    }

    public LocalDate getScrutinyEndDate() {
        return scrutinyEndDate;
    }

    public void setScrutinyEndDate(LocalDate scrutinyEndDate) {
        this.scrutinyEndDate = scrutinyEndDate;
    }

    public ElectionProcess scrutinyEndDate(LocalDate scrutinyEndDate) {
        this.scrutinyEndDate = scrutinyEndDate;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ElectionProcess active(Boolean active) {
        this.active = active;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public ElectionProcess createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectionProcess)) return false;
        return id != null && id.equals(((ElectionProcess) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ElectionProcess{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", electionDate=" + electionDate +
            ", active=" + active +
            "}";
    }
}
