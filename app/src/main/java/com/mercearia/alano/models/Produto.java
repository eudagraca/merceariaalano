package com.mercearia.alano.models;

import com.mercearia.alano.utils.MyUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Produto {
    private String id;
    private String nome;
    private Categoria categoria;
    private Double precoUnitario;
    private Double precoCompra;
    private int quantidadeUnitaria;
    private int quantidade;


    public Produto(String nome, Double precoUnitario) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
    }

    public Produto() {
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

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(Double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public int getQuantidadeUnitaria() {
        return quantidadeUnitaria;
    }

    public void setQuantidadeUnitaria(String quantidadeUnitaria) {
        this.quantidadeUnitaria = Integer.parseInt(quantidadeUnitaria);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = Integer.parseInt(quantidade);
    }



    public String getDataRegisto() {

        SimpleDateFormat formatter = new SimpleDateFormat(MyUtils.PATTERN_DATE);
        Date date = new Date();
        return formatter.format(date);
    }
}
