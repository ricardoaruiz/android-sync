package br.com.rar.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 1/3/17.
 */
public class AlunoConverter {

    public String convertToJson(List<Aluno> alunos) {

        JSONStringer json = new JSONStringer();

        try {
            json.object().key("list");
                json.array();
                    json.object().key("aluno");
                        json.array();
                            for(Aluno aluno : alunos) {
                                json.object();
                                    json.key("nome").value(aluno.getNome());
                                    json.key("nota").value(aluno.getNota());
                                json.endObject();
                            }
                        json.endArray();
                    json.endObject();
                json.endArray();
            json.endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

}
