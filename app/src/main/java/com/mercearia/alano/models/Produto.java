package com.mercearia.alano.models;

import com.mercearia.alano.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Produto {
    private String id;
    private String nome;
    private float precoVenda;
    private float precoCompra;
    private int quantidade, quantidadeVendida;
    private Date date;
    private String data;
    private float lucro;
    private float valorEmCaixa;

    public Produto() {
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

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

    public float getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(float precoVenda) {
        this.precoVenda = precoVenda;
    }

    public float getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(float precoCompra) {
        this.precoCompra = precoCompra;
    }

    public int getQuantidade() {
        return quantidade;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getDataRegisto() {
        SimpleDateFormat formatter = new SimpleDateFormat(Helper.PATTERN_DATE);
        date = new Date();
        return formatter.format(date);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDate() {
        return date;
    }

    public float getLucro() {
        return lucro= (getPrecoVenda()*getQuantidade()) - (getPrecoCompra());
    }

    public void setLucro(float lucro) {
        this.lucro = lucro;
    }

    public float getValorEmCaixa() {
        return valorEmCaixa;
    }

    public void setValorEmCaixa(float valorEmCaixa) {
        this.valorEmCaixa = valorEmCaixa;
    }
}
