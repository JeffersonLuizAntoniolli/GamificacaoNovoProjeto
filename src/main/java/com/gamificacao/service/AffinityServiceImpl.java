package com.gamificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.repository.AffinityRepository;
import com.gamificacao.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AffinityServiceImpl implements AffinityService {
    private AffinityRepository affinityRepository;

    @Autowired
    public AffinityServiceImpl(AffinityRepository affinityRepository) {
        this.affinityRepository = affinityRepository;
    }
    
	@Override
	public Affinity createAffinity(Affinity affinity) {
		return affinityRepository.save(affinity);
	}

	@Override
	public void updateAffinity(Long id, Affinity updatedAffinity) {
		Affinity affinity = affinityRepository.getOne(id);
		affinity.setName(updatedAffinity.getName());
		affinity.setDescription(updatedAffinity.getDescription());
        affinityRepository.save(affinity);
	}

	@Override
	public void deleteAffinity(Long id) {
		affinityRepository.deleteById(id);
	}

	@Override
	public List<Affinity> findAll() {
		return affinityRepository.findAll();
	}

	@Override
	public Affinity getAffinityById(Long AffinityId) {
		return affinityRepository.findById(AffinityId).orElse(null);
	}

    

}
