package com.gamificacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    String showLoginForm() {
        //o login é enviado utilizando o método POST na <form th: action = "@ {/ login}" method = "post"> 
        return "forms/login";
    }
}
