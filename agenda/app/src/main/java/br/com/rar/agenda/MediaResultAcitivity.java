package br.com.rar.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.rar.agenda.client.response.MediaResponse;

public class MediaResultAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_result_acitivity);

        MediaResponse result = (MediaResponse) getIntent().getSerializableExtra("result");

        TextView campoMedia = (TextView) findViewById(R.id.media_result_value_media);
        campoMedia.setText(result.getMedia());

        TextView campoQtAlunos = (TextView) findViewById(R.id.media_result_value_qtAlunos);
        campoQtAlunos.setText(result.getQuantidade());
    }
}
