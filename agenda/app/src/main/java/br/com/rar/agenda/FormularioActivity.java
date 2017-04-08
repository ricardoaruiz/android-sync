package br.com.rar.agenda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import br.com.rar.agenda.dao.AlunoDAO;
import br.com.rar.agenda.modelo.Aluno;
import br.com.rar.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    public static final int RETORNO_CAMERA = 567;

    private FormularioHelper formularioHelper;

    private String caminhoFoto;
    private ProgressDialog processando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        formularioHelper = new FormularioHelper(this);

        //Botão camera
        Button btnCamera = (Button) findViewById(R.id.formulario_btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(intentCamera, RETORNO_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RETORNO_CAMERA:
                if(resultCode == Activity.RESULT_OK) {
                    formularioHelper.carregaImagem(this.caminhoFoto);
                }
                break;
        }

    }

    //Sobrescreve o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Adiciona comportamento aos itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok :

                Aluno aluno = formularioHelper.getAluno();

                AlunoDAO alunoDAO = new AlunoDAO(FormularioActivity.this);

                if(aluno.getId() == null) {
                    alunoDAO.insere(aluno);
                } else {
                    alunoDAO.altera(aluno);
                }
                alunoDAO.close();

                enviarParaServidor(aluno);

                Toast.makeText(FormularioActivity.this, aluno.getNome() + " salvo ", Toast.LENGTH_SHORT).show();
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enviarParaServidor(Aluno aluno) {

        processando = ProgressDialog.show(this,"Processando...","Enviando Aluno ao servidor...", false);

        Call<Void> insereAlunoCall = RetrofitInicializador.getInstance().getAlunoService().insere(aluno);
        insereAlunoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("onResponse", "requisição com sucesso");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("onFaliure", "requisição falhou");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(processando != null && processando.isShowing()) {
            processando.dismiss();
        }
    }

}
