package com.gamificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gamificacao.model.Item;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.service.ItemService;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String listItems(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
    	//boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
    	
    	model.addAttribute("items", itemService.findAll());
        return "views/items";
    }

    //serviço para apresentar a pagina criar uma novo item
    @GetMapping("/item/create")
    public String showEmptyTaskForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
    	model.addAttribute("item", new Item());
        return "forms/item-new";
    }
   
    //serviço que fara criação da novo item e então retornar pagina principal de lista de itens
    @PostMapping("/item/create")
    public String createItem(@Valid Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/item-new";
        }
        itemService.createItem(item);
        return "redirect:/items/";
    }
    // Vai apresentar a pagina de edição do item
    @GetMapping("/item/edit/{id}")
    public String showFilledItemForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.getItemById(id));
        return "forms/item-edit";
    }
 // Vai salvar os dados alterados e voltar a pagina de listagem da atividade
    @PostMapping("/item/edit/{id}")
    public String updateItem(@Valid Item item, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/item-edit";
        }
        itemService.updateItem(id, item);
        item = itemService.getItemById(id);
        return "redirect:/items/";
    }

    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable Long id) {     
    	 itemService.deleteItem(id);
         return "redirect:/items";
    }
}
