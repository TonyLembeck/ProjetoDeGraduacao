package br.com.android.sample.view.cadastrar;

import android.content.Intent;
import android.graphics.Bitmap;

import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import java.util.Date;
import java.util.UUID;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.Comentario;
import br.com.android.sample.domain.Foto;
import br.com.android.sample.view.autenticacao.ComumActivity;

public class CadastrarPontoActivity extends ComumActivity implements DatabaseReference.CompletionListener{

    private double altura, altitude, latitude, longitude, userLatitude, userLongitude, userAltitude, distancia, anguloBussula, anguloAccelerometro;
    private Ponto ponto;
    private Comentario comentario;
    private Foto foto;
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
                altitude = params.getDouble("altitude");
                latitude = params.getDouble("latitude");
                longitude = params.getDouble("longitude");
                userLatitude = params.getDouble("userLatitude");
                userLongitude = params.getDouble("userLongitude");
                userAltitude = params.getDouble("userAltitude");
                distancia = params.getDouble("distancia");
                anguloBussula = params.getDouble("anguloBussula");
                anguloAccelerometro = params.getDouble("anguloAccelerometro");
            }
        }
        setTitle(this.getString(R.string.cadastrar_ponto));
    }

    protected void initPonto(){
        UUID uid = UUID.fromString(this.getString(R.string.uuid));
        ponto = new Ponto();
        ponto.setId(uid.randomUUID() + "");
        ponto.setIdUser(mAuth.getCurrentUser().getUid());
        ponto.setNome(nome.getText().toString());
        ponto.setData(new Date());
        ponto.setLatitude(latitude);
        ponto.setLongitude(longitude);
        ponto.setAltura(altura);
        ponto.setAltitude(altitude);
        ponto.setUserLatitude(userLatitude);
        ponto.setUserLongitude(userLongitude);
        ponto.setUserAltitude(userAltitude);
        ponto.setDistancia(distancia);
        ponto.setAnguloBussula(anguloBussula);
        ponto.setAnguloAccelerometro(anguloAccelerometro);


        comentario = new Comentario(uid.randomUUID() + "", ponto.getIdUser(),
                coment.getText().toString(), ponto.getData() );

        ponto.setListaComentario(comentario.getId(), comentario);

        foto = new Foto(uid.randomUUID() + "", ponto.getIdUser(), bitmap, ponto.getData());

        ponto.setListaImagem(foto.getId(), foto);
    }


    public void onBotaoFotoClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                bitmap = (Bitmap) bundle.get("data");
                imagemFoto.setImageBitmap(bitmap);
            }
        }
    }

    protected void onCadastarPontoClick(View view){

        initPonto();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pontoRef = database.getReference("pontos");
        pontoRef.child(ponto.getId()).setValue(ponto);
        DatabaseReference novaRef = pontoRef.child(ponto.getId());

        novaRef.child("fotos").child(foto.getId()).child("data").setValue(foto.getData());
        novaRef.child("fotos").child(foto.getId()).child("idUser").setValue(ponto.getIdUser());
        novaRef.child("fotos").child(foto.getId()).child("id").setValue(ponto.getId());

        novaRef.child("comentarios").child(comentario.getId()).setValue(comentario);

        fotoUpdate();

        finish();
    }

    protected void fotoUpdate(){

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");

        // Create a reference to 'images/mountains.jpg'
        StorageReference fotoRef = storageRef.child("fotos/" + ponto.getId()+ "/"  + foto.getId());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        UploadTask uploadTask;
        uploadTask = fotoRef.putStream(bs);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        showToast( this.getString(R.string.ponto_criado) );
        closeProgressBar();
        finish();
    }


    protected void initViews() {

        nome = (EditText) findViewById(R.id.nome_ponto);
        imagemFoto = (ImageView) findViewById(R.id.botaoFoto);
        coment = (EditText) findViewById(R.id.comentario);
    }

}
