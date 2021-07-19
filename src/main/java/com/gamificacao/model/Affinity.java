package com.gamificacao.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Affinity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "affinity_id")
    private Long id;
	@NotEmpty(message = "{Precisa ser cadastrado um nome para o Afinidade}")
    private String name;	// Nome ou Titulo da Atividade
    @NotEmpty(message = "{Precisa ser cadastrado uma decrição sobre Afinidade}")
    @Column(length = 1200)	
    @Size(max = 1200, message = "{O limite de caracteres é 1200}")
    private String description;
	
    public Affinity() {
    }
    
    public Affinity(
    			@NotEmpty String name,
    			@NotEmpty @Size(max = 1200) String description
            ) {
    this.name = name;
    this.description = description;
    }
    
    public Long getId() {
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Affinity other = (Affinity) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
    
    
    
    
}
