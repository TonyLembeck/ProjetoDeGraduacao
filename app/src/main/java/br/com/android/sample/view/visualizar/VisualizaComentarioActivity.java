package br.com.android.sample.view.visualizar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import br.com.android.sample.R;
import br.com.android.sample.domain.Comentario;
import br.com.android.sample.domain.util.ItemComentario;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.domain.util.ListaAdapterComentario;
import br.com.android.sample.view.autenticacao.ComumActivity;

public class VisualizaComentarioActivity extends ComumActivity implements ViewSwitcher.ViewFactory {

    private String idPonto;
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseComentRef, refComentario, refUser;
    private ArrayList<ItemComentario> listaItemComentario;
    private ListaAdapterComentario listaAdapterComentario;
    private String[] from;
    private int[] to;
    private View viewAddComentario;
    private AlertDialog alertaDialog;
    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_comentario);
        mAuth = FirebaseAuth.getInstance();

        listView = (ListView) findViewById(R.id.listView);
        listaItemComentario = new ArrayList<ItemComentario>();
        listaAdapterComentario = new ListaAdapterComentario(this, listaItemComentario);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null){
                idPonto = params.getString("idPonto");
                setTitle(params.getString("nome"));
            }
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBarComentario);
        from = new String[] {"nome", "comentario"};
        to = new int[] {android.R.id.text1, android.R.id.text2};
        getComentarios();

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
        refUser = firebaseComentRef.child("users");
        refComentario = firebaseComentRef.child("pontos").child(idPonto).child("comentarios");

        refComentario.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String strRef = dataSnapshot.getKey();
                progressBar.setVisibility( View.VISIBLE);

                refComentario.child(strRef).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Comentario coment = dataSnapshot.getValue(Comentario.class);



                        refUser.child(coment.getIdUser()).child("name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                listaItemComentario.add(new ItemComentario( dataSnapshot.getValue(String.class), coment.getComentario()));
                                listView.setAdapter(listaAdapterComentario);

                                progressBar.setVisibility( View.GONE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });
        //listView.setAdapter(new SimpleAdapter(VisualizaComentarioActivity.this,listDeComentarios, android.R.layout.two_line_list_item, from, to));

    }


    public void onFotoClick(View view){

        Intent intent = new Intent(VisualizaComentarioActivity.this, VisualizaFotoActivity.class);
        intent.putExtra("idPonto", idPonto);
        startActivity(intent);
    }

    public void onAddComentarioClick(View view){

        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        viewAddComentario = li.inflate(R.layout.add_comentarios, null);
        //definimos para o botão do layout um clickListener
        viewAddComentario.findViewById(R.id.confirmarAddComentario).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (((EditText) viewAddComentario.findViewById(R.id.addComentario)).getText().toString().equals("")) {
                    showToast(VisualizaComentarioActivity.this.getString(R.string.escrever_comentario));
                }else{
                    UUID uid = UUID.fromString(VisualizaComentarioActivity.this.getString(R.string.uuid));
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference pontoRef = database.getReference("pontos");
                    DatabaseReference novaRef = pontoRef.child(idPonto);
                    Comentario comentario = new Comentario(uid.randomUUID() + "", mAuth.getCurrentUser().getUid(),
                            ((EditText) viewAddComentario.findViewById(R.id.addComentario)).getText().toString(), new Date());

                    novaRef.child("comentarios").child(comentario.getId()).setValue(comentario);

                    //exibe um Snackbar informativo.
                    showToast(VisualizaComentarioActivity.this.getString(R.string.comentario_cancelado));
                    alertaDialog.dismiss();
                }
            }
        });
        viewAddComentario.findViewById(R.id.cancelarAddComentario).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //exibe um Snackbar informativo.
                showToast(VisualizaComentarioActivity.this.getString(R.string.comentario_cadastrado));
                alertaDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.cad_novo_comentario));
        builder.setView(viewAddComentario);
        alertaDialog  = builder.create();
        alertaDialog.show();
    }
}
