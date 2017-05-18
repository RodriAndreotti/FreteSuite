package br.metodista.ads.fretesuite.models;

/**
 * Created by Apartamento on 08/05/2017.
 */

public class MDLProduto {
    private Long id;
    private String descricao;
    private String codigoRastreio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    @Override
    public String toString() {
        return id + " - " +
                descricao + " - " +
                codigoRastreio;
    }
}
