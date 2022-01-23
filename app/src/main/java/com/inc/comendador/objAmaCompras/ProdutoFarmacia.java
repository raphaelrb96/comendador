package com.inc.comendador.objAmaCompras;

import java.util.ArrayList;
import java.util.Map;

public class ProdutoFarmacia extends Produto {

    private String fabricante;
    private String principioAtivo;
    private boolean precisaDeReceita;
    private String posologia;
    private int categoriaFarma;
    private int tipoDeMedicamento;

    public ProdutoFarmacia(String id, String nome, String descricao, double preco, float rating, long timeStamp, boolean disponivel, long quantidade, String fotoPrincipal, ArrayList<String> fotos, Map<String, Boolean> tag, double peso, long numeroViews, long numeroAddCart, long numeroDeItensVendidos, long numeroDeCompartilhamentos, long numeroDeComentarios, long numeroDeMensagens, long ultimaMensagem, long ultimaCompra, long ultimaVezAdicionadoAoCart, long ultimoCompartilhamento, long ultimaView, String fabricante, String principioAtivo, boolean precisaDeReceita, String posologia, int categoriaFarma, int tipoDeMedicamento) {
        super(id, nome, descricao, preco, rating, timeStamp, disponivel, quantidade, fotoPrincipal, fotos, tag, peso, numeroViews, numeroAddCart, numeroDeItensVendidos, numeroDeCompartilhamentos, numeroDeComentarios, numeroDeMensagens, ultimaMensagem, ultimaCompra, ultimaVezAdicionadoAoCart, ultimoCompartilhamento, ultimaView);
        this.fabricante = fabricante;
        this.principioAtivo = principioAtivo;
        this.precisaDeReceita = precisaDeReceita;
        this.posologia = posologia;
        this.categoriaFarma = categoriaFarma;
        this.tipoDeMedicamento = tipoDeMedicamento;
    }

    public ProdutoFarmacia() {

    }

    public String getFabricante() {
        return fabricante;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public boolean isPrecisaDeReceita() {
        return precisaDeReceita;
    }

    public String getPosologia() {
        return posologia;
    }

    public int getCategoriaFarma() {
        return categoriaFarma;
    }

    public int getTipoDeMedicamento() {
        return tipoDeMedicamento;
    }
}
