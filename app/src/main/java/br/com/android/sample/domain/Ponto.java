package br.com.android.sample.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 06/10/16.
 */
public class Ponto {
    public static String PROVIDER = "br.com.thiengo.thiengocalopsitafbexample.domain.Ponto.PROVIDER";

    private String id;
    private String idUser;
    private String nome;
    private Date data;
    private double latitude;
    private double longitude;
    private double userLatitude;
    private double userLongitude;
    private double userAltitude;
    private double altitude;
    private double altura;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) { this.data = data; }

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

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getUserAltitude() {
        return userAltitude;
    }

    public void setUserAltitude(double userAltitude) {
        this.userAltitude = userAltitude;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getAltura() {
        return altura;
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
}
