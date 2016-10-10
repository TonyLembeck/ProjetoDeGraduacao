package br.com.android.sample.domain;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.android.sample.domain.util.Comentario;
import br.com.android.sample.domain.util.Foto;
import br.com.android.sample.domain.util.LibraryClass;

/**
 * Created by tony on 06/10/16.
 */
public class Ponto {
    public static String PROVIDER = "br.com.thiengo.thiengocalopsitafbexample.domain.Ponto.PROVIDER";

    private String id;
    private String name;
    private Date data;
    private double latitude;
    private double longitude;
    private double altitude;
    private Map<String, Comentario> listaComentario = new HashMap<String, Comentario>();
    private Map<String, Foto> listaImagem = new HashMap<String, Foto>();

    public Ponto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Comentario getListaComentario(String nome) {
        return listaComentario.get(nome);
    }

    public void setListaComentario(String nome, Comentario comentario) {
        listaComentario.put(nome, comentario);
    }

    public Foto getListaImagem(String nome) {
        return listaImagem.get(nome);
    }

    public void setListaImagem(String nome, Foto foto) {
        listaImagem.put(nome, foto);
    }

}
