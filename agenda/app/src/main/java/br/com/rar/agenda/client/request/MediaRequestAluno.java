package br.com.rar.agenda.client.request;

import java.io.Serializable;
import java.util.List;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 1/3/17.
 */
public class MediaRequestAluno implements Serializable {

    private static final long serialVersionUID = -2129764047344976232L;

    private List<Aluno> aluno;

    public MediaRequestAluno(List<Aluno> aluno) {
        this.aluno = aluno;
    }
}
