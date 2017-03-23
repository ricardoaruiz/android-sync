package br.com.rar.agenda.task;

import android.os.AsyncTask;

import br.com.rar.agenda.client.WebClient;
import br.com.rar.agenda.converter.AlunoConverter;
import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 22/03/17.
 */
public class InsereAlunoTask extends AsyncTask<Object, Void, Void>{

    private Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }


    @Override
    protected Void doInBackground(Object... params) {
        String json = new AlunoConverter().converteParaJsonCompleto(aluno);
        new WebClient().insere(json);
        return null;
    }
}

