package br.com.android.sample.domain;

import java.util.Date;

/**
 * Created by tony on 10/10/16.
 */
public class Comentario {

    private String id;
    private String idUser;
    private String comentario;
    private Date data;

    public Comentario(String id, String idUser, String comentario, Date data) {
        this.id = id;
        this.idUser = idUser;
        this.comentario = comentario;
        this.data = data;
    }

    public String getComentario() {return comentario;}

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
