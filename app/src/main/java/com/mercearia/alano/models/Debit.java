package com.mercearia.alano.models;

public class Debit {

    private String name;
    private String data;
    private int quantidadeVendida;
    private int quantidadeRemanescente;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
