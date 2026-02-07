package com.tyse.scrutiny.micro.scrutiny.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Scrutiny day entity - represents a single day of scrutiny.
 */
@Table("scrutiny_day")
public class ScrutinyDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("election_process_id")
    private Long electionProcessId;

    @NotNull
    @Column("date")
    private LocalDate date;

    @NotNull
    @Column("day_number")
    private Integer dayNumber;

    @Size(max = 100)
    @Column("label")
    private String label;

    @Transient
    private ElectionProcess electionProcess;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScrutinyDay id(Long id) {
        this.id = id;
        return this;
    }

    public Long getElectionProcessId() {
        return electionProcessId;
    }

    public void setElectionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
    }

    public ScrutinyDay electionProcessId(Long electionProcessId) {
        this.electionProcessId = electionProcessId;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ScrutinyDay date(LocalDate date) {
        this.date = date;
        return this;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public ScrutinyDay dayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ScrutinyDay label(String label) {
        this.label = label;
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

    public ScrutinyDay electionProcess(ElectionProcess electionProcess) {
        this.setElectionProcess(electionProcess);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScrutinyDay)) return false;
        return id != null && id.equals(((ScrutinyDay) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ScrutinyDay{" +
            "id=" + id +
            ", electionProcessId=" + electionProcessId +
            ", date=" + date +
            ", dayNumber=" + dayNumber +
            ", label='" + label + "'" +
            "}";
    }
}
