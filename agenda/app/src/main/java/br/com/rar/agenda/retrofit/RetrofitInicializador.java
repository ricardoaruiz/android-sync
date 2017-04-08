package br.com.rar.agenda.retrofit;

import br.com.rar.agenda.service.AlunoService;
import br.com.rar.agenda.service.DispositivoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ralmendro on 25/03/17.
 */

public class RetrofitInicializador {

    private static RetrofitInicializador instance;

    private Retrofit retrofit;

    private RetrofitInicializador() {
    }

    public static RetrofitInicializador getInstance() {
        if(instance == null) {
            instance = new RetrofitInicializador();
        }
        return instance;
    }

    private Retrofit getClientServidorCadastro() {
        if(retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(interceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.12:8080/api/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }

    public AlunoService getAlunoService() {
        return getClientServidorCadastro().create(AlunoService.class);
    }

    public DispositivoService getDispositivoService() {
        return getClientServidorCadastro().create(DispositivoService.class);
    }
}
