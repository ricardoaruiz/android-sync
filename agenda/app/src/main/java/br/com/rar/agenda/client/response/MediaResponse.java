package br.com.rar.agenda.client.response;

import java.io.Serializable;

/**
 * Created by ralmendro on 1/3/17.
 */

public class MediaResponse implements Serializable {

    private static final long serialVersionUID = -7702872297739203488L;

    private String media;

    private String quantidade;

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
