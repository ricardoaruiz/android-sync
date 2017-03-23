package br.com.rar.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.File;
import java.io.Serializable;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 12/22/16.
 */

public class FormularioHelper {

    private FormularioActivity activity;

    private Aluno aluno;

    private EditText campoNome;

    private EditText campoEndereco;

    private EditText campoTelefone;

    private EditText campoSite;

    private RatingBar campoNota;

    private ImageView campoFoto;

    public FormularioHelper(FormularioActivity activity) {

        this.activity = activity;

        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        campoFoto = (ImageView) activity.findViewById(R.id.formulario_imagem);

        //Pega o aluno vindo da tela de listagem
        aluno = (Aluno) this.activity.getIntent().getSerializableExtra("aluno");

        if(aluno == null) {
            aluno = new Aluno();
        } else {
            campoNome.setText(aluno.getNome());
            campoEndereco.setText(aluno.getEndereco());
            campoTelefone.setText(aluno.getTelefone());
            campoSite.setText(aluno.getSite());
            campoNota.setProgress(aluno.getNota().intValue());
            carregaImagem(aluno.getFoto());
        }

    }

    public Aluno getAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        if(campoFoto.getTag() != null) {
            aluno.setFoto((String) campoFoto.getTag());
        }

        return aluno;
    }

    public void carregaImagem(String caminhoFoto) {
        if(caminhoFoto != null && new File(caminhoFoto).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 200, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setTag(caminhoFoto);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}