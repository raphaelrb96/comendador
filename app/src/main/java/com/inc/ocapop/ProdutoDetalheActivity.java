package com.inc.ocapop;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.inc.ocapop.analitycs.AnalitycsFacebook;
import com.inc.ocapop.analitycs.AnalitycsGoogle;

import static com.inc.ocapop.FragmentMain.user;
import static com.inc.ocapop.MainActivity.ids;

public class ProdutoDetalheActivity extends AppCompatActivity {

    private TextView nomeProd, valor, descricao, tv_detalhe_prod_valor_antigo, tv_detalhe_prod_valor_economia;
    private ImageView imagem;
    private FirebaseFirestore firebaseFirestore;
    private ProdObjParcelable prodObjParcelable;
    private Toast mToast;
    private ExtendedFloatingActionButton efab;
    private ProgressBar pb;

    private View bt_voltar_produto_detalhe;

    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        nomeProd = (TextView) findViewById(R.id.tv_detalhe_prod_nome);
        descricao = (TextView) findViewById(R.id.tv_detalhe_prod_descricao);
        tv_detalhe_prod_valor_antigo = (TextView) findViewById(R.id.tv_detalhe_prod_valor_antigo);
        tv_detalhe_prod_valor_economia = (TextView) findViewById(R.id.tv_detalhe_prod_valor_economia);
        valor = (TextView) findViewById(R.id.tv_detalhe_prod_valor);
        imagem = (ImageView) findViewById(R.id.img_pod_detalhe);
        pb = (ProgressBar) findViewById(R.id.pb_prod_detalhe);
        efab = (ExtendedFloatingActionButton) findViewById(R.id.efab_prod_detalhe);
        bt_voltar_produto_detalhe = (View) findViewById(R.id.bt_voltar_produto_detalhe);
        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);
        prodObjParcelable = getIntent().getParcelableExtra("prod");
        if (prodObjParcelable == null) {
            finish();
            return;
        }

        Glide.with(this).load(prodObjParcelable.getImgCapa()).into(imagem);
        valor.setText(String.valueOf((int) prodObjParcelable.getProdValor()) + ",00");
        nomeProd.setText(prodObjParcelable.getProdName());
        descricao.setText(prodObjParcelable.getDescr());
        if (prodObjParcelable.getValorAntigo() > 0) {
            tv_detalhe_prod_valor_antigo.setVisibility(View.VISIBLE);
            tv_detalhe_prod_valor_economia.setVisibility(View.VISIBLE);
            int vAntigoInt = (int)prodObjParcelable.getValorAntigo();
            int vAtualInt = (int)prodObjParcelable.getProdValor();
            int economiaInt = vAntigoInt - vAtualInt;
            tv_detalhe_prod_valor_economia.setText("Você vai economizar \nR$ " + economiaInt + ",00 reais");
            tv_detalhe_prod_valor_antigo.setText((int)prodObjParcelable.getValorAntigo() + ",00");
            tv_detalhe_prod_valor_antigo.setPaintFlags(tv_detalhe_prod_valor_antigo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv_detalhe_prod_valor_antigo.setVisibility(View.GONE);
            tv_detalhe_prod_valor_economia.setVisibility(View.GONE);
        }

        firebaseFirestore = FirebaseFirestore.getInstance();

        bt_voltar_produto_detalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void voltarDetalhe(View view) {
        onBackPressed();
    }

    public void abrirCarrinho() {
        Intent intent = new Intent(ProdutoDetalheActivity.this, CarrinhoActivity.class);
        startActivity(intent);
    }

    public void comprarProdDetalhe(View view) {
        if (user.isAnonymous() || user == null) {
            mToast = Toast.makeText(this, "Faça Login para poder efutuar sua compra", Toast.LENGTH_LONG);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast.show();
        } else {
            final ProdObj prodObj = prodObjParcelable.getProd();
            final String str = prodObj.getIdProduto();
            CarComprasActivy carComprasActivy = new CarComprasActivy(str, prodObj.getProdName(), prodObj.getFabricante(), prodObj.getImgCapa(), prodObj.getProdValor(), 1, ((float) 1) * prodObj.getProdValor());
            DocumentReference reference = firebaseFirestore.collection("carComprasActivy").document("usuario").collection(user.getUid()).document(str);
            if (!ids.contains(str)) {

                pb.setVisibility(View.VISIBLE);
                efab.setVisibility(View.GONE);
                reference.set(carComprasActivy).addOnSuccessListener(ProdutoDetalheActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ids.add(str);
                        abrirCarrinho();
                        finish();
                    }
                }).addOnFailureListener(ProdutoDetalheActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mToast = Toast.makeText(ProdutoDetalheActivity.this, "Erro ao Adicionar " + prodObj.getProdName() + " ao carrinho ", Toast.LENGTH_LONG);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast.show();
                    }
                });

            } else {
                abrirCarrinho();
                finish();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        analitycsGoogle.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
        analitycsFacebook.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
    }
}
