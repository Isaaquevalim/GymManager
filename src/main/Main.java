package main;

import dao.AlunoDAO;
import dao.AulaDAO;
import dao.FrequenciaDAO;
import dao.InstrutorDAO;
import dao.PlanoDAO;
import entities.Aluno;
import entities.Aula;
import entities.Funcionario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        AlunoDAO alunoDAO = new AlunoDAO();
        PlanoDAO planoDAO = new PlanoDAO();
        InstrutorDAO instrutorDAO = new InstrutorDAO();
        FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
        AulaDAO aulaDAO = new AulaDAO();

        do {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ACADEMIA (GYM MANAGER) ===");
            System.out.println("1. Gerenciar Alunos (CRUD)");
            System.out.println("2. Gerenciar Planos");
            System.out.println("3. Gerenciar Instrutores");
            System.out.println("4. Gerenciar Aulas");
            System.out.println("5. Registrar Frequência (Entrada)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            // Tratamento preventivo caso digitem letras no menu principal
            try {
                opcaoPrincipal = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
            } catch (Exception e) {
                System.out.println("❌ Por favor, digite apenas números!");
                scanner.nextLine(); // Limpa a letra digitada para não dar loop infinito
                continue;
            }

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
                    menuAulas(scanner, aulaDAO, instrutorDAO, alunoDAO);
                    break;
                case 5:
                    menuFrequencia(scanner, frequenciaDAO, alunoDAO); // -> Adicionado alunoDAO aqui no final
                    break;
                case 0:
                    System.out.println("Encerrando o sistema... Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcaoPrincipal != 0);

        scanner.close();
    }

    private static void menuAlunos(Scanner scanner, AlunoDAO alunoDAO, PlanoDAO planoDAO) {
        int opcaoAluno = -1;

        do {
            System.out.println("\n--- GERENCIAR ALUNOS ---");
            System.out.println("1. Cadastrar Novo Aluno (Create)");
            System.out.println("2. Listar Todos os Alunos (Read)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");

            try {
                opcaoAluno = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Opção inválida!");
                scanner.nextLine();
                continue;
            }

            switch (opcaoAluno) {
                case 1:
                    System.out.print("Digite o nome do aluno: ");
                    String nome = scanner.nextLine();

                    System.out.print("Digite o CPF (Apenas números): ");
                    String cpf = scanner.nextLine();

                    // --- BLINDAGEM DA DATA DE NASCIMENTO ---
                    LocalDate dataNascimento = null;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    // Enquanto o usuário não digitar a data no formato correto com as barras, o sistema não avança
                    while (dataNascimento == null) {
                        System.out.print("Data de Nascimento (DD/MM/AAAA): ");
                        String dataNascStr = scanner.nextLine();
                        try {
                            dataNascimento = LocalDate.parse(dataNascStr, formatter);
                        } catch (DateTimeParseException e) {
                            // Captura o erro que travou o sistema antes e dá um aviso limpo
                            System.out.println("❌ Formato inválido! Você precisa digitar com as barras. Exemplo: 28/09/2006");
                        }
                    }

                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();

                    System.out.print("E-mail: ");
                    String email = scanner.nextLine();

                    // Instancia passando null no plano inicial para fins de teste
                    Aluno novoAluno = new Aluno(0, nome, cpf, dataNascimento, telefone, email, LocalDate.now(), null);
                    alunoDAO.salvar(novoAluno);
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE ALUNOS CADASTRADOS ---");
                    List<Aluno> lista = alunoDAO.listarTodos();

                    if (lista.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado no banco de dados.");
                    } else {
                        for (Aluno a : lista) {
                            // Comunicação perfeita usando o novo método idAluno
                            System.out.println("ID: " + a.getIdAluno() + " | Nome: " + a.getNome() + " | CPF: " + a.getCpf());
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
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Opção inválida!");
                scanner.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Nome do Plano (ex: Gold, Black): ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição dos benefícios: ");
                    String descricao = scanner.nextLine();

                    // --- BLINDAGEM DO VALOR MENSAL (DOUBLE) ---
                    double valor = 0.0;
                    boolean valorValido = false;
                    while (!valorValido) {
                        System.out.print("Valor Mensal (Ex: 299,90 ou 299.90): ");
                        String valorStr = scanner.nextLine();
                        try {
                            // Substitui a vírgula por ponto caso o usuário digite no padrão brasileiro
                            valorStr = valorStr.replace(",", ".");
                            valor = Double.parseDouble(valorStr);
                            valorValido = true;
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Valor inválido! Digite um número decimal válido.");
                        }
                    }

                    // --- BLINDAGEM DA DURAÇÃO (INT) ---
                    int duracao = 0;
                    boolean duracaoValida = false;
                    while (!duracaoValida) {
                        System.out.print("Duração (em meses): ");
                        try {
                            duracao = scanner.nextInt();
                            scanner.nextLine(); // Limpa o buffer
                            duracaoValida = true;
                        } catch (Exception e) {
                            System.out.println("❌ Digite um número inteiro para a quantidade de meses!");
                            scanner.nextLine(); // Limpa o buffer do erro
                        }
                    }

                    System.out.print("Benefícios inclusos adicionais: ");
                    String beneficios = scanner.nextLine();

                    Plano novoPlano = new Plano(0, nome, descricao, valor, duracao, beneficios);
                    planoDAO.salvar(novoPlano);
                    break;

                case 2:
                    System.out.print("Digite o ID do plano que deseja buscar: ");
                    try {
                        int idBusca = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        Plano plano = planoDAO.buscarPorId(idBusca);

                        if (plano != null) {
                            System.out.println("\nPlano Encontrado:");
                            System.out.println("Nome: " + plano.getNome() + " | Valor: R$ " + plano.getValorMensal() + " | Duração: " + plano.getDuracaoMeses() + " meses");
                        } else {
                            System.out.println("Nenhum plano encontrado com o ID informado.");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ ID inválido! Digite apenas números.");
                        scanner.nextLine();
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
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Opção inválida!");
                scanner.nextLine();
                continue;
            }

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

                    Funcionario novoInstrutor = new Funcionario(0, nome, cpf, telefone, especialidade, horarios);
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
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Opção inválida!");
                scanner.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Nome da Aula (ex: Crossfit, Spinning): ");
                    String nome = scanner.nextLine();
                    System.out.print("Descrição da atividade: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Capacidade Máxima de Alunos: ");
                    int capacidade = scanner.nextInt();
                    scanner.nextLine();

                    // --- BLINDAGEM DO HORÁRIO DA AULA ---
                    LocalDateTime horario = null;
                    while (horario == null) {
                        System.out.print("Horário (Formato AAAA-MM-DDTHH:MM, ex: 2026-05-20T19:00): ");
                        String horarioStr = scanner.nextLine();
                        try {
                            horario = LocalDateTime.parse(horarioStr);
                        } catch (DateTimeParseException e) {
                            System.out.println("❌ Formato de horário inválido! Siga exatamente o padrão com o 'T' separando data e hora.");
                        }
                    }

                    System.out.print("Duração (em minutos): ");
                    int duracao = scanner.nextInt();

                    System.out.print("ID do Instrutor responsável: ");
                    int idInstrutor = scanner.nextInt();
                    Funcionario instrutor = instrutorDAO.buscarPorId(idInstrutor);

                    if (instrutor != null) {
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

                    Aluno alunoEncontrado = alunoDAO.buscarPorId(idAluno);
                    Aula aulaEncontrada = aulaDAO.buscarPorId(idAula);

                    if (alunoEncontrado != null && aulaEncontrada != null) {
                        services.InscricaoService inscricaoService = new services.InscricaoService(aulaDAO);
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

    private static void menuFrequencia(Scanner scanner, FrequenciaDAO frequenciaDAO, AlunoDAO alunoDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- CONTROLE DE FREQUÊNCIA ---");
            System.out.println("1. Registrar Entrada de Aluno (Catraca)");
            System.out.println("2. Consultar Total de Visitas de um Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha: ");
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado
            } catch (Exception e) {
                System.out.println("❌ Opção inválida!");
                scanner.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Digite o ID do aluno para liberar a entrada: ");
                    try {
                        int idAluno = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer

                        // Busca o aluno no banco de dados para descobrir o nome dele
                        Aluno aluno = alunoDAO.buscarPorId(idAluno);

                        if (aluno != null) {
                            // Registra a entrada no banco de dados
                            frequenciaDAO.registrarEntrada(idAluno);
                            // Exibe a mensagem de sucesso muito mais intuitiva e personalizada!
                            System.out.println("🎉 Entrada liberada! Seja bem-vindo(a), " + aluno.getNome() + "!");
                        } else {
                            System.out.println("❌ Erro: Não existe nenhum aluno cadastrado com o ID " + idAluno);
                        }
                    } catch (Exception e) {
                        System.out.println("❌ ID inválido! Digite apenas números.");
                        scanner.nextLine();
                    }
                    break;

                case 2:
                    System.out.print("Digite o ID do aluno para checar o histórico: ");
                    try {
                        int idBusca = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer

                        // Busca o aluno no banco de dados para descobrir o nome dele
                        Aluno aluno = alunoDAO.buscarPorId(idBusca);

                        if (aluno != null) {
                            // Puxa o contador de acessos do FrequenciaDAO
                            int totalVisitas = frequenciaDAO.contarVisitasAluno(idBusca);
                            // Exibe o histórico usando o nome real do aluno
                            System.out.println("📊 O(A) aluno(a) '" + aluno.getNome() + "' realizou um total de " + totalVisitas + " visitas à academia.");
                        } else {
                            System.out.println("❌ Erro: Aluno não encontrado com o ID informado.");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ ID inválido! Digite apenas números.");
                        scanner.nextLine();
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
}