package br.com.android.sample.domain.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.android.sample.R;

/**
 * Created by tony on 28/10/16.
 */
public class ListaAdapterComentario extends ArrayAdapter{
    private Context context;
    private ArrayList<ItemComentario> lista;

    public ListaAdapterComentario(Context context, ArrayList<ItemComentario> lista){
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemComentario itemComentario = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.comentario_layout_view, null);
        TextView nomeUser = (TextView) convertView.findViewById(R.id.nomeUser);
        nomeUser.setText(itemComentario.getNome());

        TextView comentario = (TextView) convertView.findViewById(R.id.comentario);
        comentario.setText(itemComentario.getComentario());

        return convertView;
    }
}
