package br.com.rar.agenda.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import br.com.rar.agenda.dao.AlunoDAO;
import br.com.rar.agenda.dto.AlunoSync;
import br.com.rar.agenda.event.AtualizaListaAlunoEvent;


public class AgendaMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        Log.i("Mensagem Recebida", "onMessageReceived: " + String.valueOf(data));

        salvaAlunoRecebidoFirebase(data);
    }

    private void salvaAlunoRecebidoFirebase(Map<String, String> data) {
        try {
            String json = data.get("alunoSync");
            ObjectMapper mapper = new ObjectMapper();
            AlunoSync alunoSync = mapper.readValue(json, AlunoSync.class);

            AlunoDAO dao = new AlunoDAO(this);
            dao.sincroniza(alunoSync.getAlunos());
            dao.close();

            Log.i("salvaAlunoRecebido", "O Aluno recebido do firebase foi salvo: " + alunoSync.getAlunos().get(0).getNome());

            EventBus.getDefault().post(new AtualizaListaAlunoEvent());

        } catch (IOException e) {
            Log.e("salvaAlunoRecebido", "Problema ao salvar aluno recebido do firebase " + e.getMessage());
        }
    }
}
