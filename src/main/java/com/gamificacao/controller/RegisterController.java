package com.gamificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gamificacao.model.User;
import com.gamificacao.service.UserService;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }
    // Metodo Get para realizar cadastro de um novo colaborador
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "forms/register";
    }
    // Metodo Post para realizar cadastro de um novo colaborador
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/register";
        }

        if (userService.isUserEmailPresent(user.getEmail())) {
            model.addAttribute("exist", true);
            return "register";
        }
        //Se tudo certo, é cadastrado com sucesso e então vai para pagina de sucesso!
        userService.createUser(user);
        return "views/success";
    }

}
