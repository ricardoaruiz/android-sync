package br.com.rar.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import br.com.rar.agenda.R;
import br.com.rar.agenda.modelo.Aluno;

/**
 * Created by ralmendro on 12/29/16.
 */

public class AlunoAdapter extends BaseAdapter{


    private final Context context;
    private final List<Aluno> alunos;

    public AlunoAdapter(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return this.alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Aluno aluno = this.alunos.get(position);

        View view = convertView != null ? convertView : LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(aluno.getTelefone());

        if(aluno.getFoto() != null && new File(aluno.getFoto()).exists()) {
            ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
            Bitmap bitmap = BitmapFactory.decodeFile(aluno.getFoto());
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
        }

        return view;
    }
}

