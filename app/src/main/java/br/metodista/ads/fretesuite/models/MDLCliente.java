package br.metodista.ads.fretesuite.models;

/**
 * Created by leonardo.lima on 05/05/2017.
 */

public class MDLCliente {

    private Long _id;
    private String nome;
    private String cep;
    private String endereco;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public MDLCliente(){}

    public MDLCliente(Long id, String nome, String cep, String endereco) {
        this._id = id;
        this.nome = nome;
        this.cep = cep;
        this.endereco = endereco;
    }

    public MDLCliente(String nome, String cep, String endereco) {
        this.nome = nome;
        this.cep = cep;
        this.endereco = endereco;
    }
}