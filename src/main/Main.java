package main;

import dao.*;
import entities.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPrincipal = -1;

        // --- INSTANCIANDO TODOS OS 10 DAOs DO SISTEMA ---
        AlunoDAO alunoDAO = new AlunoDAO();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        SalaDAO salaDAO = new SalaDAO();
        MatriculaDAO matriculaDAO = new MatriculaDAO();
        AulaDAO aulaDAO = new AulaDAO();
        EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
        FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
        PagamentoDAO pagamentoDAO = new PagamentoDAO();
        ServicosExternosDAO servicosExternosDAO = new ServicosExternosDAO();

        do {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ACADEMIA (GYM MANAGER) ===");
            System.out.println("1. Gerenciar Alunos");
            System.out.println("2. Gerenciar Funcionários");
            System.out.println("3. Gerenciar Salas");
            System.out.println("4. Gerenciar Matrículas/Planos");
            System.out.println("5. Gerenciar Aulas");
            System.out.println("6. Gerenciar Equipamentos");
            System.out.println("7. Gerenciar Serviços (Painel Financeiro)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção do fluxo: ");

            try {
                opcaoPrincipal = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Por favor, digite apenas números válidos.");
                scanner.nextLine();
                continue;
            }

            switch (opcaoPrincipal) {
                case 1: menuAlunos(scanner, alunoDAO); break;
                case 2: menuFuncionarios(scanner, funcionarioDAO); break;
                case 3: menuSalas(scanner, salaDAO); break;
                case 4: menuMatriculas(scanner, matriculaDAO); break;
                case 5: menuAulas(scanner, aulaDAO); break;
                case 6: menuEquipamentos(scanner, equipamentoDAO); break;
                case 7: menuFinanceiro(scanner, pagamentoDAO, manutencaoDAO, servicosExternosDAO, funcionarioDAO); break;
                case 0: System.out.println("Encerrando o sistema. Ótimo trabalho, equipe!"); break;
                default: System.out.println("⚠️ Opção inválida!");
            }
        } while (opcaoPrincipal != 0);

        scanner.close();
    }

    // ==========================================================
    // FLUXOS 1, 2 e 3: Cadastros Base
    // ==========================================================
    private static void menuAlunos(Scanner scanner, AlunoDAO alunoDAO) {
        System.out.println("\n--- GERENCIAR ALUNOS ---");
        System.out.print("1. Cadastrar | 2. Listar | 3. Deletar | Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();
        if (op == 1) {
            System.out.print("Nome: "); String nome = scanner.nextLine();
            System.out.print("CPF: "); String cpf = scanner.nextLine();
            Aluno aluno = new Aluno(cpf, nome, LocalDate.now()); // Data simplificada para agilidade no teste
            alunoDAO.salvar(aluno);
        } else if (op == 2) {
            for (Aluno a : alunoDAO.listarTodos()) System.out.println("CPF: " + a.getCpfAluno() + " | Nome: " + a.getNomeCompleto());
        } else if (op == 3) {
            System.out.print("CPF para deletar: "); String cpf = scanner.nextLine();
            alunoDAO.deletar(cpf);
        }
    }

    private static void menuFuncionarios(Scanner scanner, FuncionarioDAO funcionarioDAO) {
        System.out.println("\n--- GERENCIAR FUNCIONÁRIOS ---");
        System.out.print("1. Cadastrar | 2. Listar | Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();
        if (op == 1) {
            System.out.print("Cargo: "); String cargo = scanner.nextLine();
            System.out.print("Salário: "); double salario = Double.parseDouble(scanner.nextLine().replace(",", "."));
            Funcionario f = new Funcionario(0, cargo, salario, "Acesso Geral", "Nenhuma");
            funcionarioDAO.salvar(f);
        } else if (op == 2) {
            for (Funcionario f : funcionarioDAO.listarTodos()) System.out.println("ID: " + f.getIdFuncionario() + " | Cargo: " + f.getCargo() + " | Salário: R$" + f.getSalario());
        }
    }

    private static void menuSalas(Scanner scanner, SalaDAO salaDAO) {
        System.out.println("\n--- GERENCIAR SALAS ---");
        System.out.print("1. Cadastrar | 2. Listar | Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();
        if (op == 1) {
            System.out.print("Número da Sala: "); int num = scanner.nextInt(); scanner.nextLine();
            System.out.print("Tipo (ex: Pilates): "); String tipo = scanner.nextLine();
            System.out.print("Capacidade: "); int cap = scanner.nextInt(); scanner.nextLine();
            salaDAO.salvar(new Sala(num, tipo, cap));
        } else if (op == 2) {
            for (Sala s : salaDAO.listarTodas()) System.out.println("Sala " + s.getNumeroSala() + " | " + s.getTipoSala() + " | Vagas: " + s.getCapacidadeMaxima());
        }
    }

    // ==========================================================
    // FLUXO 4: Matrículas e Planos
    // ==========================================================
    private static void menuMatriculas(Scanner scanner, MatriculaDAO matriculaDAO) {
        System.out.println("\n--- GERENCIAR MATRÍCULAS ---");
        System.out.println("1. Nova Matrícula/Plano");
        System.out.print("Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();

        if (op == 1) {
            System.out.print("CPF do Aluno existente: ");
            String cpf = scanner.nextLine();
            System.out.print("Tipo do Plano (ex: Black, Mensal): ");
            String tipo = scanner.nextLine();
            System.out.print("Valor do Plano: R$ ");
            double valor = Double.parseDouble(scanner.nextLine().replace(",", "."));
            System.out.print("Permissões (ex: Acesso Total): ");
            String permissoes = scanner.nextLine();

            // Instancia um aluno apenas com o CPF para servir de Chave Estrangeira no BD
            Aluno alunoRef = new Aluno(cpf, null, null);
            Matricula m = new Matricula(0, alunoRef, tipo, valor, LocalDate.now(), LocalDate.now().plusMonths(1), permissoes);
            matriculaDAO.salvar(m);
        }
    }

    // ==========================================================
    // FLUXO 5: Grade de Aulas
    // ==========================================================
    private static void menuAulas(Scanner scanner, AulaDAO aulaDAO) {
        System.out.println("\n--- GERENCIAR AULAS ---");
        System.out.println("1. Agendar Nova Aula");
        System.out.print("Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();

        if (op == 1) {
            System.out.print("Número da Sala (Existente): ");
            int idSala = scanner.nextInt();
            System.out.print("ID do Funcionário/Instrutor (Existente): ");
            int idFunc = scanner.nextInt(); scanner.nextLine();
            System.out.print("Tipo de Aula (ex: Spinning): ");
            String tipo = scanner.nextLine();

            System.out.print("Horário da Aula (HH:MM, ex: 19:30): ");
            LocalTime horario = LocalTime.parse(scanner.nextLine() + ":00"); // Adiciona segundos para o padrão SQL

            Sala salaRef = new Sala(idSala, null, 0);
            Funcionario funcRef = new Funcionario(idFunc, null, 0, null, null);
            Aula novaAula = new Aula(0, salaRef, funcRef, tipo, LocalDate.now(), horario);

            aulaDAO.salvar(novaAula);
        }
    }

    // ==========================================================
    // FLUXO 6: Inventário de Equipamentos
    // ==========================================================
    private static void menuEquipamentos(Scanner scanner, EquipamentoDAO equipamentoDAO) {
        System.out.println("\n--- GERENCIAR EQUIPAMENTOS ---");
        System.out.println("1. Cadastrar Máquina");
        System.out.println("2. Alterar Status (Ex: Enviar para Manutenção)");
        System.out.print("Escolha: ");
        int op = scanner.nextInt(); scanner.nextLine();

        if (op == 1) {
            System.out.print("Número da Sala onde ficará: ");
            int idSala = scanner.nextInt(); scanner.nextLine();
            System.out.print("Tipo de Equipamento (ex: Esteira, Halter): ");
            String tipo = scanner.nextLine();
            System.out.print("Quantidade: ");
            int qtd = scanner.nextInt(); scanner.nextLine();

            Sala salaRef = new Sala(idSala, null, 0);
            Equipamento eq = new Equipamento(0, salaRef, tipo, qtd, "Ativo");
            equipamentoDAO.salvar(eq);
        } else if (op == 2) {
            System.out.print("ID do Equipamento: ");
            int idEq = scanner.nextInt(); scanner.nextLine();
            System.out.print("Novo Status (Ativo / Em Manutenção / Quebrado): ");
            String status = scanner.nextLine();
            equipamentoDAO.atualizarStatus(idEq, status);
        }
    }

    // ==========================================================
    // FLUXO 7: O Painel Financeiro e Custos Operacionais
    // ==========================================================
    private static void menuFinanceiro(Scanner scanner, PagamentoDAO pagDAO, ManutencaoDAO manDAO, ServicosExternosDAO servDAO, FuncionarioDAO funcDAO) {
        int op = -1;
        do {
            System.out.println("\n--- GERENCIAR SERVIÇOS E FINANCEIRO ---");
            System.out.println("1. Registrar Entrada (Pagamento de Mensalidade)");
            System.out.println("2. Registrar Despesa (Serviço Externo/Contas)");
            System.out.println("3. Registrar Custo de Manutenção");
            System.out.println("4. 📊 Gerar Painel de Relatório Financeiro");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            op = scanner.nextInt(); scanner.nextLine();

            switch (op) {
                case 1:
                    System.out.print("ID da Matrícula vinculada: ");
                    int idMat = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Valor Recebido: R$ ");
                    double valorPago = Double.parseDouble(scanner.nextLine().replace(",", "."));

                    Matricula mRef = new Matricula(idMat, null, null, 0, null, null, null);
                    pagDAO.salvar(new Pagamento(0, mRef, LocalDate.now(), valorPago, "Pago"));
                    break;

                case 2:
                    System.out.print("Descrição da Despesa (ex: Conta de Luz, Empresa de Limpeza): ");
                    String desc = scanner.nextLine();
                    System.out.print("Valor do Serviço: R$ ");
                    double valorServ = Double.parseDouble(scanner.nextLine().replace(",", "."));

                    servDAO.salvar(new ServicosExternos(0, null, null, desc, valorServ, LocalDate.now(), "Pago"));
                    break;

                case 3:
                    System.out.print("ID do Equipamento Consertado: ");
                    int idEq = scanner.nextInt(); scanner.nextLine();
                    System.out.print("Custo do Conserto: R$ ");
                    double custoManut = Double.parseDouble(scanner.nextLine().replace(",", "."));

                    Equipamento eqRef = new Equipamento(idEq, null, null, 0, null);
                    manDAO.salvar(new Manutencao(0, eqRef, LocalDate.now(), custoManut));
                    break;

                case 4:
                    // A MÁGICA ACONTECE AQUI: Reunindo os dados de todas as tabelas
                    System.out.println("\n=================================================");
                    System.out.println("      📊 PAINEL FINANCEIRO GERAL (DASHBOARD)     ");
                    System.out.println("=================================================");

                    double receitas = pagDAO.somarReceitasTotais();
                    double custosManutencao = manDAO.somarCustosManutencao();
                    double despesasExternas = servDAO.somarDespesasExternas();

                    // Calculando a folha salarial da equipe via Java
                    double salarios = 0;
                    for (Funcionario f : funcDAO.listarTodos()) {
                        salarios += f.getSalario();
                    }

                    double totalDespesas = custosManutencao + despesasExternas + salarios;
                    double saldo = receitas - totalDespesas;

                    System.out.println("💰 RECEITAS:");
                    System.out.println("   + Mensalidades Pagas:           R$ " + String.format("%.2f", receitas));

                    System.out.println("\n💸 DESPESAS:");
                    System.out.println("   - Folha Salarial (Funcionários):R$ " + String.format("%.2f", salarios));
                    System.out.println("   - Manutenção de Equipamentos:   R$ " + String.format("%.2f", custosManutencao));
                    System.out.println("   - Serviços Externos / Contas:   R$ " + String.format("%.2f", despesasExternas));
                    System.out.println("   ----------------------------------------------");
                    System.out.println("   TOTAL DE CUSTOS OPERACIONAIS:   R$ " + String.format("%.2f", totalDespesas));

                    System.out.println("\n📈 RESULTADO FINAL (LUCRO/PREJUÍZO): R$ " + String.format("%.2f", saldo));
                    System.out.println("=================================================");
                    break;
            }
        } while (op != 0);
    }
}