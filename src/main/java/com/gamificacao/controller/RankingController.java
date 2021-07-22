package com.gamificacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.AffinityOrder;
import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;
import com.gamificacao.repository.UserAffinityRepository;
import com.gamificacao.repository.UserRepository;
import com.gamificacao.service.AffinityService;

@Controller
public class RankingController {
	
	@Autowired
	private AffinityService affinityService;
	@Autowired
	UserRepository userRespository;
	@Autowired
	UserAffinityRepository userAffinityRepository;
	
    @GetMapping("/ranking")
    String showIndex(AffinityOrder affinityOrder, Model model) {
    	List<User> usuariosPorExperiencia = userRespository.findByOrderByExperienceDesc();
    	model.addAttribute("usuariosExp", usuariosPorExperiencia);
    	List<User> usuariosPorPontos = userRespository.findByOrderByPointsDesc();
    	model.addAttribute("usuariosPontos", usuariosPorPontos);
    	

    	List<Affinity> affinities = affinityService.findAll();
    	model.addAttribute("affinitys", affinities);
    	List<UserAffinity> usuariosPorAfinidade;
    	if(affinityOrder !=null && affinityOrder.getAffinity()!=null) {
    		usuariosPorAfinidade = userAffinityRepository.FindByOrderByExperienceDesc(affinityOrder.getAffinity().getId());
    	}else {
    		usuariosPorAfinidade = userAffinityRepository.FindByOrderByExperienceDesc(affinities.get(0).getId());
    	}
    	
    	model.addAttribute("usuariosAfinidade",usuariosPorAfinidade);
    	model.addAttribute("affinityOrder",affinityOrder!=null?affinityOrder:new AffinityOrder(affinities.get(0)));
    	
        return "views/ranking"; // retorna a pagina inicial
    }
    
    
}
