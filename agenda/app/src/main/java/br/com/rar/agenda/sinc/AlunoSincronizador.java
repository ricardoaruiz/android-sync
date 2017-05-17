package br.com.rar.agenda.sinc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.rar.agenda.dao.AlunoDAO;
import br.com.rar.agenda.dto.AlunoSync;
import br.com.rar.agenda.event.AtualizaListaAlunoEvent;
import br.com.rar.agenda.preferences.AlunoPreferences;
import br.com.rar.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {

    private final Context context;

    private EventBus bus;

    private AlunoPreferences alunoPreferences;

    public AlunoSincronizador(Context context) {
        this.context = context;
        this.bus = EventBus.getDefault();
        this.alunoPreferences = new AlunoPreferences(context);
    }

    public void buscaTodos() {
        if(alunoPreferences.temVersao()) {
            buscaAlunosNovos();
        } else {
            buscaAlunos();
        }
    }

    private void buscaAlunosNovos() {
        String versao = alunoPreferences.getVersao();
        Call<AlunoSync> call = RetrofitInicializador.getInstance().getAlunoService().novos(versao);
        call.enqueue(buscaAlunosCallback());
    }

    private void buscaAlunos() {
        Call<AlunoSync> call = RetrofitInicializador.getInstance().getAlunoService().lista();
        call.enqueue(buscaAlunosCallback());
    }

    @NonNull
    private Callback<AlunoSync> buscaAlunosCallback() {
        return new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                sincronizaAlunos(response.body());
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("buscaAlunos", t.getMessage());
                bus.post(new AtualizaListaAlunoEvent());
            }
        };
    }

    private void sincronizaAlunos(AlunoSync alunoSync) {
        alunoPreferences.salvarVersao(alunoSync.getMomentoDaUltimaModificacao());

        AlunoDAO dao = new AlunoDAO(context);
        dao.sincroniza(alunoSync.getAlunos());
        dao.close();

        bus.post(new AtualizaListaAlunoEvent());
    }
}