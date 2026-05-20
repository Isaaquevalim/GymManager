package services;

import dao.AulaDAO;
import entities.Aluno;
import entities.Aula;
import java.time.LocalDate;

public class InscricaoService {

    private AulaDAO aulaDAO;

    // Injeção de dependência: O serviço precisa do DAO para consultar o banco de dados
    public InscricaoService(AulaDAO aulaDAO) {
        this.aulaDAO = aulaDAO;
    }

    // Método principal que concentra a Regra de Negócio Complexa
    public void matricularAluno(Aluno aluno, Aula aula) {

        System.out.println("\n⏳ Iniciando validações de matrícula...");

        // 1. Verificar se o plano do aluno está ativo (não vencido) [cite: 36]
        if (!aluno.isPlanoAtivo()) {
            // Se o plano estiver vencido, não permite a inscrição e informa a data [cite: 38]
            LocalDate vencimento = aluno.calcularVencimentoPlano(); // Calcula a data baseada na duração [cite: 37]
            System.out.println("❌ Erro: Inscrição negada. O plano do aluno está vencido desde: " + vencimento);
            return; // Interrompe o processo
        }
        System.out.println("✅ Plano ativo validado.");

        // 2. Verificar se a aula não atingiu a capacidade máxima [cite: 39]
        // Conta quantos alunos já estão inscritos acessando o banco de dados [cite: 40]
        int totalInscritos = aulaDAO.contarAlunosInscritos(aula.getId());

        if (totalInscritos >= aula.getCapacidadeMaxima()) {
            // Se atingiu o limite, não permite a inscrição e informa a capacidade atual [cite: 41]
            System.out.println("❌ Erro: Inscrição negada. A aula atingiu o limite máximo de " + aula.getCapacidadeMaxima() + " vagas.");
            return;
        }
        System.out.println("✅ Vagas disponíveis validadas (" + totalInscritos + "/" + aula.getCapacidadeMaxima() + ").");

        // 3. Verificar se o aluno não está inscrito em outra aula no mesmo horário [cite: 42]
        // Busca as aulas do aluno e compara os horários [cite: 43]
        boolean possuiConflito = aulaDAO.verificarConflitoHorario(aluno.getId(), aula.getHorario());

        if (possuiConflito) {
            // Se houver conflito, não permite a inscrição [cite: 44]
            System.out.println("❌ Erro: Inscrição negada. O aluno já possui outra aula marcada para o horário " + aula.getHorario());
            return;
        }
        System.out.println("✅ Checagem de conflito de horários validada.");

        // 4. Se todas as condições forem atendidas, confirmar a inscrição [cite: 45]
        aulaDAO.inscreverAluno(aluno.getId(), aula.getId());
        System.out.println("🎉 SUCESSO! Aluno '" + aluno.getNome() + "' matriculado na aula '" + aula.getNome() + "'.");
    }
}