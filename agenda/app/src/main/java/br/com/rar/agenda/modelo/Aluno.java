package br.com.rar.agenda.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ralmendro on 12/22/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aluno implements Serializable {

    private static final long serialVersionUID = 8110372202663052350L;

    private String id;

    private String nome;

    private String endereco;

    private String telefone;

    private String site;

    private Double nota;

    private String foto;

    private int desativado;

    private int sincronizado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getDesativado() {
        return desativado;
    }

    public void setDesativado(int desativado) {
        this.desativado = desativado;
    }

    public int getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }

    public boolean isDesativado() {
        return desativado == 1;
    }

    public void sincroniza() {
        this.sincronizado = 1;
    }

    public void desincroniza() {
        this.sincronizado = 0;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.nome;
    }
}
