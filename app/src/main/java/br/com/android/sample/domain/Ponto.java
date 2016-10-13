package br.com.android.sample.domain;

import com.beyondar.android.world.GeoObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.android.sample.R;
import br.com.android.sample.domain.util.LibraryClass;

/**
 * Created by tony on 06/10/16.
 */
public class Ponto {
    public static String PROVIDER = "br.com.thiengo.thiengocalopsitafbexample.domain.Ponto.PROVIDER";

    private String id;
    private String idUser;
    private String nome;
    private Calendar data;
    private double latitude;
    private double longitude;
    private double altitude;
    private double altura;
    private Map<String, Comentario> listaComentario = new HashMap<String, Comentario>();
    private Map<String, Foto> listaImagem = new HashMap<String, Foto>();
    private static ArrayList<Ponto> pontos = new ArrayList<>();
    private static DatabaseReference firebaseRef;

    public Ponto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Comentario getListaComentario(String id) {
        return listaComentario.get(id);
    }

    public void setListaComentario(String id, Comentario comentario) {
        listaComentario.put(id, comentario);
    }

    public Foto getListaImagem(String id) {
        return listaImagem.get(id);
    }

    public void setListaImagem(String id, Foto foto) {
        listaImagem.put(id, foto);
    }

    public static ArrayList<Ponto> getPontos(){

        firebaseRef = LibraryClass.getFirebase();
        firebaseRef = firebaseRef.child("pontos");


        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ref = dataSnapshot.getKey();
                DatabaseReference novaRef;
                novaRef = firebaseRef.child(ref);


                novaRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Ponto ponto = dataSnapshot.getValue(Ponto.class);
                        pontos.add(ponto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });



        return pontos;
    }



}
