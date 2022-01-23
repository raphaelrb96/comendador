package com.inc.comendador.objAmaCompras;

import java.util.ArrayList;
import java.util.Map;

public class Automoveis extends Produto {

    private String marca;
    private int modeloCambio;
    private int combustivel;
    private int ano;
    private Map<Integer, Boolean> detalhesAutomovel;
    private int tipoDeAutomovel;
    private String cor;
    private String km;

    public Automoveis(String id, String nome, String descricao, double preco, float rating, long timeStamp, boolean disponivel, long quantidade, String fotoPrincipal, ArrayList<String> fotos, Map<String, Boolean> tag, double peso, long numeroViews, long numeroAddCart, long numeroDeItensVendidos, long numeroDeCompartilhamentos, long numeroDeComentarios, long numeroDeMensagens, long ultimaMensagem, long ultimaCompra, long ultimaVezAdicionadoAoCart, long ultimoCompartilhamento, long ultimaView, String marca, int modeloCambio, int combustivel, int ano, Map<Integer, Boolean> detalhesAutomovel, int tipoDeAutomovel, String cor, String km) {
        super(id, nome, descricao, preco, rating, timeStamp, disponivel, quantidade, fotoPrincipal, fotos, tag, peso, numeroViews, numeroAddCart, numeroDeItensVendidos, numeroDeCompartilhamentos, numeroDeComentarios, numeroDeMensagens, ultimaMensagem, ultimaCompra, ultimaVezAdicionadoAoCart, ultimoCompartilhamento, ultimaView);
        this.marca = marca;
        this.modeloCambio = modeloCambio;
        this.combustivel = combustivel;
        this.ano = ano;
        this.detalhesAutomovel = detalhesAutomovel;
        this.tipoDeAutomovel = tipoDeAutomovel;
        this.cor = cor;
        this.km = km;
    }

    public Automoveis() {

    }


    public String getMarca() {
        return marca;
    }

    public int getModeloCambio() {
        return modeloCambio;
    }

    public int getCombustivel() {
        return combustivel;
    }

    public int getAno() {
        return ano;
    }

    public Map<Integer, Boolean> getDetalhesAutomovel() {
        return detalhesAutomovel;
    }

    public int getTipoDeAutomovel() {
        return tipoDeAutomovel;
    }

    public String getCor() {
        return cor;
    }

    public String getKm() {
        return km;
    }
}
