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
        roleService.createRole(new Role("ADMIN")); // Ao iniciar o projeto, será criado o Papel de Admin (Gerente de Projetos) com o usuário de acesso na sequência
        roleService.createRole(new Role("USER")); // Ao iniciar o projeto, será criado o Papel de Colaborador com um usuário de acesso na sequência
        roleService.createRole(new Role("GAMEMASTER")); // Ao iniciar o projeto, será criado o Papel de Game Master com o usuário de acesso na sequência
        roleService.findAll().stream().map(role -> "Papel Salvo: " + role.getRole()).forEach(logger::info);

        //Usuário ROOT --------------------------------------------------------------------------------------------------------
        //1 - Admin (Root) ou Usuário do Banco
        User admin = new User(
                defaultAdminMail,
                defaultAdminName,
                defaultAdminPassword,
                defaultAdminImage);
        userService.createUser(admin);
        userService.changeRoleToAdmin(admin); // Usuário com papel Admin não pode ser deletado


      //Afinidades Iniciais --------------------------------------------------------------------------------------------------------

        Affinity suporte = affinityService.createAffinity(new Affinity(
        		"Atividades de Suporte",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de Suporte, tais como atendimento ao cliente, resolução de duvidas, entre outros"	
        ));
        
        Affinity desenvolvimento = affinityService.createAffinity(new Affinity(
        		"Atividades de Desenvolvimento",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de Desenvolvimento, tais como programar codigos, corrigir bugs, examinar codigo-fonte, entre outros"	
        ));
        
        Affinity migração = affinityService.createAffinity(new Affinity(
        		"Atividades de Migração",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de Migração"	
        ));
        
        Affinity teste = affinityService.createAffinity(new Affinity(
        		"Atividades de Teste",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades de teste"	
        ));
     // afinidade padrão focada para inicio do Game Master conforme for realizando as atividades do projeto de Game Master
        Affinity gamificacao = affinityService.createAffinity(new Affinity(
        		"Atividades de Gamificação",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades focadas na Gamificação."	
        ));
        // afinidade padrão focada para inicio do administrador conforme for realizando as atividades do projeto de Administrador
        Affinity gerencia = affinityService.createAffinity(new Affinity(
        		"Atividades de Gerenciamento e Administração",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades focadas no Gerenciamento e Administração."	
        ));
        // afinidade padrão focada para inicio do colaborador conforme for realizando as atividades do projeto de colaborador
        Affinity novico = affinityService.createAffinity(new Affinity(
        		"Atividades Noviço",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades focadas enquanto o colaborador for Noviço, ou seja, estiver em aprendizado e adquirido conhecimento na empresa"	
        ));

        //Projetos Tutorial do Administrador --------------------------------------------------------------------------------------------------------
        
        //Usuário Administrador Teste
        User manager = new User(
                "manager@mail.com",
                "Manager",
                "112233",
                "images/admin.png");
        userService.createUser(manager); 
        userService.changeRoleToAdmin(manager); // Usuário com papel Admin não pode ser deletado
        
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
                "Introdução ao Papel do Administrador",
                "Olá, tudo bem? Vamos conhecer quais são as principais funções e papel do Administrador no sistema? O administrador serão aqueles executarão a criação de atividades e projetos no sistema, ou seja, farão a gestão de atividades e serviço dentro do sistema para que os demais colaboradores envolvidos. Como também poderão executar atividades semelhantes do Colaborador, de poder comprar e utilizar os itens criados pelo Game Master do sistema. Vamos iniciar nossa jornada em como podemos interagir com o sistema, sendo inicialmente concluído está atividade na coluna Finalizado. Vamos lá!",
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
                "Primeiramente para inicializarmos com as ações disponiveis que um Administrador ou um Gerente de Projetos pode ter no sistema, vamos começar olhando em como podemos cadastrar um novo Projeto para dar inicio as nossas listas de atividades. Para isso, uma vez que você esteja logado no sistema como usuário Administrador, vá no no menu Lista de Projetos localizado no cabeçalho da página. Então, será visto lista de projetos tutoriais já previamente cadastrados no sistema, com objetivo de explicar e passar o conhecimento de como utilizar o sistema de gerenciamento de atividades com base no papel de cada usuário. Mas para criar seu próprio projeto, basta você ir no botão Criar Novo Projeto, preencha os dados obrigatórios e confirme em Salvar. Uma vez que essa etapa esteja finalizada, retorne ao projeto \"Aprendendo a mexer no sistema como Administrador do Sistema\" e finalize está atividade, pois estaremos indo para a próxima. DICA: Analisando a ultima coluna da lista de projetos, será possivel ver que o administrador também editar ou então deletar projetos, mas que se já estiver com atividades criadas em si, não é recomendado sua exclusão. ",
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
                "Uma vez que tenha cadastrado seu novo projeto, na lista de projetos, clique em cima do seu nome que você será direcionado para página de lista de atividades daquele projeto, então indo no botão clicar Nova Atividade, preencha todos os campos necessários e então confime para Salvar. Preencha as atividades a serem feitas com os objetivos e quantidades desejaveis, para então marcamos essa etapa também como finalizada e então avançarmos.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 4
        taskService.createTask(new Task(
                "Cadastro e Controle de Usuários",
                "Pronto, temos projetos e atividades concluidas, agora precisaremos cadastrar novos usuários para que possamos ter o controle necessário e encaminharmos atividades para que ele ou eles sejam responsaveis. Para isso vamo no Menu Lista de Usuários, ao direcionado na pagina, será possivel ver usuários já cadastrados no sistema, como o Administrador (Manager) e o Game Master. Mas o nosso foco para agora será o botão para  registrarmos um novo usuário, que ao clicar no botão, será necessário adicionar todos os campos obrigatórios e confirmar para salva-lo. Com esse processo finalizado, você poderá marcar essa etapa para avançarmos. DICA: Analisando a ultima coluna da lista de usua´rios, será possivel ver que o administrador também editar ou então deletar usuários, mas que se já estiver com atividades consigos, não é recomendado sua exclusão. ",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 5
        taskService.createTask(new Task(
                "Encaminhar Atividades para o Novo Usuário Registrado",
                "Com atividades e o usuário criado, vamos agora encaminhar as suas atividades criadas para este usuário. Para realizarmos esses passos, vamos retornar na lista de projetos, entrar no seu projeto criado no decorrer destas etapas e clicar no botão Designar Atividades. Na parte superior da página, será apresentando todos usuários cadastrados no sistema, então ao clicar sobre o seu usuário que foi criado em etapas anteriores, vai ser atualizado a tabela de atividades a serem realizadas, encaminhe as atividades criadas do seu projeto pelo botão Encaminhar. Com isso estaremos finalizando também está etapa e podendo seguir para proxima. DICA: Para retirar uma atividade encaminhada, basta clicar no botão Retirar da atividade.",
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
                "Uma vez que as suas atividades tenham sido realizadas, você poderá marcar-lá como completadas por meio da coluna Finalizado, que seria exatamente igual a atividade que você vinha fazendo para o seu segmento de etapas no decorrer desse projeto. Uma vez que você realize essa ação, você estará dando pontos e exp. ao usuário que está como responsavel. Uma vez feita essa atividade, você pode marca-lá como finalizada para seguirmos em nossa jornada.",
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
                "Este por sua vez quando tiver todas as suas atividades concluidas, será um processo natural para que marquemos este projeto criado, também fique como finalizado, para assim seguirmos em frente com novos desafios.",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));
        
      //Atividade Admin 8
        taskService.createTask(new Task(
                "Conclusão ao Papel do Administrador",
                "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Administrador ou Gerente de Projetos, achou interessante? Para encerramos perfeitamente este projeto, conclua está atividade. Após isso, realizar os proximos projetos ou então basta apenas se divertir da maneira que desejar no sistema. Até Mais!!",
                today.minusDays(-2),
                false,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));

      //Projeto Tutorial do Game Master --------------------------------------------------------------------------------------------------------
        
     	//Usuário GameMaster Teste
        User gamemaster = new User(
                "gamemaster@mail.com",
                "gamemaster",
                "112233",
                "images/admin.png");
        userService.createUser(gamemaster); 
        userService.changeRoleToGameMaster(gamemaster); // Usuário com papel GameMaster não pode ser deletado
        //Projeto inicial do Game Master
        Project projectGameMaster = projectService.createProject(new Project(
        		"Aprendendo a mexer no sistema como Game Master",
        		today,
        		false,
        		userService.getUserByEmail("gamemaster@mail.com").getName()
        ));
        
      //Atividade Game Master - 1
        taskService.createTask(new Task(
                 "Introdução ao Papel do Game Master",
                 "Olá, tudo bem? Vamos conhecer quais são as principais funções e papel do Game Master no sistema? Ele será responsavel por realizar o cadastro e controle das afinidades, como também dos itens que poderão ser comprados na loja pelos demais usuários. Então é importante que se entenda a necessidade dos colaboradores para que cadastre os melhores para gerar engajamento e seja justo! Para começarmos, podemos ir na lista dessa atividade e marca-lá como concluida na coluna Finalizado.",
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
                 "Ao estar logado no sistema, vá no menu Afinidade, clique em Criar nova Afinidade e preencha todos os campos as informações desejadas e clique no botão Confirmar. Então volte este projeto e confirme essa atividade como concluida.",
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
                 "Quando temos uma afinidade já cadastrada, podemos alterar seus dados caso necessário, basta ir no botão Editar na nova Afinidade cadastrada para realizar as alteraçoes desejadas, se não desejar alterar nada, clique em cancelar. Então volte este projeto e confirme essa atividade como concluida.",
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
                 "Agora quando necessitarmos remover uma afinidade por algum determinado motivo, podemos fazer-la indo no botão remover na linha da afinidade em si. Nesse caso vamos clicar no botão deletar e exlcuir afinidade recém criada. Então volte este projeto e confirme essa atividade como concluida. DICA: É recomendado remover uma afinidade se ela for recente e não estiver já vinculada com atividades ou usuários do sistema.",
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
                 "Ao estar logado no sistema, vá no menu da Loja, clique em Criar novo Item e preencha todos os campos conforme as informações desejadas e clique no botão Confirmar. Então volte este projeto e confirme essa atividade como concluida.",
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
                 "Quando temos um item já cadastrado, podemos alterar seus dados caso necessário, basta ir no botão Editar na nova Afinidade cadastrada para realizar as alteraçoes desejadas, se não desejar alterar nada, clique em cancelar. Então volte este projeto e confirme essa atividade como concluida.",
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
                 "Agora quando necessitarmos remover um Item por algum determinado motivo, podemos fazer-lo indo no botão remover na linha do em si. Nesse caso vamos clicar no botão deletar e exlcuir o item recém criado. Então volte este projeto e confirme essa atividade como concluida. DICA: É recomendado remover um item se ele for recente e não estiver já vinculada usuários do sistema.",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
        
        //Atividade Game Master - 8
        taskService.createTask(new Task(
                 "Conclusão ao Papel do Game Master",
                 "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Game Master, achou interessante? Para encerramos perfeitamente este projeto, conclua está atividade. Após isso, basta apenas se divertir da maneira que desejar no sistema.Até Mais!!",
                 today.minusDays(-3),
                 false,
                 userService.getUserByEmail("gamemaster@mail.com").getName(),
                 userService.getUserByEmail("gamemaster@mail.com"),
                 projectGameMaster,
                 gamificacao   
         ));
       
        
        //Projeto Tutorial do Colaborador --------------------------------------------------------------------------------------------------------
        
     	//Usuário Colaborador Teste
        
        //1 - Usuário Colaborador Teste
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
                 "Introdução ao Papel do Colaborador",
                 "Olá, tudo bem? Vamos conhecer quais são as principais funções e papel do Colaborador no sistema? Os colaboradores serão os usuários casuais no uso do sistema, serão aqueles executarão as atividades dos projetos criados pelo Administrador ou Gerente de Projetos, como também aqueles que comprarão e utilizaração os itens criados pelo Game Master do sistema. De forma resumida, são os jogadores ou então os players. Vamos iniciar nossa jornada em como podemos interagir com o sistema, sendo inicialmente concluido está atividade na coluna Finlizado. Vamos lá!",
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
                 "Será na pagina de Perfil que encontraremos nossos principais dados pessoais e demais registros que nos cerca enquanto um usuário do sistema. Para entrar no cadastro de perfil, vá no cabeçalho da pagina onde apresenta a descrição de email do usuário logado, proximo a opção de sair (fazer logout do sistema). Uma vez que chegue na pagina de perfil, você poderá estar concluido essa atividade eentão partirmos para próxima da lista do projeto.",
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
                 "Acessando a pagina de perfil, será possivel verificar a possibilidade atualizar a sua imagem de usuário com uma imagem que desejar gerar seu avatar dentro do sistema. Para realizarmos esse processo, basta clicar no botão Atualizar meu Avatar e então confirmar a imagem que desejarmos. Pronto! Com essa atividade feita, poderemos conclui-la e então seguirmos adiante.",
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
                 "As afinidades serão uma caracteristica, experiencia e representação tipo de atividade que você executa ou já executou no sistema e para cada atividade que concluirmos, ganharemos experiência nessa afinidade, como também a experiencia do usuário de forma geral. Como já foram concluidas atividades anteriores no decorrer do andamento dese projeto, você vai vizualizar que já existe um nivel de experiencia e uma afinidade sendo apresentada. Com essa explicação, concluimos nossa atividade e então poderemos ir para proxima deixando está como finalizada.",
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
                 "É possivel perceber na pagina de perfil do usuário, onde é mostrado os dados pessoais do usuario como nome e email, verá que tem uma informação ferente à pontos. Com eles, podemos ir ao menu Loja no cabeçalho da pagina e então fazer uma compra com item que desejarmos (Ou que conseguirmos comprar com os pontos que temos! xD). Uma vez que você comprar o item desejado, poderemos realizar a finalização dessa atividade e darmos continuidade com a nossa lista. Vamos lá!",
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
                 + "Uma vez que tenha comprado o item desejado, você poderá ve-lo na página de perfil do usuario. Ao ver o item comprado, você poderá clicar no botão Utiliza-lo para quando desejar querer executar ou solicitar algo que esteja relacionado contexto do seu item comprado. DICA: Uma vez que você clicar que deseja utiliza-lo, você não poderá mais utiliza-lo novamente. Será necessário comprar o item novamente. Uma vem que realize essa etapa, você poderá marcar está atividade como concluida para então prosseguirmos.",
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
                 "Uma vez que tenha comprado o item desejado, você poderá ve-lo na página de perfil do usuario. Ao ver o item comprado, você poderá clicar no botão Utiliza-lo para quando desejar querer executar ou solicitar algo que esteja relacionado contexto do seu item comprado. DICA: Uma vez que você clicar que deseja utiliza-lo, você não poderá mais utiliza-lo novamente. Será necessário comprar o item novamente. Uma vem que realize essa etapa, você poderá marcar está atividade como concluida para então prosseguirmos.",
                 today.minusDays(-2),
                 false,
                 userService.getUserByEmail("user@mail.com").getName(),
                 userService.getUserByEmail("user@mail.com"),
                 project,
                 novico   
         ));
        
      //Atividade Colaborador - 8
        taskService.createTask(new Task(
                 "Conclusão ao Papel do Colaborador",
                 "Com isso finalizamos nossas ultimas atividades no projeto Aprendendo a mexer no sistema como Coaborador, achou interessante? Para encerramos perfeitamente este projeto, conclua está atividade. Após isso, realizar os proximos projetos ou então basta apenas se divertir da maneira que desejar no sistema. Até Mais!!",
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
        		"Esse item permite que você possa trocar os pontos ganhos por uma latinha de cerveja Conti 269 ML",
        		(long) 2
        ));
        
        itemService.createItem(new Item(
        		"Vale Criptomoeda",
        		"Esse item permite que você possa trocar os pontos ganhos por um valor 0.000000000001 Fragmento de CriptoMoedaGenerica",
        		(long) 10
        ));
        
        itemService.createItem(new Item(
        		"Bau Misterioso",
        		"Esse item permite que você possa trocar os pontos ganhos por um baú misterioso do GameMaster (Pode conter Pegadinha)",
        		(long) 6
        ));
        
        itemService.createItem(new Item(
        		"Bau Misterioso Sagrado",
        		"Esse item permite que você possa trocar os pontos ganhos por um baú misterioso do GameMaster (Garantido de não haver Pegadinha)",
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
