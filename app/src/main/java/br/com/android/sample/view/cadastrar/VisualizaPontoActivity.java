package br.com.android.sample.view.cadastrar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import br.com.android.sample.R;
import br.com.android.sample.domain.Foto;
import br.com.android.sample.domain.Ponto;

public class VisualizaPontoActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private ImageSwitcher image;
    private ArrayList<Bitmap> imgs = new ArrayList<>();
    private String idPonto;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_ponto);
        ArrayList<Foto> fotos = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null){
                idPonto = params.getString("idPonto");
            }
        }

        StorageReference storageRef = storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");
        StorageReference fotoRef = storageRef.child("fotos/" + idPonto);


        image = (ImageSwitcher) findViewById(R.id.imagemSwitcher);
        image.setFactory(this);
        image.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        image.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new AdaptadorImagem(this, imgs, new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT)));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Drawable drawable = new BitmapDrawable(imgs.get(i));
                image.setImageDrawable(drawable);
            }
        });
    }

    @Override
    public View makeView(){
        ImageView vw = new ImageView(this);
        vw.setScaleType(ImageView.ScaleType.FIT_CENTER);
        vw.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
                ImageSwitcher.LayoutParams.MATCH_PARENT));
        return vw;
    }
}
