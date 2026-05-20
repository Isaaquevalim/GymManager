package main;

import dao.AlunoDAO;
import dao.FuncionarioDAO;
import dao.SalaDAO;
import entities.Aluno;
import entities.Funcionario;
import entities.Sala;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        // Instanciando os DAOs que fazem a ponte com o PostgreSQL
        AlunoDAO alunoDAO = new AlunoDAO();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        SalaDAO salaDAO = new SalaDAO();

        do {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ACADEMIA ===");
            System.out.println("1. Gerenciar Alunos");
            System.out.println("2. Gerenciar Funcionários");
            System.out.println("3. Gerenciar Salas");
            System.out.println("4. Gerenciar Matrículas/Planos");
            System.out.println("5. Gerenciar Aulas");
            System.out.println("6. Gerenciar Equipamentos");
            System.out.println("7. Gerenciar Serviços (Financeiro)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção do fluxo: ");

            try {
                opcaoPrincipal = scanner.nextInt();
                scanner.nextLine(); // Limpeza de buffer
            } catch (Exception e) {
                System.out.println("❌ Por favor, digite apenas números válidos.");
                scanner.nextLine();
                continue;
            }

            switch (opcaoPrincipal) {
                case 1:
                    menuAlunos(scanner, alunoDAO);
                    break;
                case 2:
                    menuFuncionarios(scanner, funcionarioDAO);
                    break;
                case 3:
                    menuSalas(scanner, salaDAO);
                    break;
                case 4:
                    System.out.println("⏳ Em construção: CRUD de Matrículas e Planos.");
                    break;
                case 5:
                    System.out.println("⏳ Em construção: CRUD da Grade Horária de Aulas.");
                    break;
                case 6:
                    System.out.println("⏳ Em construção: Controle de Inventário e Equipamentos.");
                    break;
                case 7:
                    System.out.println("⏳ Em construção: Painel Financeiro e Custos Operacionais.");
                    break;
                case 0:
                    System.out.println("Encerrando o sistema. Ótimo trabalho, equipe!");
                    break;
                default:
                    System.out.println("⚠️ Opção inválida!");
            }
        } while (opcaoPrincipal != 0);

        scanner.close();
    }

    // --- FLUXO 1: CRUD DE ALUNOS ---
    private static void menuAlunos(Scanner scanner, AlunoDAO alunoDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR ALUNOS ---");
            System.out.println("1. Cadastrar Aluno (Create)");
            System.out.println("2. Listar Alunos (Read)");
            System.out.println("3. Atualizar Aluno (Update)");
            System.out.println("4. Remover Aluno (Delete)");
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
                    System.out.print("Nome Completo: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF (Apenas números, até 14 caracteres): ");
                    String cpf = scanner.nextLine();

                    // Blindagem de conversão de data
                    LocalDate dataNascimento = null;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    while (dataNascimento == null) {
                        System.out.print("Data de Nascimento (DD/MM/AAAA): ");
                        try {
                            dataNascimento = LocalDate.parse(scanner.nextLine(), formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("❌ Formato inválido! Utilize barras (ex: 28/09/2006).");
                        }
                    }

                    Aluno novoAluno = new Aluno(cpf, nome, dataNascimento);
                    alunoDAO.salvar(novoAluno);
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE ALUNOS ---");
                    List<Aluno> alunos = alunoDAO.listarTodos();
                    if (alunos.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado.");
                    } else {
                        for (Aluno a : alunos) {
                            System.out.println("CPF: " + a.getCpfAluno() + " | Nome: " + a.getNomeCompleto() + " | Nasc: " + a.getDataNascimento());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Digite o CPF do aluno que deseja atualizar: ");
                    String cpfAtualizar = scanner.nextLine();
                    System.out.print("Novo Nome Completo: ");
                    String novoNome = scanner.nextLine();

                    LocalDate novaData = null;
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    while (novaData == null) {
                        System.out.print("Nova Data de Nascimento (DD/MM/AAAA): ");
                        try {
                            novaData = LocalDate.parse(scanner.nextLine(), fmt);
                        } catch (DateTimeParseException e) {
                            System.out.println("❌ Formato inválido!");
                        }
                    }

                    Aluno alunoAtualizado = new Aluno(cpfAtualizar, novoNome, novaData);
                    alunoDAO.atualizar(alunoAtualizado);
                    break;

                case 4:
                    System.out.print("Digite o CPF do aluno a ser removido: ");
                    String cpfRemover = scanner.nextLine();
                    alunoDAO.deletar(cpfRemover);
                    break;

                case 0: break;
                default: System.out.println("⚠️ Opção inválida!");
            }
        } while (opcao != 0);
    }

    // --- FLUXO 2: CRUD DE FUNCIONÁRIOS ---
    private static void menuFuncionarios(Scanner scanner, FuncionarioDAO funcionarioDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR FUNCIONÁRIOS ---");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Listar Funcionários");
            System.out.println("3. Desligar Funcionário");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try { opcao = scanner.nextInt(); scanner.nextLine(); }
            catch (Exception e) { scanner.nextLine(); continue; }

            switch (opcao) {
                case 1:
                    System.out.print("Cargo (ex: Instrutor, Limpeza): ");
                    String cargo = scanner.nextLine();
                    System.out.print("Salário: ");
                    double salario = Double.parseDouble(scanner.nextLine().replace(",", "."));
                    System.out.print("Pontos de Acesso (ex: Todas as salas): ");
                    String pontos = scanner.nextLine();
                    System.out.print("Delegações (ex: Musculação período noturno): ");
                    String delegacoes = scanner.nextLine();

                    Funcionario f = new Funcionario(0, cargo, salario, pontos, delegacoes);
                    funcionarioDAO.salvar(f);
                    break;
                case 2:
                    System.out.println("\n--- LISTA DE FUNCIONÁRIOS ---");
                    for (Funcionario func : funcionarioDAO.listarTodos()) {
                        System.out.println("ID: " + func.getIdFuncionario() + " | Cargo: " + func.getCargo() + " | Salário: R$" + func.getSalario());
                    }
                    break;
                case 3:
                    System.out.print("Digite o ID do funcionário a ser desligado: ");
                    int idRemover = scanner.nextInt();
                    funcionarioDAO.deletar(idRemover);
                    break;
            }
        } while (opcao != 0);
    }

    // --- FLUXO 3: CRUD DE SALAS ---
    private static void menuSalas(Scanner scanner, SalaDAO salaDAO) {
        int opcao = -1;
        do {
            System.out.println("\n--- GERENCIAR SALAS ---");
            System.out.println("1. Registrar Nova Sala");
            System.out.println("2. Listar Salas Existentes");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            try { opcao = scanner.nextInt(); scanner.nextLine(); }
            catch (Exception e) { scanner.nextLine(); continue; }

            switch (opcao) {
                case 1:
                    System.out.print("Número da Sala: ");
                    int numero = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Tipo da Sala (ex: Pilates, Dança): ");
                    String tipo = scanner.nextLine();
                    System.out.print("Capacidade Máxima: ");
                    int cap = scanner.nextInt(); scanner.nextLine();

                    Sala sala = new Sala(numero, tipo, cap);
                    salaDAO.salvar(sala);
                    break;
                case 2:
                    System.out.println("\n--- LISTA DE SALAS ---");
                    for (Sala s : salaDAO.listarTodas()) {
                        System.out.println("Sala " + s.getNumeroSala() + " | Tipo: " + s.getTipoSala() + " | Capacidade: " + s.getCapacidadeMaxima() + " pessoas");
                    }
                    break;
            }
        } while (opcao != 0);
    }
}