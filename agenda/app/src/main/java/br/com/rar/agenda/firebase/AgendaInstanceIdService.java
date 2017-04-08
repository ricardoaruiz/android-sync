package br.com.rar.agenda.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.rar.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ralmendro on 08/04/17.
 */

public class AgendaInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String refreshedToken) {
        Call<Void> dispositivoCall = RetrofitInicializador.getInstance().getDispositivoService().enviarToken(refreshedToken);
        dispositivoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("onResponse", "Token do dispositivo enviado: " + refreshedToken);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("onFailure", "Erro ao enviar o token do dispositivo: " + t.getMessage());
            }
        });
    }

}
