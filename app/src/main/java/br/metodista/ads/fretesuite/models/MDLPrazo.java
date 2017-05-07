package br.metodista.ads.fretesuite.models;

/**
 * Created by leonardo.lima on 07/05/2017.
 */

public class MDLPrazo {

    private String codigo;
    private String prazoEntrega;
    private String msgErro;
    private boolean entregaDomiciliar;
    private boolean entregaSabado;

    public MDLPrazo() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(String prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public boolean isEntregaDomiciliar() {
        return entregaDomiciliar;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public void setEntregaDomiciliar(boolean entregaDomiciliar) {
        this.entregaDomiciliar = entregaDomiciliar;
    }

    public boolean isEntregaSabado() {
        return entregaSabado;
    }

    public void setEntregaSabado(boolean entregaSabado) {
        this.entregaSabado = entregaSabado;
    }
}

