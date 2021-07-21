package com.gamificacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RankingController {

    @GetMapping("/ranking")
    String showIndex() {
        return "views/ranking"; // retorna a pagina inicial
    }
}
