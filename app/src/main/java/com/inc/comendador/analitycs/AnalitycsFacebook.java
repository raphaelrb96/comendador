package com.inc.comendador.analitycs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

public class AnalitycsFacebook {

    private Activity activityAtual;
    private Context context;
    private AppEventsLogger logger;

    public AnalitycsFacebook(Activity activity) {
        activityAtual = activity;
        context = activity;
        logger = AppEventsLogger.newLogger(activity);
        //FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }

    public void carteiraView (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("CarteiraView", params);
    }

    public void afiliacao (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeAdm", nomeUser);
        params.putString("IdAdm", idUser);
        params.putString("ImagemAdm", imagemUser);
        logger.logEvent("Afiliacao", params);
    }

    public void cadastroDeUsuario (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("Cadastro", params);
    }

    public void visitaAoPainelRevendedor (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("PainelRevendedorView", params);
    }

    public void visitaAoPainelAfiliados (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("PainelAfiliadosView", params);
    }

    public void logRevenda (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("Revenda", params);
    }

    public void logFormularioRevendaConcluido (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("FormularioRevendaConcluido", params);
    }

    public void logViewCartRevendaEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("ViewCartRevenda", params);
    }

    public void logViewPainelRevendaEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("ViewPainelRevenda", params);
    }

    public void logRevenderProdutoEvent (String nome, String idProduto, int categoria, boolean promocional, String imagem, double valToSum) {
        Bundle params = new Bundle();
        params.putString("Nome", nome);
        params.putString("IdProduto", idProduto);
        params.putInt("Categoria", categoria);
        params.putBoolean("Promocional", promocional);
        params.putString("Imagem", imagem);
        logAddToCartEvent(nome, idProduto, "product", "BRL", valToSum);
        logger.logEvent("RevenderProduto", valToSum, params);
    }

    public void logAdicionarAoCarrinhoEvent (String nome, String idProduto, int categoria, boolean promocional, String imagem, double valToSum) {
        Bundle params = new Bundle();
        params.putString("Nome", nome);
        params.putString("IdProduto", idProduto);
        params.putInt("Categoria", categoria);
        params.putBoolean("Promocional", promocional);
        params.putString("Imagem", imagem);
        logAddToCartEvent(nome, idProduto, "product", "BRL", valToSum);
        logger.logEvent("AdicionarAoCarrinho", valToSum, params);
    }

    public void logAddToCartEvent (String contentData, String contentId, String contentType, String currency, double price) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price, params);
    }


    public void logUsuarioAdicionaProdutoAoCartEvent (String nome, String idProduto, int categoria, boolean promocional, String imagem, String nomeDoUsuario, String uidUsuario, String emailUsuario, String fotoUsuario, double valToSum) {
        Bundle params = new Bundle();
        params.putString("Nome", nome);
        params.putString("IdProduto", idProduto);
        params.putInt("Categoria", categoria);
        params.putBoolean("Promocional", promocional);
        params.putString("Imagem", imagem);
        params.putString("NomeDoUsuario", nomeDoUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putString("EmailUsuario", emailUsuario);
        params.putString("FotoUsuario", fotoUsuario);
        logger.logEvent("UsuarioAdicionaProdutoAoCart", valToSum, params);
    }


    public void logProdutoVizualizadoPeloRevendedorEvent (String nomeProduto, String idProduto, double valorProduto) {
        Bundle params = new Bundle();
        params.putString("NomeProduto", nomeProduto);
        params.putString("IdProduto", idProduto);
        params.putDouble("ValorProduto", valorProduto);
        logger.logEvent("ProdutoVizualizadoParaRevenda", params);
        logViewContentEvent(nomeProduto, nomeProduto, idProduto, "BRL", valorProduto);
    }

    public void logProdutoVizualizadoEvent (String nomeProduto, String idProduto, double valorProduto) {
        Bundle params = new Bundle();
        params.putString("NomeProduto", nomeProduto);
        params.putString("IdProduto", idProduto);
        params.putDouble("ValorProduto", valorProduto);
        logger.logEvent("ProdutoVizualizado", params);
        logViewContentEvent(nomeProduto, nomeProduto, idProduto, "BRL", valorProduto);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logViewContentEvent (String contentType, String contentData, String contentId, String currency, double price) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, price, params);
    }


    public void logProdutoCompradoEvent (String nomeProduto, String idProduto, String imagemProduto, double valToSum) {
        Bundle params = new Bundle();
        params.putString("NomeProduto", nomeProduto);
        params.putString("IdProduto", idProduto);
        params.putString("ImagemProduto", imagemProduto);
        logger.logEvent("ProdutoComprado", valToSum, params);
    }


    public void logUserPesquisouEnderecoEvent (String nomeUser, String idUser, String imagemUser, String endereco) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putString("Endereco", endereco);
        logger.logEvent("UserPesquisouEndereco", params);
    }


    public void logUserEnviaMensagemEvent (String nomeUser, String idUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        logger.logEvent("UserEnviaMensagem", params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT);
    }

    public void logUserVisitaCarrinhoEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("UserVisitaCarrinho", params);
    }


    public void logUserVisitaCheckoutEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, int itens) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        logInitiateCheckoutEvent(nomeUser, idUser, "usuario", itens, false, "BRL", total);
        logger.logEvent("UserVisitaCheckout", params);
    }

    public void logInitiateCheckoutEvent (String contentData, String contentId, String contentType, int numItems, boolean paymentInfoAvailable, String currency, double totalPrice) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putInt(AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE, paymentInfoAvailable ? 1 : 0);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, totalPrice, params);
    }

    public void logUserCompraSolicitadaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraSolicitada", total, params);
    }


    public void logUserCompraConcluidaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraConcluida", total, params);
    }


    public void logUserCompraCanceladaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraCancelada", total, params);
    }

    public void logPesquisaProdutoEvent (String nomePesquisa, String nomeUsuario, String uidUsuario, boolean isSucess, String resultado) {
        Bundle params = new Bundle();
        params.putString("NomePesquisa", nomePesquisa);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putBoolean("ResultadoEncontrado", isSucess);
        logSearchEvent("product", resultado, nomeUsuario, nomePesquisa, isSucess);
        logger.logEvent("PesquisaProduto", params);
    }


    public void logSearchEvent (String contentType, String contentData, String contentId, String searchString, boolean success) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, searchString);
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success ? 1 : 0);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, params);
    }


    public void logLoginFaceEvent (boolean facebookLogin, String nomeUsuario, String uidUsuario) {
        Bundle params = new Bundle();
        params.putBoolean("FacebookLogin", facebookLogin);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        logger.logEvent("LoginFace", params);
    }


    public void logLoginGoogleEvent (boolean googleLogin, String nomeUsuario, String uidUsuario) {
        Bundle params = new Bundle();
        params.putBoolean("GoogleLogin", googleLogin);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        logger.logEvent("LoginGoogle", params);
    }

}
