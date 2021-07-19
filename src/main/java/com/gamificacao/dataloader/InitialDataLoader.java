package com.gamificacao.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.Item;
import com.gamificacao.model.Project;
import com.gamificacao.model.Role;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.service.AffinityService;
import com.gamificacao.service.ItemService;
import com.gamificacao.service.ProjectService;
import com.gamificacao.service.RoleService;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	private ProjectService projectService;
    private UserService userService;
    private TaskService taskService;
    private RoleService roleService;
    private AffinityService affinityService;
    private ItemService itemService;
    private final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Value("${default.admin.mail}")
    private String defaultAdminMail;
    @Value("${default.admin.name}")
    private String defaultAdminName;
    @Value("${default.admin.password}")
    private String defaultAdminPassword;
    @Value("${default.admin.image}")
    private String defaultAdminImage;

    @Autowired //Metodo Construtor Initial DataLoader
    public InitialDataLoader(UserService userService, TaskService taskService, RoleService roleService, ProjectService projectService, AffinityService affinityService, ItemService itemService) {
        this.userService = userService;
        this.taskService = taskService;
        this.roleService = roleService;
        this.projectService = projectService;
        this.affinityService = affinityService;
        this.itemService = itemService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //Papeis --------------------------------------------------------------------------------------------------------
        roleService.createRole(new Role("ADMIN")); // Ao iniciar o projeto, será criado o Papel de Admin (Gerente de Projetos) com o usuário de acesso na sequência
        roleService.createRole(new Role("USER")); // Ao iniciar o projeto, será criado o Papel de Colaborador com um usuário de acesso na sequência
        roleService.createRole(new Role("GAMEMASTER")); // Ao iniciar o projeto, será criado o Papel de Game Master com o usuário de acesso na sequência
        roleService.findAll().stream().map(role -> "Papel Salvo: " + role.getRole()).forEach(logger::info);

        //Usuários Iniciais --------------------------------------------------------------------------------------------------------
        //1 - Admin (Root) ou Usuário do Banco
        User admin = new User(
                defaultAdminMail,
                defaultAdminName,
                defaultAdminPassword,
                defaultAdminImage);
        userService.createUser(admin);
        userService.changeRoleToAdmin(admin); // Usuário com papel Admin não pode ser deletado

        //2 - Usuário Administrador Teste
        User manager = new User(
                "manager@mail.com",
                "Manager",
                "112233",
                "images/admin.png");
        userService.createUser(manager); 
        userService.changeRoleToAdmin(manager); // Usuário com papel Admin não pode ser deletado

        //3 - Usuário Tradicional Teste
        userService.createUser(new User(
                "user@mail.com",
                "User",
                "112233",
                "images/user.png"));

        //4 - Usuário GameMaster Teste
        userService.createUser(new User(
                "gamemaster@mail.com",
                "gamemaster",
                "112233",
                "images/admin.png"));

        
        userService.findAll().stream()
                .map(u -> "saved user: " + u.getName())
                .forEach(logger::info);

        //Projetos Tutoriais --------------------------------------------------------------------------------------------------------
        
        LocalDate today = LocalDate.now();
        //1
        
        Project project = projectService.createProject(new Project(
        		"Primeiro Projeto Teste",
        		today,
        		true,
        		userService.getUserByEmail("manager@mail.com").getName()
        ));
        
        //Atividades tutoriais --------------------------------------------------------------------------------------------------------

        //1
        taskService.createTask(new Task(
                "Primeira Atividade Teste",
                "Primeira Descrição Atividade Teste.",
                today.minusDays(40),
                true,
                userService.getUserByEmail("user@mail.com").getName(),
                userService.getUserByEmail("user@mail.com"),
                project
        ));

     //   2
        taskService.createTask(new Task(
                "Segunda Atividade Teste ",
                "Segunda Descrição Atividade Teste.",
                today.minusDays(30),
                true,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                project
        ));
        
      //Afinidades Iniciais --------------------------------------------------------------------------------------------------------

        affinityService.createAffinity(new Affinity(
        		"Atividades de Suporte",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de Suporte, tais como atendimento ao cliente, resolução de duvidas, entre outros"	
        ));
        
        affinityService.createAffinity(new Affinity(
        		"Atividades de Desenvolvimento",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de Desenvolvimento, tais como programar codigos, corrigir bugs, examinar codigo-fonte, entre outros"	
        ));
        
      //Itens Iniciais --------------------------------------------------------------------------------------------------------

        itemService.createItem(new Item(
        		"Primeiro Item teste",
        		"Primero Item A ser registrado no Sistema",
        		(long) 30
        ));

        taskService.findAll().stream().map(t -> "Atividade Salva: '" + t.getName()
                + "' Para o Responsavel: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
    }

    private String getOwnerNameOrNoOwner(Task task) {
        return task.getOwner() == null ? "Sem Responsavel" : task.getOwner().getName();
    }
}
