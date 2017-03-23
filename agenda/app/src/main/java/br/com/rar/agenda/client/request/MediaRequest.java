package br.com.rar.agenda.client.request;

import java.io.Serializable;
import java.util.List;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 1/3/17.
 */

public class MediaRequest implements Serializable {

    private static final long serialVersionUID = -1349103178858430956L;

    private String teste;

    private List<MediaRequestAluno> list;

    public MediaRequest(List<MediaRequestAluno> list) {
        this.list = list;
        this.teste = "ABC";
    }
}
