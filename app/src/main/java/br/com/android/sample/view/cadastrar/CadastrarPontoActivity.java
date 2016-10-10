package br.com.android.sample.view.cadastrar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.util.Comentario;
import br.com.android.sample.domain.util.Foto;
import br.com.android.sample.view.autenticacao.ComumActivity;

public class CadastrarPontoActivity extends ComumActivity implements DatabaseReference.CompletionListener{

    private double altura;
    private double altitude;
    private double latitude;
    private double longitude;
    private String caminhoFoto;
    private File file;
    private Ponto ponto;
    private EditText nome;
    private ImageView imagemFoto;
    private EditText coment;
    private Bitmap bitmap;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_ponto);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null){
                altura = params.getDouble("altura");
                latitude = params.getDouble("latitude");
                longitude = params.getDouble("longitude");
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    protected void initPonto(){
        ponto = new Ponto();
        Comentario comentario = new Comentario(nome.getText().toString() + "_comentario_1",
                coment.getText().toString(), Calendar.getInstance().getTime() );
        Foto foto = new Foto(nome.getText().toString() + "_foto_1", bitmap, Calendar.getInstance().getTime());
        ponto.setName( nome.getText().toString() );
        ponto.setData(Calendar.getInstance().getTime());
        ponto.setLatitude(latitude);
        ponto.setLongitude(longitude);
        ponto.setAltitude(altura);
        ponto.setListaComentario(nome.getText().toString() + "_comentario_1", comentario);
        ponto.setListaImagem(nome.getText().toString() + "_foto_1", foto);
    }


    public void onBotaoFotoClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
        caminhoFoto = Environment.getExternalStorageState() + "/" + System.currentTimeMillis() + ".jpg";
        file = new File(caminhoFoto);
        Uri outputFileUri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                bitmap = (Bitmap) bundle.get("data");
                imagemFoto.setImageBitmap(bitmap);
            }
        }else{
            Toast.makeText(this, "Result: " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    public void onCadastarPontoClick(View view){

        initPonto();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pontoRef = database.getReference("Ponto");

        pontoRef.child(ponto.getName()).setValue(ponto);
/*
        DatabaseReference novaRef = pontoRef.child(nome.getText().toString());

        novaRef.child("altura 1").setValue(altura);
        novaRef.child("latitude 1").setValue(latitude);
        novaRef.child("longitude 1").setValue(longitude);
        novaRef.child("data").setValue(Calendar.getInstance().getTime());
        novaRef.child("fotos").child(nome.getText().toString() + "_foto_1").child("data").setValue(Calendar.getInstance().getTime());
        novaRef.child("fotos").child(nome.getText().toString() + "_foto_1").child("user").setValue(mAuth.getCurrentUser().getUid());
        novaRef.child("Comentarios").child(nome.getText().toString() + "_comentario_1").child("texto").setValue(coment.getText().toString());
        novaRef.child("Comentarios").child(nome.getText().toString() + "_comentario_1").child("data").setValue(Calendar.getInstance().getTime());
        novaRef.child("Comentarios").child(nome.getText().toString() + "_comentario_1").child("user").setValue(mAuth.getCurrentUser().getUid());
*/

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(nome.getText().toString() + "_foto_1");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/" + nome.getText().toString() + "_foto_1");

        InputStream stream;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        UploadTask uploadTask;
        uploadTask = mountainsRef.putStream(bs);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

        finish();
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        showToast( "Ponto criado com sucesso!" );
        closeProgressBar();
        finish();
    }


    @Override
    protected void initViews() {

        nome = (EditText) findViewById(R.id.nome_ponto);
        imagemFoto = (ImageView) findViewById(R.id.botaoFoto);
        coment = (EditText) findViewById(R.id.comentario);
    }

    @Override
    protected void initUser() {

    }
}
