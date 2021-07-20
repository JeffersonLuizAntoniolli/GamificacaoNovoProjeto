package com.gamificacao;

import java.io.File;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.gamificacao.controller.UserController;

@SpringBootApplication
@ComponentScan({"com.gamificacao", "controller"}) // Fazer Scan de imagens na package controller
public class GamificacaoApplication {
	//Classe principal que inicializa o sistema
    public static void main(String[] args) {
    	new File(UserController.uploadDirectory).mkdir();
        SpringApplication.run(GamificacaoApplication.class, args);
    }

}
