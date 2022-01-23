package com.inc.comendador.objAmaCompras;

import java.util.ArrayList;
import java.util.Map;

public class Produto {

    private String id;
    private String nome;
    private String descricao;
    private double preco;
    private float rating;
    private long timeStamp;
    private boolean disponivel;
    private long quantidade;
    private String fotoPrincipal;
    private ArrayList<String> fotos;
    private Map<String, Boolean> tag;
    private double peso;

    //analitics
    private long numeroViews;
    private long numeroAddCart;
    private long numeroDeItensVendidos;
    private long numeroDeCompartilhamentos;
    private long numeroDeComentarios;
    private long numeroDeMensagens;
    private long ultimaMensagem;
    private long ultimaCompra;
    private long ultimaVezAdicionadoAoCart;
    private long ultimoCompartilhamento;
    private long ultimaView;


    public Produto(String id, String nome, String descricao, double preco, float rating, long timeStamp, boolean disponivel, long quantidade, String fotoPrincipal, ArrayList<String> fotos, Map<String, Boolean> tag, double peso, long numeroViews, long numeroAddCart, long numeroDeItensVendidos, long numeroDeCompartilhamentos, long numeroDeComentarios, long numeroDeMensagens, long ultimaMensagem, long ultimaCompra, long ultimaVezAdicionadoAoCart, long ultimoCompartilhamento, long ultimaView) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.rating = rating;
        this.timeStamp = timeStamp;
        this.disponivel = disponivel;
        this.quantidade = quantidade;
        this.fotoPrincipal = fotoPrincipal;
        this.fotos = fotos;
        this.tag = tag;
        this.peso = peso;
        this.numeroViews = numeroViews;
        this.numeroAddCart = numeroAddCart;
        this.numeroDeItensVendidos = numeroDeItensVendidos;
        this.numeroDeCompartilhamentos = numeroDeCompartilhamentos;
        this.numeroDeComentarios = numeroDeComentarios;
        this.numeroDeMensagens = numeroDeMensagens;
        this.ultimaMensagem = ultimaMensagem;
        this.ultimaCompra = ultimaCompra;
        this.ultimaVezAdicionadoAoCart = ultimaVezAdicionadoAoCart;
        this.ultimoCompartilhamento = ultimoCompartilhamento;
        this.ultimaView = ultimaView;
    }

    public Produto() {

    }

    public double getPeso() {
        return peso;
    }

    public long getNumeroDeMensagens() {
        return numeroDeMensagens;
    }

    public long getUltimaMensagem() {
        return ultimaMensagem;
    }

    public long getNumeroViews() {
        return numeroViews;
    }

    public long getNumeroAddCart() {
        return numeroAddCart;
    }

    public long getNumeroDeItensVendidos() {
        return numeroDeItensVendidos;
    }

    public long getNumeroDeCompartilhamentos() {
        return numeroDeCompartilhamentos;
    }

    public long getNumeroDeComentarios() {
        return numeroDeComentarios;
    }

    public long getUltimaCompra() {
        return ultimaCompra;
    }

    public long getUltimaVezAdicionadoAoCart() {
        return ultimaVezAdicionadoAoCart;
    }

    public long getUltimoCompartilhamento() {
        return ultimoCompartilhamento;
    }

    public long getUltimaView() {
        return ultimaView;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public float getRating() {
        return rating;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public Map<String, Boolean> getTag() {
        return tag;
    }
}
