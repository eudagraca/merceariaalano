package com.mercearia.alano.models;

public class Debit {

    private String name;
    private String data;
    private int quantidadeVendida;
    private int quantidadeRemanescente;
    private Double valorCaixa;


//    public Debit(String name, String data, int quantidadeVendida, int quantidadeRemanescente) {
//        this.name = name;
//        this.data = data;
//        this.quantidadeVendida = quantidadeVendida;
//        this.quantidadeRemanescente = quantidadeRemanescente;
//    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public int getQuantidadeRemanescente() {
        return quantidadeRemanescente;
    }

    public void setQuantidadeRemanescente(int quantidadeRemanescente) {
        this.quantidadeRemanescente = quantidadeRemanescente;
    }

    public Double getValorCaixa() {
        return valorCaixa;
    }

    public void setValorCaixa(Double valorCaixa) {
        this.valorCaixa = valorCaixa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
