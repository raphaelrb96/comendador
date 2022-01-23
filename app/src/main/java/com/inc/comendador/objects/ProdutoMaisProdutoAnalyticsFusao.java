package com.inc.comendador.objects;


import com.inc.comendador.ProdObj;

public class ProdutoMaisProdutoAnalyticsFusao {

    private ProdObj prodObj;
    private ProdutoAnalitycs produtoAnalitycs;

    public ProdutoMaisProdutoAnalyticsFusao(ProdObj prodObj, ProdutoAnalitycs produtoAnalitycs) {
        this.prodObj = prodObj;
        this.produtoAnalitycs = produtoAnalitycs;
    }

    public ProdutoMaisProdutoAnalyticsFusao() {

    }

    public ProdObj getProdObj() {
        return prodObj;
    }

    public ProdutoAnalitycs getProdutoAnalitycs() {
        return produtoAnalitycs;
    }
}
