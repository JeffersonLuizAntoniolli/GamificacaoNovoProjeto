package com.gamificacao.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;	// Codigo de identificação no Banco da Atividade
    @NotEmpty(message = "{Precisa ser cadastrado um nome para a Atividade}")
    private String name;	// Nome ou Titulo da Atividade
    @NotEmpty(message = "{Precisa ser cadastrado uma descrição sobre oque vai ser a Atvididade}")
    @Column(length = 1200)	
    @Size(max = 1200, message = "{Limite de caracteres é de 1200}")
    private String description; 	// Descrição da Atividade
    @NotNull(message = "{Precisa ser preenchido uma data de prazo final para Atividade}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date; 		// Data de prazo final da Atividade
    private boolean isCompleted; // Atividade Foi concluida ou não
    private String creatorName;  // Usuário Criador da Atividade
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner; // Usuário Responsavel pela Atividade
    //@NotNull(message = "{task.project.not.null}")
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project; 	// Projeto a qual atividade vai estar vinculada
    public long daysLeftUntilDeadline(LocalDate date) {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
    @ManyToOne
    @JoinColumn(name = "AFFINITY_ID")
    private Affinity affinity; // Afinidade a qual atividade vai estar vinculada
    
    public Task() {
    }

    public Task(@NotEmpty String name,
                @NotEmpty @Size(max = 1200) String description,
                @NotNull LocalDate date,
                boolean isCompleted,
                String creatorName,
                Project project,
                Affinity affinity) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.creatorName = creatorName;
        this.project = project;
        this.affinity = affinity;
    }

    public Task(@NotEmpty String name,
                @NotEmpty @Size(max = 1200) String description,
                @NotNull LocalDate date,
                boolean isCompleted,
                String creatorName,
                User owner,
                Project project,
                Affinity affinity) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.creatorName = creatorName;
        this.owner = owner;
        this.project = project;
        this.affinity = affinity;
    }

    public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isCompleted == task.isCompleted &&
                Objects.equals(id, task.id) &&
                name.equals(task.name) &&
                description.equals(task.description) &&
                date.equals(task.date) &&
                Objects.equals(creatorName, task.creatorName) &&
                Objects.equals(owner, task.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, date, isCompleted, creatorName, owner);
    }

	public Affinity getAffinity() {
		return affinity;
	}

	public void setAffinity(Affinity affinity) {
		this.affinity = affinity;
	}
}
