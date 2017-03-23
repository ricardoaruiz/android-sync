package br.com.rar.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.rar.agenda.adapter.AlunoAdapter;
import br.com.rar.agenda.client.WebClient;
import br.com.rar.agenda.converter.AlunoConverter;
import br.com.rar.agenda.dao.AlunoDAO;
import br.com.rar.agenda.modelo.Aluno;
import br.com.rar.agenda.task.EnviaAlunosTask;

public class ListaContatos extends AppCompatActivity {

    public static final int CALL_PHONE_RETURN_CODE = 1;
    private static final int RECEIVE_SMS_RETURN_CODE = 2;
    private static final int INTERNET_RETURN_CODE = 3;

    private ListView listAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        verifyAndRequestPermissions();

        listAlunos = (ListView) findViewById(R.id.lista_alunos);
        registerForContextMenu(listAlunos);

        setListAlunoItemClick();
        setBtnNovoAlunoClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo adapterMenu = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listAlunos.getItemAtPosition(adapterMenu.position);
        createMenuLigar(menu, aluno);
        createMenuSMS(menu, aluno);
        createVisualizarNoMapa(menu, aluno);
        createMenuVisitarSite(menu, aluno);
        createMenuRemover(menu, aluno);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_lista_enviar_notas:
                new EnviaAlunosTask(this).execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void verifyAndRequestPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_RETURN_CODE);
            }
        }
    }

    private void setListAlunoItemClick() {
        listAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) lista.getItemAtPosition(position);
                Intent irParaFormulario = new Intent(ListaContatos.this, FormularioActivity.class);
                irParaFormulario.putExtra("aluno", aluno);
                startActivity(irParaFormulario);
            }
        });
    }

    private void setBtnNovoAlunoClick() {
        Button btnNovoAluno = (Button) findViewById(R.id.lista_aluno_btnNovoAluno);
        btnNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVaiParaFormulario = new Intent(ListaContatos.this, FormularioActivity.class);
                startActivity(intentVaiParaFormulario);
            }
        });
    }

    private void carregaLista() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();

        AlunoAdapter adapter = new AlunoAdapter(this, alunos);

        listAlunos.setAdapter(adapter);
    }

    private void createMenuRemover(ContextMenu menu, final Aluno aluno) {
        //Menu Remover
        MenuItem menuRemover = menu.add("Remover");
        menuRemover.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlunoDAO alunoDAO = new AlunoDAO(ListaContatos.this);
                alunoDAO.remover(aluno);
                alunoDAO.close();

                carregaLista();

                return false;
            }
        });
    }

    private void createMenuVisitarSite(ContextMenu menu, Aluno aluno) {
        //Menu site
        MenuItem menuSite = menu.add("Visitar site");

        String site = aluno.getSite();
        if(!site.startsWith("http://")) {
            site = "http://" + site;
        }

        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse(site));
        menuSite.setIntent(intentSite);
    }

    private void createVisualizarNoMapa(ContextMenu menu, Aluno aluno) {
        //Menu Ver no Mapa
        MenuItem menuVerNoMapa = menu.add("Visualizar no mapa");
        Intent intentVerNoMapa = new Intent(Intent.ACTION_VIEW);
        intentVerNoMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        menuVerNoMapa.setIntent(intentVerNoMapa);
    }

    private void createMenuSMS(ContextMenu menu, Aluno aluno) {
        //Menu SMS
        MenuItem menuSms = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        menuSms.setIntent(intentSMS);
    }

    private void createMenuLigar(ContextMenu menu, final Aluno aluno) {
        //Menu ligar
        MenuItem menuLigar = menu.add("Ligar");
        menuLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(ListaContatos.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaContatos.this, new String[] {Manifest.permission.CALL_PHONE}, CALL_PHONE_RETURN_CODE);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });
    }
}
