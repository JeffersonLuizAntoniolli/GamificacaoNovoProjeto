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

    	LocalDate today = LocalDate.now();
    	
        //Papeis --------------------------------------------------------------------------------------------------------
        roleService.createRole(new Role("ADMIN")); // Ao iniciar o projeto, ser?? criado o Papel de Admin (Gerente de Projetos) com o usu??rio de acesso na sequ??ncia
        roleService.createRole(new Role("USER")); // Ao iniciar o projeto, ser?? criado o Papel de Colaborador com um usu??rio de acesso na sequ??ncia
        roleService.createRole(new Role("GAMEMASTER")); // Ao iniciar o projeto, ser?? criado o Papel de Game Master com o usu??rio de acesso na sequ??ncia
        roleService.findAll().stream().map(role -> "Papel Salvo: " + role.getRole()).forEach(logger::info);

        //Usu??rio ROOT --------------------------------------------------------------------------------------------------------
        //1 - Admin (Root) ou Usu??rio do Banco
        User admin = new User(
                defaultAdminMail,
                defaultAdminName,
                defaultAdminPassword,
                defaultAdminImage);
        userService.createUser(admin);
        userService.changeRoleToAdmin(admin); // Usu??rio com papel Admin n??o pode ser deletado


      //Afinidades Iniciais --------------------------------------------------------------------------------------------------------

        Affinity suporte = affinityService.createAffinity(new Affinity(
        		"Atividades de Suporte",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades de Suporte, tais como atendimento ao cliente, resolu????o de duvidas, entre outros"	
        ));
        
        Affinity desenvolvimento = affinityService.createAffinity(new Affinity(
        		"Atividades de Desenvolvimento",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades de Desenvolvimento, tais como programar codigos, corrigir bugs, examinar codigo-fonte, entre outros"	
        ));
        
        Affinity migra????o = affinityService.createAffinity(new Affinity(
        		"Atividades de Migra????o",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades de Migra????o"	
        ));
        
        Affinity teste = affinityService.createAffinity(new Affinity(
        		"Atividades de Teste",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades de teste"	
        ));
     // afinidade padr??o focada para inicio do Game Master conforme for realizando as atividades do projeto de Game Master
        Affinity gamificacao = affinityService.createAffinity(new Affinity(
        		"Atividades de Gamifica????o",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades focadas na Gamifica????o."	
        ));
        // afinidade padr??o focada para inicio do administrador conforme for realizando as atividades do projeto de Administrador
        Affinity gerencia = affinityService.createAffinity(new Affinity(
        		"Atividades de Gerenciamento e Administra????o",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades focadas no Gerenciamento e Administra????o."	
        ));
        // afinidade padr??o focada para inicio do colaborador conforme for realizando as atividades do projeto de colaborador
        Affinity novico = affinityService.createAffinity(new Affinity(
        		"Atividades Novi??o",
        		"Est?? afinidade ?? acumulo de experi??ncias com todos os tipos de atividades focadas enquanto o colaborador for Novi??o, ou seja, estiver em aprendizado e adquirido conhecimento na empresa"	
        ));

        //Projetos Tutorial do Administrador --------------------------------------------------------------------------------------------------------
        
        //Usu??rio Administrador Teste
        User manager = new User(
                "manager@mail.com",
                "Manager",
                "112233",
                "images/admin.png");
        userService.createUser(manager); 
        userService.changeRoleToAdmin(manager); // Usu??rio com papel Admin n??o pode ser deletado
        
        //1
        //Projeto inicial do Admin
        Project projectAdmin = projectService.createProject(new Project(
        		"Aprendendo a mexer no sistema como Administrador do Sistema",
        		today,
        		false,
        		userService.getUserByEmail("manager@mail.com").getName()
        ));

        //Atividade Admin 1
        taskService.createTask(new Task(
                "Introdu????o ao Papel do Administrador",
                "Ol??, tudo bem? Vamos conhecer quais s??o as principais fun????es e papel do Administrador no sistema? O administrador ser??o aqueles executar??o a cria????o de atividades e projetos no sistema, ou seja, far??o a gest??o de atividades e servi??o dentro do sistema para que os demais colaboradores envolvidos. Como tamb??m poder??o executar atividades semelhantes do Colaborador, de poder comprar e utilizar os itens criados pelo Game Master do sistema. Vamos iniciar nossa jornada em como podemos interagir com o sistema, sendo inicialmente conclu??do est?? atividade na coluna Finalizado. Vamos l??!",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 2
        taskService.createTask(new Task(
                "Cadastro de Um Novo Projeto",
                "Primeiramente para inicializarmos com as a????es disponiveis que um Administrador ou um Gerente de Projetos pode ter no sistema, vamos come??ar olhando em como podemos cadastrar um novo Projeto para dar inicio as nossas listas de atividades. Para isso, uma vez que voc?? esteja logado no sistema como usu??rio Administrador, v?? no no menu Lista de Projetos localizado no cabe??alho da p??gina. Ent??o, ser?? visto lista de projetos tutoriais j?? previamente cadastrados no sistema, com objetivo de explicar e passar o conhecimento de como utilizar o sistema de gerenciamento de atividades com base no papel de cada usu??rio. Mas para criar seu pr??prio projeto, basta voc?? ir no bot??o Criar Novo Projeto, preencha os dados obrigat??rios e confirme em Salvar. Uma vez que essa etapa esteja finalizada, retorne ao projeto \"Aprendendo a mexer no sistema como Administrador do Sistema\" e finalize est?? atividade, pois estaremos indo para a pr??xima. DICA: Analisando a ultima coluna da lista de projetos, ser?? possivel ver que o administrador tamb??m editar ou ent??o deletar projetos, mas que se j?? estiver com atividades criadas em si, n??o ?? recomendado sua exclus??o. ",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 3
        taskService.createTask(new Task(
                "Cadastro de Atividades Dentro Projeto",
                "Uma vez que tenha cadastrado seu novo projeto, na lista de projetos, clique em cima do seu nome que voc?? ser?? direcionado para p??gina de lista de atividades daquele projeto, ent??o indo no bot??o clicar Nova Atividade, preencha todos os campos necess??rios e ent??o confime para Salvar. Preencha as atividades a serem feitas com os objetivos e quantidades desejaveis, para ent??o marcamos essa etapa tamb??m como finalizada e ent??o avan??armos.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 4
        taskService.createTask(new Task(
                "Cadastro e Controle de Usu??rios",
                "Pronto, temos projetos e atividades concluidas, agora precisaremos cadastrar novos usu??rios para que possamos ter o controle necess??rio e encaminharmos atividades para que ele ou eles sejam responsaveis. Para isso vamo no Menu Lista de Usu??rios, ao direcionado na pagina, ser?? possivel ver usu??rios j?? cadastrados no sistema, como o Administrador (Manager) e o Game Master. Mas o nosso foco para agora ser?? o bot??o para  registrarmos um novo usu??rio, que ao clicar no bot??o, ser?? necess??rio adicionar todos os campos obrigat??rios e confirmar para salva-lo. Com esse processo finalizado, voc?? poder?? marcar essa etapa para avan??armos. DICA: Analisando a ultima coluna da lista de usua??rios, ser?? possivel ver que o administrador tamb??m editar ou ent??o deletar usu??rios, mas que se j?? estiver com atividades consigos, n??o ?? recomendado sua exclus??o. ",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 5
        taskService.createTask(new Task(
                "Encaminhar Atividades para o Novo Usu??rio Registrado",
                "Com atividades e o usu??rio criado, vamos agora encaminhar as suas atividades criadas para este usu??rio. Para realizarmos esses passos, vamos retornar na lista de projetos, entrar no seu projeto criado no decorrer destas etapas e clicar no bot??o Designar Atividades. Na parte superior da p??gina, ser?? apresentando todos usu??rios cadastrados no sistema, ent??o ao clicar sobre o seu usu??rio que foi criado em etapas anteriores, vai ser atualizado a tabela de atividades a serem realizadas, encaminhe as atividades criadas do seu projeto pelo bot??o Encaminhar. Com isso estaremos finalizando tamb??m est?? etapa e podendo seguir para proxima. DICA: Para retirar uma atividade encaminhada, basta clicar no bot??o Retirar da atividade.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 6
        taskService.createTask(new Task(
                "Marcar As atividades Como Concluidas",
                "Uma vez que as suas atividades tenham sido realizadas, voc?? poder?? marcar-l?? como completadas por meio da coluna Finalizado, que seria exatamente igual a atividade que voc?? vinha fazendo para o seu segmento de etapas no decorrer desse projeto. Uma vez que voc?? realize essa a????o, voc?? estar?? dando pontos e exp. ao usu??rio que est?? como responsavel. Uma vez feita essa atividade, voc?? pode marca-l?? como finalizada para seguirmos em nossa jornada.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 7
        taskService.createTask(new Task(
                "Marcar O Projeto como Finalizado",
                "Este por sua vez quando tiver todas as suas atividades concluidas, ser?? um processo natural para que marquemos este projeto criado, tamb??m fique como finalizado, para assim seguirmos em frente com novos desafios.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 8
        taskService.createTask(new Task(
                "Conclus??o ao Papel do Administrador",
                "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Administrador ou Gerente de Projetos, achou interessante? Para encerramos perfeitamente este projeto, conclua est?? atividade. Ap??s isso, realizar os proximos projetos ou ent??o basta apenas se divertir da maneira que desejar no sistema. At?? Mais!!",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));

      //Projeto Tutorial do Game Master --------------------------------------------------------------------------------------------------------
        
     	//Usu??rio GameMaster Teste
        User gamemaster = new User(
                "gamemaster@mail.com",
                "gamemaster",
                "112233",
                "images/admin.png");
        userService.createUser(gamemaster); 
        userService.changeRoleToGameMaster(gamemaster); // Usu??rio com papel GameMaster n??o pode ser deletado
        //Projeto inicial do Game Master
        Project projectGameMaster = projectService.createProject(new Project(
        		"Aprendendo a mexer no sistema como Game Master",
        		today,
        		false,
        		userService.getUserByEmail("gamemaster@mail.com").getName()
        ));
        
      //Atividade Game Master - 1
        taskService.createTask(new Task(
                 "Introdu????o ao Papel do Game Master",
                 "Ol??, tudo bem? Vamos conhecer quais s??o as principais fun????es e papel do Game Master no sistema? Ele ser?? responsavel por realizar o cadastro e controle das afinidades, como tamb??m dos itens que poder??o ser comprados na loja pelos demais usu??rios. Ent??o ?? importante que se entenda a necessidade dos colaboradores para que cadastre os melhores para gerar engajamento e seja justo! Para come??armos, podemos ir na lista dessa atividade e marca-l?? como concluida na coluna Finalizado.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
      //Atividade Game Master - 2
        taskService.createTask(new Task(
                 "Cadastrando uma Nova Afinidade",
                 "Ao estar logado no sistema, v?? no menu Afinidade, clique em Criar nova Afinidade e preencha todos os campos as informa????es desejadas e clique no bot??o Confirmar. Ent??o volte este projeto e confirme essa atividade como concluida.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
      //Atividade Game Master - 3
        taskService.createTask(new Task(
                 "Editando sua Afinidade",
                 "Quando temos uma afinidade j?? cadastrada, podemos alterar seus dados caso necess??rio, basta ir no bot??o Editar na nova Afinidade cadastrada para realizar as altera??oes desejadas, se n??o desejar alterar nada, clique em cancelar. Ent??o volte este projeto e confirme essa atividade como concluida.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
        //Atividade Game Master - 4
        taskService.createTask(new Task(
                 "Removendo Afinidade da Lista",
                 "Agora quando necessitarmos remover uma afinidade por algum determinado motivo, podemos fazer-la indo no bot??o remover na linha da afinidade em si. Nesse caso vamos clicar no bot??o deletar e exlcuir afinidade rec??m criada. Ent??o volte este projeto e confirme essa atividade como concluida. DICA: ?? recomendado remover uma afinidade se ela for recente e n??o estiver j?? vinculada com atividades ou usu??rios do sistema.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
        //Atividade Game Master - 5
        taskService.createTask(new Task(
                 "Cadastrando um novo Item",
                 "Ao estar logado no sistema, v?? no menu da Loja, clique em Criar novo Item e preencha todos os campos conforme as informa????es desejadas e clique no bot??o Confirmar. Ent??o volte este projeto e confirme essa atividade como concluida.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
        //Atividade Game Master - 6
        taskService.createTask(new Task(
                 "Editando seu Item",
                 "Quando temos um item j?? cadastrado, podemos alterar seus dados caso necess??rio, basta ir no bot??o Editar na nova Afinidade cadastrada para realizar as altera??oes desejadas, se n??o desejar alterar nada, clique em cancelar. Ent??o volte este projeto e confirme essa atividade como concluida.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
          
        //Atividade Game Master - 7
        taskService.createTask(new Task(
                 "Removendo Item da Lista",
                 "Agora quando necessitarmos remover um Item por algum determinado motivo, podemos fazer-lo indo no bot??o remover na linha do em si. Nesse caso vamos clicar no bot??o deletar e exlcuir o item rec??m criado. Ent??o volte este projeto e confirme essa atividade como concluida. DICA: ?? recomendado remover um item se ele for recente e n??o estiver j?? vinculada usu??rios do sistema.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
        //Atividade Game Master - 8
        taskService.createTask(new Task(
                 "Conclus??o ao Papel do Game Master",
                 "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Game Master, achou interessante? Para encerramos perfeitamente este projeto, conclua est?? atividade. Ap??s isso, basta apenas se divertir da maneira que desejar no sistema.At?? Mais!!",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
       
        
        //Projeto Tutorial do Colaborador --------------------------------------------------------------------------------------------------------
        
     	//Usu??rio Colaborador Teste
        
        //1 - Usu??rio Colaborador Teste
        userService.createUser(new User(
                "user@mail.com",
                "User",
                "112233",
                "images/user.png"));
      //Projeto inicial do Colaborador
        Project project = projectService.createProject(new Project(
        		"Aprendendo a mexer no sistema como Colaborador",
        		today,
        		false,
        		userService.getUserByEmail("user@mail.com").getName()
        ));
        
      //Atividade Colaborador - 1
        taskService.createTask(new Task(
                 "Introdu????o ao Papel do Colaborador",
                 "Ol??, tudo bem? Vamos conhecer quais s??o as principais fun????es e papel do Colaborador no sistema? Os colaboradores ser??o os usu??rios casuais no uso do sistema, ser??o aqueles executar??o as atividades dos projetos criados pelo Administrador ou Gerente de Projetos, como tamb??m aqueles que comprar??o e utilizara????o os itens criados pelo Game Master do sistema. De forma resumida, s??o os jogadores ou ent??o os players. Vamos iniciar nossa jornada em como podemos interagir com o sistema, sendo inicialmente concluido est?? atividade na coluna Finlizado. Vamos l??!",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 2
        taskService.createTask(new Task(
                 "Acessando sua pagina de perfil",
                 "Ser?? na pagina de Perfil que encontraremos nossos principais dados pessoais e demais registros que nos cerca enquanto um usu??rio do sistema. Para entrar no cadastro de perfil, v?? no cabe??alho da pagina onde apresenta a descri????o de email do usu??rio logado, proximo a op????o de sair (fazer logout do sistema). Uma vez que chegue na pagina de perfil, voc?? poder?? estar concluido essa atividade eent??o partirmos para pr??xima da lista do projeto.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 3
        taskService.createTask(new Task(
                 "Atualizando uma imagem de Avatar",
                 "Acessando a pagina de perfil, ser?? possivel verificar a possibilidade atualizar a sua imagem de usu??rio com uma imagem que desejar gerar seu avatar dentro do sistema. Para realizarmos esse processo, basta clicar no bot??o Atualizar meu Avatar e ent??o confirmar a imagem que desejarmos. Pronto! Com essa atividade feita, poderemos conclui-la e ent??o seguirmos adiante.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 4
        taskService.createTask(new Task(
                 "Vendo Lista Afinidades do Perfil",
                 "As afinidades ser??o uma caracteristica, experiencia e representa????o tipo de atividade que voc?? executa ou j?? executou no sistema e para cada atividade que concluirmos, ganharemos experi??ncia nessa afinidade, como tamb??m a experiencia do usu??rio de forma geral. Como j?? foram concluidas atividades anteriores no decorrer do andamento dese projeto, voc?? vai vizualizar que j?? existe um nivel de experiencia e uma afinidade sendo apresentada. Com essa explica????o, concluimos nossa atividade e ent??o poderemos ir para proxima deixando est?? como finalizada.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 5
        taskService.createTask(new Task(
                 "Comprando um Item",
                 "?? possivel perceber na pagina de perfil do usu??rio, onde ?? mostrado os dados pessoais do usuario como nome e email, ver?? que tem uma informa????o ferente ?? pontos. Com eles, podemos ir ao menu Loja no cabe??alho da pagina e ent??o fazer uma compra com item que desejarmos (Ou que conseguirmos comprar com os pontos que temos! xD). Uma vez que voc?? comprar o item desejado, poderemos realizar a finaliza????o dessa atividade e darmos continuidade com a nossa lista. Vamos l??!",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 6
        taskService.createTask(new Task(
                 "Vendo Itens Comprados",
                 "\n"
                 + "Uma vez que tenha comprado o item desejado, voc?? poder?? ve-lo na p??gina de perfil do usuario. Ao ver o item comprado, voc?? poder?? clicar no bot??o Utiliza-lo para quando desejar querer executar ou solicitar algo que esteja relacionado contexto do seu item comprado. DICA: Uma vez que voc?? clicar que deseja utiliza-lo, voc?? n??o poder?? mais utiliza-lo novamente. Ser?? necess??rio comprar o item novamente. Uma vem que realize essa etapa, voc?? poder?? marcar est?? atividade como concluida para ent??o prosseguirmos.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 7
        taskService.createTask(new Task(
                 "Vendo o Ranking",
                 "Uma vez que tenha comprado o item desejado, voc?? poder?? ve-lo na p??gina de perfil do usuario. Ao ver o item comprado, voc?? poder?? clicar no bot??o Utiliza-lo para quando desejar querer executar ou solicitar algo que esteja relacionado contexto do seu item comprado. DICA: Uma vez que voc?? clicar que deseja utiliza-lo, voc?? n??o poder?? mais utiliza-lo novamente. Ser?? necess??rio comprar o item novamente. Uma vem que realize essa etapa, voc?? poder?? marcar est?? atividade como concluida para ent??o prosseguirmos.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 8
        taskService.createTask(new Task(
                 "Conclus??o ao Papel do Colaborador",
                 "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Coaborador, achou interessante? Para encerramos perfeitamente este projeto, conclua est?? atividade. Ap??s isso, realizar os proximos projetos ou ent??o basta apenas se divertir da maneira que desejar no sistema. At?? Mais!!",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Itens Iniciais --------------------------------------------------------------------------------------------------------

        itemService.createItem(new Item(
        		"Vale Cerveja latinha Conti",
        		"Esse item permite que voc?? possa trocar os pontos ganhos por uma latinha de cerveja Conti 269 ML",
        		(long) 2
        ));
        
        itemService.createItem(new Item(
        		"Vale Criptomoeda",
        		"Esse item permite que voc?? possa trocar os pontos ganhos por um valor 0.000000000001 Fragmento de CriptoMoedaGenerica",
        		(long) 10
        ));
        
        itemService.createItem(new Item(
        		"Bau Misterioso",
        		"Esse item permite que voc?? possa trocar os pontos ganhos por um ba?? misterioso do GameMaster (Pode conter Pegadinha)",
        		(long) 6
        ));
        
        itemService.createItem(new Item(
        		"Bau Misterioso Sagrado",
        		"Esse item permite que voc?? possa trocar os pontos ganhos por um ba?? misterioso do GameMaster (Garantido de n??o haver Pegadinha)",
        		(long) 30
        ));
        
        userService.findAll().stream()
        .map(u -> "saved user: " + u.getName())
        .forEach(logger::info);

        taskService.findAll().stream().map(t -> "Atividade Salva: '" + t.getName()
                + "' Para o Responsavel: " + getOwnerNameOrNoOwner(t)).forEach(logger::info);
    }

    private String getOwnerNameOrNoOwner(Task task) {
        return task.getOwner() == null ? "Sem Responsavel" : task.getOwner().getName();
    }
}
