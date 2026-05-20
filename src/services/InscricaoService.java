package services;

import dao.AulaDAO;
import entities.Aluno;
import entities.Aula;
import java.time.LocalDate;

public class InscricaoService {

    private AulaDAO aulaDAO;

    public InscricaoService(AulaDAO aulaDAO) {
        this.aulaDAO = aulaDAO;
    }

    public void matricularAluno(Aluno aluno, Aula aula) {
        System.out.println("\n⏳ Iniciando validações de matrícula...");

        if (!aluno.isPlanoAtivo()) {
            LocalDate vencimento = aluno.calcularVencimentoPlano();
            System.out.println("❌ Erro: Inscrição negada. O plano do aluno está vencido desde: " + vencimento);
            return;
        }
        System.out.println("✅ Plano ativo validado.");

        // CORREÇÃO: Usando getIdAula() para comunicar perfeitamente com a Entidade e o DAO
        int totalInscritos = aulaDAO.contarAlunosInscritos(aula.getIdAula());

        if (totalInscritos >= aula.getCapacidadeMaxima()) {
            System.out.println("❌ Erro: Inscrição negada. A aula atingiu o limite de " + aula.getCapacidadeMaxima() + " vagas.");
            return;
        }
        System.out.println("✅ Vagas disponíveis validadas (" + totalInscritos + "/" + aula.getCapacidadeMaxima() + ").");

        // CORREÇÃO: Usando getIdAluno()
        boolean possuiConflito = aulaDAO.verificarConflitoHorario(aluno.getIdAluno(), aula.getHorario());

        if (possuiConflito) {
            System.out.println("❌ Erro: Inscrição negada. O aluno já possui outra aula marcada para o horário " + aula.getHorario());
            return;
        }
        System.out.println("✅ Checagem de conflito de horários validada.");

        // CORREÇÃO: Passando os novos getters para efetivar a inscrição no banco de dados
        aulaDAO.inscreverAluno(aluno.getIdAluno(), aula.getIdAula());
        System.out.println("🎉 SUCESSO! Aluno '" + aluno.getNome() + "' matriculado na aula '" + aula.getNome() + "'.");
    }
}