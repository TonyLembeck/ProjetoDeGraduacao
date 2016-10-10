package br.com.android.sample.domain.util;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by tony on 10/10/16.
 */
public class Foto {

    private String name;
    private Bitmap foto;
    private Date data;

    public Foto(String name, Bitmap foto, Date data) {
        this.name = name;
        this.foto = foto;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
