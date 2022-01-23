package com.inc.comendador.objAmaCompras;

import java.util.ArrayList;
import java.util.Map;

public class ProdutoLoja extends Produto {

    private ArrayList<String> cores;
    private ArrayList<String> tamanhos;
    private ArrayList<String> numeros;
    private Map<Integer, Boolean> categorias;
    private String tempoDePreparo;
    private String variante1;
    private String variante2;
    private String variante3;
    private ArrayList<String> variaveis1;
    private ArrayList<String> variaveis2;
    private ArrayList<String> variaveis3;

    public ProdutoLoja(String id, String nome, String descricao, double preco, float rating, long timeStamp, boolean disponivel, long quantidade, String fotoPrincipal, ArrayList<String> fotos, Map<String, Boolean> tag, double peso, long numeroViews, long numeroAddCart, long numeroDeItensVendidos, long numeroDeCompartilhamentos, long numeroDeComentarios, long numeroDeMensagens, long ultimaMensagem, long ultimaCompra, long ultimaVezAdicionadoAoCart, long ultimoCompartilhamento, long ultimaView, ArrayList<String> cores, ArrayList<String> tamanhos, ArrayList<String> numeros, Map<Integer, Boolean> categorias, String tempoDePreparo, String variante1, String variante2, String variante3, ArrayList<String> variaveis1, ArrayList<String> variaveis2, ArrayList<String> variaveis3) {
        super(id, nome, descricao, preco, rating, timeStamp, disponivel, quantidade, fotoPrincipal, fotos, tag, peso, numeroViews, numeroAddCart, numeroDeItensVendidos, numeroDeCompartilhamentos, numeroDeComentarios, numeroDeMensagens, ultimaMensagem, ultimaCompra, ultimaVezAdicionadoAoCart, ultimoCompartilhamento, ultimaView);
        this.cores = cores;
        this.tamanhos = tamanhos;
        this.numeros = numeros;
        this.categorias = categorias;
        this.tempoDePreparo = tempoDePreparo;
        this.variante1 = variante1;
        this.variante2 = variante2;
        this.variante3 = variante3;
        this.variaveis1 = variaveis1;
        this.variaveis2 = variaveis2;
        this.variaveis3 = variaveis3;
    }

    public ProdutoLoja() {

    }

    public ArrayList<String> getCores() {
        return cores;
    }

    public ArrayList<String> getTamanhos() {
        return tamanhos;
    }

    public ArrayList<String> getNumeros() {
        return numeros;
    }

    public Map<Integer, Boolean> getCategorias() {
        return categorias;
    }

    public String getTempoDePreparo() {
        return tempoDePreparo;
    }

    public String getVariante1() {
        return variante1;
    }

    public String getVariante2() {
        return variante2;
    }

    public String getVariante3() {
        return variante3;
    }

    public ArrayList<String> getVariaveis1() {
        return variaveis1;
    }

    public ArrayList<String> getVariaveis2() {
        return variaveis2;
    }

    public ArrayList<String> getVariaveis3() {
        return variaveis3;
    }
}
