package br.com.rar.agenda.dto;

import java.util.List;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 03/04/17.
 */

public class AlunoSync {

    private List<Aluno> alunos;

    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }
}
