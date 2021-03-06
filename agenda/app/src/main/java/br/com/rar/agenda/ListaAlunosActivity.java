package br.com.rar.agenda;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import br.com.rar.agenda.adapter.AlunoAdapter;
import br.com.rar.agenda.dao.AlunoDAO;
import br.com.rar.agenda.event.AtualizaListaAlunoEvent;
import br.com.rar.agenda.modelo.Aluno;
import br.com.rar.agenda.retrofit.RetrofitInicializador;
import br.com.rar.agenda.sinc.AlunoSincronizador;
import br.com.rar.agenda.task.EnviaAlunosTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {

    public static final int CALL_PHONE_RETURN_CODE = 1;
    private static final int RECEIVE_SMS_RETURN_CODE = 2;
    private static final int INTERNET_RETURN_CODE = 3;
    private final AlunoSincronizador alunosSincronizador = new AlunoSincronizador(this);

    private ListView listAlunos;

    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        setBtnNovoAlunoClick();

        verifyAndRequestPermissions();

        listAlunos = (ListView) findViewById(R.id.lista_alunos);
        registerForContextMenu(listAlunos);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_lista_alunos);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alunosSincronizador.buscaTodos();
                alunosSincronizador.sincronizaAlunosInternos();
            }
        });

        setListAlunoItemClick();
        alunosSincronizador.buscaTodos();
        alunosSincronizador.sincronizaAlunosInternos();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AtualizaListaAlunoEvent event) {
        if(swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
       carregaLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void pararProcessando(ProgressDialog processando) {
        pararProcessando(true, processando);
    }

    private void pararProcessando(boolean showProgress, ProgressDialog processando) {
        if(showProgress && processando != null) {
            processando.dismiss();
        }
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
                Intent irParaFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
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
                Intent intentVaiParaFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiParaFormulario);
            }
        });
    }

    private void carregaLista() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();

        for(Aluno aluno : alunos) {
            Log.i("id do aluno", "Id do aluno: "  + aluno.getId() + " sincronizado: " + aluno.getSincronizado());
        }

        AlunoAdapter adapter = new AlunoAdapter(this, alunos);

        listAlunos.setAdapter(adapter);
    }

    private void createMenuRemover(ContextMenu menu, final Aluno aluno) {
        //Menu Remover
        MenuItem menuRemover = menu.add("Remover");
        menuRemover.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final ProgressDialog processando = ProgressDialog.show(ListaAlunosActivity.this,"Processando...","Removendo aluno...", false);

                Call<Void> callRemover = RetrofitInicializador.getInstance().getAlunoService().remover(aluno.getId());
                callRemover.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        AlunoDAO alunoDAO = new AlunoDAO(ListaAlunosActivity.this);
                        alunoDAO.remover(aluno);
                        alunoDAO.close();
                        carregaLista();
                        pararProcessando(processando);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ListaAlunosActivity.this, "Ocorreu um problema ao remover o aluno.", Toast.LENGTH_SHORT).show();
                        pararProcessando(processando);
                    }
                });

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
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[] {Manifest.permission.CALL_PHONE}, CALL_PHONE_RETURN_CODE);
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
