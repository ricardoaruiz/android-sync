package br.com.rar.agenda.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ralmendro on 17/05/17.
 */
public class AlunoPreferences {

    private static final String ALUNO_PREFERENCES = "br.com.rar.agenda.preferences.AlunoPreferences";
    private static final String VERSAO_DO_DADO = "versao_do_dado";
    private Context context;

    public AlunoPreferences(Context context) {
        this.context = context;
    }

    /**
     * Salva a versão corrente no sharedpreferences
     * @param versao
     */
    public void salvarVersao(String versao) {
        SharedPreferences sharedPrefenreces = getSharedPrefenreces();
        SharedPreferences.Editor editor = sharedPrefenreces.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    /**
     * Busca a versão corrente do sharedpreferences
     * @return
     */
    public String getVersao() {
        SharedPreferences sharedPrefenreces = getSharedPrefenreces();
        return sharedPrefenreces.getString(VERSAO_DO_DADO, "");
    }

    /**
     * Verifica se existe uma versão no sharedpreferences
     * @return
     */
    public boolean temVersao() {
        return !getVersao().isEmpty();
    }

    private SharedPreferences getSharedPrefenreces() {
        return this.context.getSharedPreferences(ALUNO_PREFERENCES, Context.MODE_PRIVATE);
    }
}