package br.com.android.sample.view.cadastrar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import br.com.android.sample.R;
import br.com.android.sample.domain.Comentario;
import br.com.android.sample.domain.Foto;
import br.com.android.sample.domain.util.LibraryClass;

public class VisualizaPontoActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private ImageSwitcher imageSwitcher;
    private ArrayList<Bitmap> imgs = new ArrayList<>();
    private String idPonto;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private DatabaseReference firebaseComentRef, firebaseFotoRef;
    private ArrayList<String> fotos = new ArrayList<>();
    private ArrayList<String> comentarios = new ArrayList<String>();
    private View viewAddComentario;
    private AlertDialog alertaDialog;
    private ListView listaComentarios;
    private ProgressBar progressBarFoto, progressBarComentario;

    private int indice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_ponto);
        mAuth = FirebaseAuth.getInstance();

        listaComentarios = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null){
                idPonto = params.getString("idPonto");
            }
        }
        progressBarFoto = (ProgressBar) findViewById(R.id.progressBarFoto);
        progressBarComentario = (ProgressBar) findViewById(R.id.progressBarComentario);

        getComentarios();
        getRefFotos();

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imagemSwitcher);
        imageSwitcher.setFactory(VisualizaPontoActivity.this);
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(VisualizaPontoActivity.this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(VisualizaPontoActivity.this, android.R.anim.fade_out));

        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizaPontoActivity.this, FotoViewActivity.class);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imgs.get(indice).compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                intent.putExtra("foto", bitmapdata);
                startActivity(intent);
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


    public void getComentarios(){
        firebaseComentRef = LibraryClass.getFirebase();
        firebaseComentRef = firebaseComentRef.child("pontos").child(idPonto).child("comentarios");

        firebaseComentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ref = dataSnapshot.getKey();
                DatabaseReference novaRef;
                novaRef = firebaseComentRef.child(ref).child("comentario");
                progressBarComentario.setVisibility( View.VISIBLE);
                novaRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        comentarios.add(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                progressBarComentario.setVisibility(View.GONE);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, comentarios);
        listaComentarios.setAdapter(arrayAdapter);

    }

    public void getRefFotos(){
        firebaseFotoRef = LibraryClass.getFirebase();
        firebaseFotoRef = firebaseFotoRef.child("pontos").child(idPonto).child("fotos");
        storageRef = storageRef.child("fotos/" + idPonto);

        firebaseFotoRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBarFoto.setVisibility( View.VISIBLE);
                final long ONE_MEGABYTE = 1024 * 1024;
                storageRef.child(dataSnapshot.getKey()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgs.add(bmp);
                        if(imgs.size() == 1)
                        imageSwitcher.setImageDrawable(new BitmapDrawable(imgs.get(0)));
                        indice = 0;

                        Gallery g = (Gallery) findViewById(R.id.gallery);
                        g.setAdapter(new AdaptadorImagem(VisualizaPontoActivity.this, imgs, new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT)));
                        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                imageSwitcher.setImageDrawable(new BitmapDrawable(imgs.get(i)));
                                indice = i;
                            }
                        });
                        progressBarFoto.setVisibility( View.GONE);

                        // Data for "images/island.jpg" is returns, use this as needed
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
        startActivityForResult(intent, 0);
    }

    public void onAddComentarioClick(View view){

        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        viewAddComentario = li.inflate(R.layout.add_comentarios, null);
        //definimos para o botão do layout um clickListener
        viewAddComentario.findViewById(R.id.confirmarAddComentario).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (((EditText) viewAddComentario.findViewById(R.id.addComentario)).getText().toString().equals("")) {
                    Toast.makeText(VisualizaPontoActivity.this, "Favor escrever um comentário!", Toast.LENGTH_SHORT).show();
                }else{
                    UUID uid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference pontoRef = database.getReference("pontos");
                    DatabaseReference novaRef = pontoRef.child(idPonto);
                    Comentario comentario = new Comentario(uid.randomUUID() + "", mAuth.getCurrentUser().getUid(),
                            ((EditText) viewAddComentario.findViewById(R.id.addComentario)).getText().toString(), new Date());

                    novaRef.child("comentarios").child(comentario.getId()).setValue(comentario);

                    //exibe um Toast informativo.
                    Toast.makeText(VisualizaPontoActivity.this, "Comentário cadastrado!", Toast.LENGTH_SHORT).show();
                    alertaDialog.dismiss();
                }
            }
        });
        viewAddComentario.findViewById(R.id.cancelarAddComentario).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Toast informativo.
                Toast.makeText(VisualizaPontoActivity.this, "Comentário cancelado!", Toast.LENGTH_SHORT).show();
                alertaDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cadastrar novo comentário!");
        builder.setView(viewAddComentario);
        alertaDialog  = builder.create();
        alertaDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                bitmap = (Bitmap) bundle.get("data");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Confirmar a inclusão da nova Foto?");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        UUID uid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
                        Foto foto;

                        foto = new Foto(uid.randomUUID() + "", mAuth.getCurrentUser().getUid(), bitmap, new Date());


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference pontoRef = database.getReference("pontos");

                        DatabaseReference novaRef = pontoRef.child(idPonto);

                        novaRef.child("fotos").child(foto.getId()).child("data").setValue(foto.getData());
                        novaRef.child("fotos").child(foto.getId()).child("idUser").setValue(foto.getIdUser());
                        novaRef.child("fotos").child(foto.getId()).child("id").setValue(foto.getId());


                        // Create a storage reference from our app
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://projeto-de-graduacao.appspot.com");

                        // Create a reference to 'images/mountains.jpg'
                        StorageReference fotoRef = storageRef.child("fotos/" + idPonto + "/"  + foto.getId());

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
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(VisualizaPontoActivity.this, "Inclusão de foto cancelada", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();

            }
        }else{
            Toast.makeText(this, "Result: " + resultCode, Toast.LENGTH_LONG).show();
        }


    }
}
