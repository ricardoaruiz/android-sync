package br.com.rar.agenda.service;

import java.util.List;

import br.com.rar.agenda.dto.AlunoSync;
import br.com.rar.agenda.modelo.Aluno;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ralmendro on 25/03/17.
 */

public interface AlunoService {

    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();

}
