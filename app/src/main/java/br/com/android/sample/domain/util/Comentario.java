package br.com.android.sample.domain.util;

import java.util.Date;

/**
 * Created by tony on 10/10/16.
 */
public class Comentario {

    private String name;
    private String texto;
    private Date data;

    public Comentario(String name, String texto, Date data) {
        this.name = name;
        this.texto = texto;
        this.data = data;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
}
