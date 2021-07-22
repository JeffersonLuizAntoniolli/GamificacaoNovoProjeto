package com.gamificacao.model;

public class AffinityOrder {

    private Affinity affinity; // Afinidade a qual atividade vai estar vinculada
    
	public AffinityOrder(Affinity affinity) {
		super();
		this.affinity = affinity;
	}
	public AffinityOrder() {
		super();
	}

	public Affinity getAffinity() {
		return affinity;
	}

	public void setAffinity(Affinity affinity) {
		this.affinity = affinity;
	}
    
    
}
