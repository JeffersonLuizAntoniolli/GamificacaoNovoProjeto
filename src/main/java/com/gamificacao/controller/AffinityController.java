package com.gamificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.service.AffinityService;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AffinityController {

    private AffinityService affinityService;

    @Autowired
    public AffinityController(AffinityService affinityService) {
        this.affinityService = affinityService;
    }

    @GetMapping("/affinitys")
    public String listAffinitys(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
    	//boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
    	
    	model.addAttribute("affinitys", affinityService.findAll());
        return "views/affinitys";
    }

    //serviço para apresentar a pagina criar uma nova afinidade
    @GetMapping("/affinity/create")
    public String showEmptyTaskForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
    	model.addAttribute("affinity", new Affinity());
        return "forms/affinity-new";
    }
   
    //serviço que fara criação da nova afinidade e então retornar pagina principal de lista de afinidades
    @PostMapping("/affinity/create")
    public String createAffinity(@Valid Affinity affinity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/affinity-new";
        }
        affinityService.createAffinity(affinity);
        return "redirect:/affinitys/";
    }
    // Vai apresentar a pagina de edição da atividade
    @GetMapping("/affinity/edit/{id}")
    public String showFilledAffinityForm(@PathVariable Long id, Model model) {
        model.addAttribute("affinity", affinityService.getAffinityById(id));
        return "forms/affinity-edit";
    }
 // Vai salvar os dados alterados e voltar a pagina de listagem da atividade
    @PostMapping("/affinity/edit/{id}")
    public String updateAffinity(@Valid Affinity affinity, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/affinity-edit";
        }
        affinityService.updateAffinity(id, affinity);
        affinity = affinityService.getAffinityById(id);
        return "redirect:/affinitys/";
    }

    @GetMapping("/affinity/delete/{id}")
    public String deleteAffinity(@PathVariable Long id) {     
    	 affinityService.deleteAffinity(id);
         return "redirect:/affinitys";
    }
}
