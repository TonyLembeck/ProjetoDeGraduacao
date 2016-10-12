package br.com.android.sample.domain;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by tony on 10/10/16.
 */
public class Foto {

    private String id;
    private String idUser;
    private Bitmap foto;
    private Date data;

    public Foto(String id, String idUser, Bitmap foto, Date data) {
        this.id = id;
        this.idUser = idUser;
        this.foto = foto;
        this.data = data;
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

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
