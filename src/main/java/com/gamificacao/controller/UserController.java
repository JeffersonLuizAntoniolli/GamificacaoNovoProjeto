package com.gamificacao.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.gamificacao.model.User;
import com.gamificacao.service.UserService;

@Controller
public class UserController {

    private UserService userService;
    public static String uploadDirectory = System.getProperty("user")+"uploads"; // atributo para configurar diretorio do upload

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users") // Metodo que realiza a consulta de novos usuarios do sistema
    public String listUsers(Model model, SecurityContextHolderAwareRequestWrapper request) {
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("users", userService.findAll());
        model.addAttribute("isAdminSigned", isAdminSigned);
        return "views/users";
    }
    // Metodo que fará a remoção do usuário do sistema
    @GetMapping("user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
    
 // Vai apresentar a pagina de edição do perfil usuário
    @GetMapping("/user/edit/{id}")
    public String showFilledUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "forms/user-edit";
    }
    // Vai salvar os dados alterados e voltar a pagina de listagem de usuarios
    @PostMapping("/user/edit/{id}")
    public String updateUser(@Valid User user, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/user-edit";
        }
        userService.updateUser(id, user);
        //user = userService.getUserById(id);
        return "redirect:/users";
    }

    @GetMapping("user/uploadavatar") // Serviço para realizar o upload da Imagem
    public String UploadAvatar(Model model) {
    	return "forms/uploadavatar";
    }
    
	/*
	 * @PostMapping("user/uploadavatar/upload") // Serviço para realizar o upload da
	 * Imagem public String upload(Model model, @ResquestParam("files")
	 * MultipartFile[] files) { StringBuilder fileNames = new StringBuilder();
	 * for(MultipartFile file : files) { Path fileNameAndPath =
	 * Paths.get(uploadDirectory); fileNames.append(file.getOriginalFilename()); } }
	 */
}
