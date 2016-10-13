package br.com.android.sample.view.cadastrar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by tony on 01/09/16.
 */
public class AdaptadorImagem extends BaseAdapter{
    private Context ctx;
    private final ArrayList<Bitmap> imagens;
    private final  ViewGroup.LayoutParams params;

    public AdaptadorImagem(Context c, ArrayList<Bitmap> imgs, ViewGroup.LayoutParams p){
        ctx = c;
        imagens = imgs;
        params = p;
    }

    @Override
    public int getCount() { return imagens.size();}

    @Override
    public Object getItem(int i) { return i;}

    @Override
    public long getItemId(int i) {return  i;}

    @Override
    public View getView(int i, View view, ViewGroup ViewGroup){
        ImageView vw = new ImageView(ctx);
        vw.setImageBitmap(imagens.get(i));
        //vw.setImageResource(imagens[i]);
        vw.setAdjustViewBounds(true);

        vw.setLayoutParams(params);
        return vw;
    }

}
