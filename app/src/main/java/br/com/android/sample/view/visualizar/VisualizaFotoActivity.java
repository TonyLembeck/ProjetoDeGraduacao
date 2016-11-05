package br.com.android.sample.view.visualizar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import br.com.android.sample.R;
import br.com.android.sample.domain.Foto;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.view.autenticacao.ComumActivity;
import br.com.android.sample.view.cadastrar.AdaptadorImagem;

public class VisualizaFotoActivity extends ComumActivity {
    private String idPonto;
    private DatabaseReference firebaseRef;
    private StorageReference storageRef; //= storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");
    private ArrayList<Bitmap> imgs = new ArrayList<>();
    private Gallery g;
    private ProgressBar progressBar;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private int cont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_view);
        mAuth = FirebaseAuth.getInstance();
        g = (Gallery) findViewById(R.id.gallery2);
        progressBar = (ProgressBar) findViewById(R.id.progressBarFoto);

        if (savedInstanceState != null) {
            for (int i = 0; i < (int) savedInstanceState.get("cont"); i++) {
                byte[] bytes = (byte[]) savedInstanceState.get("img" + i);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgs.add(bmp);
            }
            g.setAdapter(new AdaptadorImagem(VisualizaFotoActivity.this, imgs, new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT)));
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null) {
                idPonto = params.getString("idPonto");
            }
        }
        progressBar.setVisibility( View.GONE);
        getRefFotos();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cont", imgs.size());
        for (int i = 0; i < imgs.size(); i++) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imgs.get(i).compress(Bitmap.CompressFormat.PNG, 0, bos);
            outState.putByteArray("img" + i, bos.toByteArray());
        }
    }


    public void getRefFotos(){
        firebaseRef = LibraryClass.getFirebase();
        firebaseRef = firebaseRef.child("pontos").child(idPonto).child("fotos");
        storageRef = LibraryClass.getStorage().child("fotos/" + idPonto);
        firebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (cont == imgs.size())
                    progressBar.setVisibility( View.VISIBLE);
                final long ONE_MEGABYTE = 1024 * 1024;

                storageRef.child(dataSnapshot.getKey()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        cont ++;
                        if(cont > imgs.size()) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgs.add(bmp);
                            g.setAdapter(new AdaptadorImagem(VisualizaFotoActivity.this, imgs, new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT)));
                        }
                        progressBar.setVisibility( View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });
    }

    public void onAddFotoClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                bitmap = (Bitmap) bundle.get("data");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setNeutralButton(this.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        showToast(VisualizaFotoActivity.this.getString(R.string.foto_cancelada));
                    }
                });
                builder.setMessage(this.getString(R.string.confirmar_nova_foto));
                builder.setPositiveButton(VisualizaFotoActivity.this.getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        UUID uid = UUID.fromString(VisualizaFotoActivity.this.getString(R.string.uuid));
                        Foto foto;

                        foto = new Foto(uid.randomUUID() + "", mAuth.getCurrentUser().getUid(), bitmap, new Date());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference pontoRef = database.getReference("pontos");

                        DatabaseReference novaRef = pontoRef.child(idPonto);

                        novaRef.child("fotos").child(foto.getId()).child("data").setValue(foto.getData());
                        novaRef.child("fotos").child(foto.getId()).child("idUser").setValue(foto.getIdUser());
                        novaRef.child("fotos").child(foto.getId()).child("id").setValue(foto.getId());

                        // Create a reference to 'images/mountains.jpg'
                        StorageReference fotoRef = LibraryClass.getStorage().child("fotos/" + idPonto + "/"  + foto.getId());

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
                });
                builder.show();

            }
        }
    }
}
