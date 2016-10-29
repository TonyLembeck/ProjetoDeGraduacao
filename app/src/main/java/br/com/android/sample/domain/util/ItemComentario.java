package br.com.android.sample.domain.util;

/**
 * Created by tony on 28/10/16.
 */
public class ItemComentario {
    private String nomeUser;
    private String comentario;

    public ItemComentario(String nome, String comentario) {
        this.nomeUser = nome;
        this.comentario = comentario;
    }

    public String getNome() {
        return nomeUser;
    }

    public void setNome(String nome) {
        this.nomeUser = nome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
