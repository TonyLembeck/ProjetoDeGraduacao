package br.com.android.sample.view.cadastro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;

import br.com.android.sample.R;
import br.com.android.sample.infrastructure.mask.Mask;

@EActivity(R.layout.activity_cadastro_usuario)
public class CadastroUsuarioActivity extends AppCompatActivity {

    private Bitmap bitmap;

    @ViewById(R.id.imageButton)
    ImageView img;

    @ViewById(R.id.editTextData)
    EditText data;


    @AfterViews
    public void onAfterViews() {
        data.addTextChangedListener(Mask.insert("##/##/####", data));
    }


    @UiThread
    @Click(R.id.imageButton)
    public void onTirarBotaoClick(){
        abrirCamera();

    }

    public void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if(requestCode == 0 && resultCode == RESULT_OK){

            if (bitmap != null)
                bitmap.recycle();

            Bundle bundle = data != null ? data.getExtras() : null;
            bitmap = bundle != null ? (Bitmap) bundle.get("data") : null;
            img.setImageBitmap(resizeImage(this, bitmap, 100, 150));
        }

    }

    private static Bitmap resizeImage (Context context, Bitmap bmpOriginal, float newWidth, float newHeigth){
        Bitmap novoBmp = null;
        float temp;
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();
        if (w > h) {
            temp = newWidth;
            newWidth = newHeigth;
            newHeigth = temp;
        }

        float densityFactor = context.getResources().getDisplayMetrics().density;
        float novoW = newWidth * densityFactor;
        float novoH = newHeigth * densityFactor;

        //Calcula escala em percentual do tamanho original para o novo tamanho
        float scalaW = novoW / w;
        float scalaH = novoH / h;

        //Criando uma matriz para manipulação da imagem bitmap
        Matrix matrix = new Matrix();

        //Definindo a proporção da escala para o matriz
        matrix.postScale(scalaW, scalaH);

        //Criando o novo Bitmap com o novo tamanho
        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h,matrix, true);

        return novoBmp;
    }
}