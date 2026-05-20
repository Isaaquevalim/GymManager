package main;

// Importamos todas as classes que vamos usar.
// As classes DAO fazem a comunicação com o banco, e as Entities são os nossos objetos (Aluno, Plano, etc).
import dao.AlunoDAO;
import dao.AulaDAO;
import dao.FrequenciaDAO;
import dao.InstrutorDAO;
import dao.PlanoDAO;
import entities.Aluno;
import entities.Aula;
import entities.Instrutor;
import entities.Plano;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // O Scanner é a nossa porta de entrada: é ele que vai ler tudo o que a gente digitar no terminal.
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        // Antes do menu iniciar, a gente prepara os DAOs.
        // Eles são as nossas "ferramentas" para inserir ou buscar dados no PostgreSQL.
        AlunoDAO alunoDAO = new AlunoDAO();
        PlanoDAO planoDAO = new PlanoDAO();
        InstrutorDAO instrutorDAO = new InstrutorDAO();
        FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
        AulaDAO aulaDAO = new AulaDAO();

        // O 'do-while' garante que o menu vai aparecer pelo menos uma vez e
        // vai continuar rodando em loop até alguém digitar '0'.
        do {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ACADEMIA (GYM MANAGER) ===");
            System.out.println("1. Gerenciar Alunos (CRUD)");
            System.out.println("2. Gerenciar Planos");
            System.out.println("3. Gerenciar Instrutores");
            System.out.println("4. Gerenciar Aulas");
            System.out.println("5. Registrar Frequência (Entrada)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            opcaoPrincipal = scanner.nextInt();

            // ESSA LINHA É UM SALVA-VIDAS!
            // Quando a gente digita um número e aperta Enter, o Java lê o número mas deixa o "Enter" na memória.
            // Se a gente não "limpar" esse Enter com o nextLine() vazio, o próximo texto que o sistema pedir vai ser pulado sozinho.
            scanner.nextLine();

            // Direciona o sistema para o sub-menu que o usuário escolheu.
            switch (opcaoPrincipal) {
                case 1:
                    menuAlunos(scanner, alunoDAO, planoDAO);
                    break;
                case 2:
                    menuPlanos(scanner, planoDAO);
                    break;
                case 3:
                    menuInstrutores(scanner, instrutorDAO);
                    break;
                case 4:
                    // Repare que o menu de aulas precisa tanto do DAO de aulas quanto do instrutor e aluno,
                    // porque a aula junta todas essas informações (relacionamentos do banco).
                    menuAulas(scanner, aulaDAO, instrutorDAO, alunoDAO);
                    break;
                case 5:
                    menuFrequencia(scanner, frequenciaDAO);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema... Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoPrincipal != 0);

        // Sempre feche o Scanner no final do programa para não vazar memória RAM.
        scanner.close();
    }

    // --- SUB-MENUS ---
    // Separamos cada entidade em uma função diferente para o 'main' não virar um arquivo de 1000 linhas impossível de ler.

    private static void menuAlunos(Scanner scanner, AlunoDAO alunoDAO, PlanoDAO planoDAO) {
        int opcaoAluno = -1;

        do {
            System.out.println("\n--- GERENCIAR ALUNOS ---");
            System.out.println("1. Cadastrar Novo Aluno (Create)");
            System.out.println("2. Listar Todos os Alunos (Read)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");

            opcaoAluno = scanner.nextInt();
            scanner.nextLine(); // Limpa o "Enter" fantasma

            switch (opcaoAluno) {
                case 1:
                    // Coleta dos dados linha por linha
                    System.out.print("Digite o nome do aluno: ");
                    String nome = scanner.nextLine();

                    System.out.print("Digite o CPF (Apenas números): ");
                    String cpf = scanner.nextLine();

                    System.out.print("Data de Nascimento (DD/MM/AAAA): ");
                    String dataNascStr = scanner.nextLine();

                    // O banco de dados e o Java trabalham com datas em formatos diferentes do que o brasileiro costuma digitar.
                    // Esse DateTimeFormatter traduz o nosso "25/05/2000" para o padrão internacional do Java.
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataNascimento = LocalDate.parse(dataNascStr, formatter);

                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();

                    System.out.print("E-mail: ");
                    String email = scanner.nextLine();

                    // Criamos o objeto Aluno na memória.
                    // Repare que passamos '0' como ID. Fazemos isso porque o banco de dados (PostgreSQL)
                    // está configurado para gerar o ID automaticamente quando salvarmos a linha.
                    Aluno novoAluno = new Aluno(0, nome, cpf, dataNascimento, telefone, email, LocalDate.now(), null);

                    // Manda o objeto prontinho pro DAO fazer o trabalho de gerar o INSERT SQL.
                    alunoDAO.salvar(novoAluno);
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE ALUNOS CADASTRADOS ---");
                    // Puxa do banco de dados uma lista completa com todos os alunos já inseridos
                    List<Aluno> lista = alunoDAO.listarTodos();

                    if (lista.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado no banco de dados.");
                    } else {
                        // O laço 'for' passa por cada aluno da lista e imprime os dados principais na tela
                        for (Aluno a : lista) {
                            System.out.println("ID: " + a.getId() + " | Nome: " + a.getNome() + " | CPF: " + a.getCpf());
                        }
                    }
                    break;

                case 0:
                    System.out.println("Retornando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcaoAluno != 0);
    }

    private static void menuPlanos(Scanner scanner, PlanoDAO planoDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR PLANOS ---");
            System.out.println("1. Cadastrar Novo Plano (Create)");
            System.out.println("2. Buscar Plano por ID (Read)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome do Plano (ex: Gold, Black): ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição dos benefícios: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Valor Mensal: ");
                    double valor = scanner.nextDouble();
                    System.out.print("Duração (em meses): ");
                    int duracao = scanner.nextInt();
                    scanner.nextLine(); // Mais um "limpa buffer" após ler um número
                    System.out.print("Benefícios inclusos adicionais: ");
                    String beneficios = scanner.nextLine();

                    // Junta todos os dados soltos no pacote (objeto) Plano e joga pro banco
                    Plano novoPlano = new Plano(0, nome, descricao, valor, duracao, beneficios);
                    planoDAO.salvar(novoPlano);
                    break;

                case 2:
                    System.out.print("Digite o ID do plano que deseja buscar: ");
                    int idBusca = scanner.nextInt();

                    // Vai no banco, procura o ID e retorna os dados se achar
                    Plano plano = planoDAO.buscarPorId(idBusca);

                    if (plano != null) {
                        System.out.println("\nPlano Encontrado:");
                        System.out.println("Nome: " + plano.getNome() + " | Valor: R$ " + plano.getValorMensal() + " | Duração: " + plano.getDuracaoMeses() + " meses");
                    } else {
                        System.out.println("Nenhum plano encontrado com o ID informado.");
                    }
                    break;

                case 0:
                    System.out.println("Retornando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuInstrutores(Scanner scanner, InstrutorDAO instrutorDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR INSTRUTORES ---");
            System.out.println("1. Cadastrar Novo Instrutor (Create)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome do Instrutor: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();
                    System.out.print("Especialidade (Musculação, Pilates, Yoga...): ");
                    String especialidade = scanner.nextLine();
                    System.out.print("Horários de Trabalho: ");
                    String horarios = scanner.nextLine();

                    Instrutor novoInstrutor = new Instrutor(0, nome, cpf, telefone, especialidade, horarios);
                    instrutorDAO.salvar(novoInstrutor);
                    break;

                case 0:
                    System.out.println("Retornando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuAulas(Scanner scanner, AulaDAO aulaDAO, InstrutorDAO instrutorDAO, AlunoDAO alunoDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR AULAS COLETIVAS ---");
            System.out.println("1. Criar Nova Aula Coletiva (Create)");
            System.out.println("2. Inscrever Aluno em uma Aula (Relacionamento N:M)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome da Aula (ex: Crossfit, Spinning): ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição da atividade: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Capacidade Máxima de Alunos: ");
                    int capacidade = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Horário (Formato AAA-MM-DDTHH:MM, ex: 2026-05-20T19:00): ");
                    String horarioStr = scanner.nextLine();
                    java.time.LocalDateTime horario = java.time.LocalDateTime.parse(horarioStr);
                    System.out.print("Duração (em minutos): ");
                    int duracao = scanner.nextInt();

                    System.out.print("ID do Instrutor responsável: ");
                    int idInstrutor = scanner.nextInt();

                    // Validação de segurança: Antes de criar a aula, o sistema verifica se o ID do instrutor digitado existe mesmo no banco.
                    Instrutor instrutor = instrutorDAO.buscarPorId(idInstrutor);

                    if (instrutor != null) {
                        // Se o instrutor existir, vincula ele à nova aula.
                        Aula novaAula = new Aula(0, nome, descricao, capacidade, horario, duracao, instrutor);
                        aulaDAO.salvar(novaAula);
                    } else {
                        System.out.println("Erro: Instrutor não encontrado! Não foi possível criar a aula.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- MATRICULAR ALUNO EM AULA ---");
                    System.out.print("Digite o ID do Aluno: ");
                    int idAluno = scanner.nextInt();
                    System.out.print("Digite o ID da Aula Coletiva: ");
                    int idAula = scanner.nextInt();

                    // --- ATENÇÃO EQUIPE: AQUI ESTÁ O CORAÇÃO DA NOSSA AVALIAÇÃO! ---
                    // Ao invés de só socar os IDs no banco de dados, nós buscamos os dados completos do aluno e da aula.
                    Aluno alunoEncontrado = alunoDAO.buscarPorId(idAluno);
                    Aula aulaEncontrada = aulaDAO.buscarPorId(idAula);

                    // Se tanto a aula quanto o aluno existirem...
                    if (alunoEncontrado != null && aulaEncontrada != null) {
                        // Acionamos a nossa camada de Serviços (onde moram as regras de negócio complexas).
                        services.InscricaoService inscricaoService = new services.InscricaoService(aulaDAO);

                        // O matricularAluno() vai conferir os horários, limite de vagas e validade do plano
                        // antes de autorizar a gravação no banco de dados.
                        inscricaoService.matricularAluno(alunoEncontrado, aulaEncontrada);
                    } else {
                        System.out.println("Erro: Aluno ou Aula não encontrados no sistema.");
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuFrequencia(Scanner scanner, FrequenciaDAO frequenciaDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- CONTROLE DE FREQUÊNCIA ---");
            System.out.println("1. Registrar Entrada de Aluno (Catraca)");
            System.out.println("2. Consultar Total de Visitas de um Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o ID do aluno para liberar a entrada: ");
                    int idAluno = scanner.nextInt();
                    // Registra a entrada usando o momento (data e hora) em que o código foi executado
                    frequenciaDAO.registrarEntrada(idAluno);
                    break;

                case 2:
                    System.out.print("Digite o ID do aluno para checar o histórico: ");
                    int idBusca = scanner.nextInt();
                    // Conta no banco de dados quantas vezes aquele ID aparece na tabela de frequência
                    int totalVisitas = frequenciaDAO.contarVisitasAluno(idBusca);
                    System.out.println("O aluno com ID " + idBusca + " realizou " + totalVisitas + " visitas à academia.");
                    break;

                case 0:
                    System.out.println("Retornando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}