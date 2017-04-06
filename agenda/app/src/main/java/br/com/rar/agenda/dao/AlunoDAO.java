package br.com.rar.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 12/23/16.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "agendadb", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableAlunos = "CREATE TABLE Alunos" +
                "(id CHAR(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "foto TEXT);";
        db.execSQL(sqlCreateTableAlunos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion){
            case 2:
                sql = "ALTER TABLE Alunos ADD COLUMN foto TEXT;";
                db.execSQL(sql);
            case 3:
                sql = "CREATE TABLE Alunos_novo " +
                        "(id CHAR(36) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT, " +
                        "nota REAL, " +
                        "foto TEXT);";
                db.execSQL(sql);

                sql = "INSERT INTO Alunos_novo " +
                        "(id, nome, endereco, telefone, site, nota, foto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, foto " +
                        "FROM Alunos";
                db.execSQL(sql);

                sql = "DROP TABLE Alunos";
                db.execSQL(sql);

                sql = "ALTER TABLE Alunos_novo " +
                        "RENAME TO Alunos";
                db.execSQL(sql);
            case 4:
                String sqlSelectAlunos = "SELECT * FROM Alunos";
                Cursor cursorAlunos = db.rawQuery(sqlSelectAlunos, null);
                List<Aluno> listaAlunos = populaAlunos(cursorAlunos);

                for(Aluno aluno : listaAlunos) {
                    ContentValues valoresAlterados = new ContentValues();
                    valoresAlterados.put("id", UUID.randomUUID().toString());

                    db.update("Alunos",valoresAlterados, "id = ?", new String[]{aluno.getId()});
                }
        }

    }

    public long insere(Aluno aluno) {
        //Obtem uma referência para o Banco de dados
        SQLiteDatabase db = this.getWritableDatabase();

        //Seta o UUID para Alunos
        setAlunoUUID(aluno);

        //Cria uma mapeamento para realizar a instrução insert
        ContentValues dados = getDadosAluno(aluno);

        return db.insert("Alunos", null, dados);
    }

    private void setAlunoUUID(Aluno aluno) {
        if(aluno.getId() == null) {
            aluno.setId(UUID.randomUUID().toString());
        }
    }

    public List<Aluno> buscaAlunos() {

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlSelectAlunos = "SELECT * FROM Alunos";
        Cursor cursorAlunos = db.rawQuery(sqlSelectAlunos, null);

        List<Aluno> listaAlunos = populaAlunos(cursorAlunos);

        cursorAlunos.close();

        return listaAlunos;
    }

    @NonNull
    private List<Aluno> populaAlunos(Cursor cursorAlunos) {
        List<Aluno> listaAlunos = new ArrayList<Aluno>();

        while (cursorAlunos.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursorAlunos.getString(cursorAlunos.getColumnIndex("id")));
            aluno.setNome(cursorAlunos.getString(cursorAlunos.getColumnIndex("nome")));
            aluno.setEndereco(cursorAlunos.getString(cursorAlunos.getColumnIndex("endereco")));
            aluno.setTelefone(cursorAlunos.getString(cursorAlunos.getColumnIndex("telefone")));
            aluno.setSite(cursorAlunos.getString(cursorAlunos.getColumnIndex("site")));
            aluno.setNota(cursorAlunos.getDouble(cursorAlunos.getColumnIndex("nota")));
            aluno.setFoto(cursorAlunos.getString(cursorAlunos.getColumnIndex("foto")));
            listaAlunos.add(aluno);
        }
        return listaAlunos;
    }

    public void remover(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos","id = ?", params);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getDadosAluno(aluno);
        String[] whereArgs = {aluno.getId().toString()};
        db.update("Alunos",dados,"id = ?", whereArgs);
    }

    public boolean isAluno(String telefone) {
        String sql = "SELECT * FROM Alunos WHERE telefone = ?";
        Cursor c = getReadableDatabase().rawQuery(sql, new String[]{telefone});
        int count = c.getCount();
        c.close();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(count);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return count > 0;
    }

    @NonNull
    private ContentValues getDadosAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("id", aluno.getId());
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("foto", aluno.getFoto());
        return dados;
    }

    public void sincroniza(List<Aluno> alunos) {
        for(Aluno aluno : alunos) {
            if(existe(aluno)) {
                altera(aluno);
            } else {
                insere(aluno);
            }
        }
    }

    private boolean existe(Aluno aluno) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id FROM Alunos WHERE id = ? LIMIT 1";

        Cursor cursor = db.rawQuery(sql, new String[]{aluno.getId()});
        int quantidadeRegistros = cursor.getCount();
        return quantidadeRegistros > 0;
    }
}