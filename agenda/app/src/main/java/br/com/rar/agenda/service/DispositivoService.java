package br.com.rar.agenda.service;

import retrofit2.Call;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ralmendro on 08/04/17.
 */
public interface DispositivoService {

    @POST("firebase/dispositivo")
    Call<Void> enviarToken(@Header("token") String token);

}
