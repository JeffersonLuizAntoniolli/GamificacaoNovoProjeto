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
        
        Affinity gamificacao = affinityService.createAffinity(new Affinity(
        		"Atividades de Gamificação",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades focadas na Gamificação."	
        ));
        
        Affinity gerencia = affinityService.createAffinity(new Affinity(
        		"Atividades de Gerenciamento e Administração",
        		"Está afinidade é acumulo de experiências com todos os tipos de atividades focadas no Gerenciamento e Administração."	
        ));
        
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
        
        Project projectAdmin = projectService.createProject(new Project(
        		"Aprendendo a mexer no sistema como Administrador do Sistema",
        		today,
        		true,
        		userService.getUserByEmail("manager@mail.com").getName()
        ));

        //Atividade Admin 1
        taskService.createTask(new Task(
                "Primeira Atividade Teste",
                "Primeira Descrição Atividade Teste.",
                today.minusDays(-1),
                true,
                userService.getUserByEmail("manager@mail.com").getName(),
                userService.getUserByEmail("manager@mail.com"),
                projectAdmin,
                gerencia   
        ));

     //Atividade Admin 2
        taskService.createTask(new Task(
                "Segunda Atividade Teste ",
                "Segunda Descrição Atividade Teste.",
                today.minusDays(-2),
                true,
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
        //Projeto de atividades para o Colaborador
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
                 today.minusDays(-1),
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
