package com.inc.ocapop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inc.ocapop.adapter.AdapterCategoriasCadastro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class CadastroDeProdutoActivity extends AppCompatActivity implements AdapterCategoriasCadastro.MudancaDeCategoria {

    private static final int REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO = 821;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Uri uriFotoProduto = null;
    private ImageView imagem_cadastro_de_produto;

    private Button bt_add_fornecedor;

    private Switch switch_disponibilidade_cadastro_de_produto;

    private CheckBox cb_cadastro_de_produto_promocional;

    private TextInputLayout inputTags;
    private TextInputEditText etTags, et_cadastro_de_produto_nome, et_cadastro_de_produto_labo,
            et_cadastro_de_produto_descricao, et_cadastro_de_produto_preco_consumidor, et_cadastro_de_produto_fornecedores, et_cadastro_de_produto_fornecedores_precos, et_cadastro_de_produto_preco_antigo;

    private TextView tv_switch_disponibilidade_cadastro_de_produto, fornecedores_e_precos;

    private FrameLayout loadding;

    private CardView btAddTag;
    private LinearLayout containerTags, container_fornecedor;
    private View btClearTags;
    private TextView tvListTags, tv_adicionar_foto_cadastro_de_produto;



    private Toast mToast = null;

    private FloatingActionButton fab_cadastro_de_produto_add_foto;
    private ExtendedFloatingActionButton bt_cadastro_de_produto_salvar, efabExcluir;

    private ArrayList<String> tagList = new ArrayList<>();
    private ArrayList<String> categoriaList = new ArrayList<>();

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private Bundle extras;
    private ProdObj prod = null;

    private RecyclerView recyclerCategorias;

    private Map<String, Boolean> CATEGORIA;
    private Map<String, Double> FORNECEDORES;
    private String fornecedoresString;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uriFotoProduto = data.getData();
                exibirFotoEscolhida(uriFotoProduto);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_po);
        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastro_prod);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViews();
        getProdIntent();
        clicksListeners();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProdIntent() {
        extras = getIntent().getExtras();
        ProdObjParcelable prodObjParcelable = getIntent().getParcelableExtra("prod");
        FORNECEDORES = new HashMap<>();
        CATEGORIA = new HashMap<>();

        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this, null);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);

        if (extras == null || prodObjParcelable == null) {
            container_fornecedor.setVisibility(View.GONE);

            tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
            return;
        }

        prod = prodObjParcelable.getProd();

        CATEGORIA = prodObjParcelable.getCategorias();

        tagList = extras.getStringArrayList("t");
        categoriaList = extras.getStringArrayList("c");

        if (prod.getFornecedores() != null) {
            FORNECEDORES = prod.getFornecedores();
            container_fornecedor.setVisibility(View.VISIBLE);
            atulizarFornecedor(FORNECEDORES);
        }

        preencherInterface(prod);
    }

    private void preencherInterface(ProdObj prod) {
        Glide.with(this).load(prod.getImgCapa()).into(imagem_cadastro_de_produto);
        tv_adicionar_foto_cadastro_de_produto.setVisibility(View.GONE);
        imagem_cadastro_de_produto.setVisibility(View.VISIBLE);
        et_cadastro_de_produto_nome.setText(prod.getProdName());
        et_cadastro_de_produto_descricao.setText(prod.getDescr());
        et_cadastro_de_produto_labo.setText(prod.getFabricante());
        String sprecoantigo = String.valueOf((int) prod.getValorAntigo());
        String svenda = String.valueOf((int) prod.getProdValor());
        switch_disponibilidade_cadastro_de_produto.setChecked(prod.isDisponivel());
        if (prod.isDisponivel()) {
            tv_switch_disponibilidade_cadastro_de_produto.setText("Disponivel");
        } else {
            tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
        }
        et_cadastro_de_produto_preco_antigo.setText(sprecoantigo);
        et_cadastro_de_produto_preco_consumidor.setText(svenda);
        atualizarListaTags();
        if (prod.isPromocional()) {
            cb_cadastro_de_produto_promocional.setChecked(true);
        }

        ArrayList<String> categs = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.categorias)));
        ArrayList<String> selecionadas = new ArrayList<>();
        for (int i = 0; i < categs.size(); i++) {
            if (prod.getCategorias() != null && prod != null) {
                if (prod.getCategorias().containsKey(String.valueOf(i))) {
                    selecionadas.add(String.valueOf(i));
                }
            }
        }

        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this, categoriaList);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);
    }


    private void findViews() {
        inputTags = (TextInputLayout) findViewById(R.id.text_input_cadastro_de_produto_tags);
        etTags = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_tags);
        btAddTag = (CardView) findViewById(R.id.bt_add_tag_cadastro_de_produto);
        btClearTags = (View) findViewById(R.id.bt_limpar_tags_cadastro_de_produto);
        containerTags = (LinearLayout) findViewById(R.id.container_tags_cadastro_de_produto);
        container_fornecedor = (LinearLayout) findViewById(R.id.container_fornecedor);
        bt_add_fornecedor = (Button) findViewById(R.id.bt_add_fornecedor);
        tvListTags = (TextView) findViewById(R.id.textview_tags_cadastro_de_produto);
        fornecedores_e_precos = (TextView) findViewById(R.id.fornecedores_e_precos);
        tv_switch_disponibilidade_cadastro_de_produto = (TextView) findViewById(R.id.tv_switch_disponibilidade_cadastro_de_produto);
        fab_cadastro_de_produto_add_foto = (FloatingActionButton) findViewById(R.id.fab_cadastro_de_produto_add_foto);
        efabExcluir = (ExtendedFloatingActionButton) findViewById(R.id.bt_cadastro_de_produto_excluir);
        tv_adicionar_foto_cadastro_de_produto = (TextView) findViewById(R.id.tv_adicionar_foto_cadastro_de_produto);
        imagem_cadastro_de_produto = (ImageView) findViewById(R.id.imagem_cadastro_de_produto);
        et_cadastro_de_produto_nome = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_nome);
        et_cadastro_de_produto_fornecedores = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_fornecedores);
        et_cadastro_de_produto_labo = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_labo);
        et_cadastro_de_produto_descricao = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_descricao);
        et_cadastro_de_produto_fornecedores_precos = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_fornecedores_precos);
        et_cadastro_de_produto_preco_consumidor = (TextInputEditText)findViewById(R.id.et_cadastro_de_produto_preco_consumidor);
        et_cadastro_de_produto_preco_antigo = (TextInputEditText) findViewById(R.id.et_cadastro_de_produto_preco_antigo);
        cb_cadastro_de_produto_promocional = (CheckBox) findViewById(R.id.cb_cadastro_de_produto_promocional);
        switch_disponibilidade_cadastro_de_produto = (Switch) findViewById(R.id.switch_disponibilidade_cadastro_de_produto);
        bt_cadastro_de_produto_salvar = (ExtendedFloatingActionButton) findViewById(R.id.bt_cadastro_de_produto_salvar);
        loadding = (FrameLayout) findViewById(R.id.tela_loading_cadastro_produto);
        recyclerCategorias = (RecyclerView) findViewById(R.id.rv_categorias_cadastro);
    }

    private void clicksListeners() {
        btClearTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparTags();
            }
        });

        btAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = pegarTexto(etTags);
                if (tag != null) {
                    tagList.add(tag);
                    etTags.setText("");
                    atualizarListaTags();
                }
            }
        });

        switch_disponibilidade_cadastro_de_produto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_switch_disponibilidade_cadastro_de_produto.setText("Disponivel");
                } else {
                    tv_switch_disponibilidade_cadastro_de_produto.setText("Indisponivel");
                }
            }
        });

        bt_add_fornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cadastro_de_produto_fornecedores.getText().toString().length() > 0 && et_cadastro_de_produto_fornecedores_precos.getText().toString().length() > 0) {
                    FORNECEDORES.put(et_cadastro_de_produto_fornecedores.getText().toString(), Double.valueOf(et_cadastro_de_produto_fornecedores_precos.getText().toString()));
                    et_cadastro_de_produto_fornecedores_precos.setText("");
                    et_cadastro_de_produto_fornecedores_precos.clearFocus();
                    et_cadastro_de_produto_fornecedores.setText("");
                    et_cadastro_de_produto_fornecedores.clearFocus();
                    atulizarFornecedor(FORNECEDORES);
                }
            }
        });

        fab_cadastro_de_produto_add_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto();
            }
        });



        bt_cadastro_de_produto_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadding.setVisibility(View.VISIBLE);
                bt_cadastro_de_produto_salvar.setVisibility(View.GONE);

                if (prod != null) {
                    salvarAtualizacao(atualizarProduto(prod), uriFotoProduto);
                    return;
                }

                ProdObj obj = verificarDados();
                if (obj == null) {
                    loadding.setVisibility(View.GONE);
                    bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                    return;
                }

                if (uriFotoProduto != null) {
                    salvarFotoEmStorage(obj);
                } else {
                    salvarDadosEmFirestore(obj.getImgCapa(), obj);
                }
            }
        });

        efabExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prod == null) {
                    return;
                }

                loadding.setVisibility(View.VISIBLE);
                bt_cadastro_de_produto_salvar.setVisibility(View.GONE);

                DocumentReference documentReference = firestore.collection("produtos").document(prod.getIdProduto());
                documentReference.delete().addOnSuccessListener(CadastroDeProdutoActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        loadding.setVisibility(View.GONE);
                        bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


    }

    private void salvarAtualizacao(final ProdObj obj, Uri uriFotoProduto) {
        if (uriFotoProduto == null) {
            DocumentReference documentReference = firestore.collection("produtos").document(obj.getIdProduto());
            documentReference.set(obj).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    loadding.setVisibility(View.GONE);
                    bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            StorageReference child = this.storageReference.child("produto");

            final StorageReference child2 = child.child(getNomeImagem(obj.getProdName()));
            child2.putFile(uriFotoProduto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        return child2.getDownloadUrl();
                    }
                    throw task.getException();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {

                        DocumentReference documentReference = firestore.collection("produtos").document(obj.getIdProduto());
                        documentReference.set(obj).addOnSuccessListener(CadastroDeProdutoActivity.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });

                    }
                    Toast.makeText(CadastroDeProdutoActivity.this, "Erro ao salvar Foto", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    loadding.setVisibility(View.GONE);
                    bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void atulizarFornecedor(Map<String, Double> fornecedores) {

        fornecedoresString = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fornecedores.forEach(new BiConsumer<String, Double>() {
                @Override
                public void accept(String s, Double aDouble) {
                    fornecedoresString = fornecedoresString + s + " = " + String.valueOf(aDouble) + "\n";
                }
            });

            fornecedores_e_precos.setText(fornecedoresString);
        }
    }

    private ProdObj verificarDados() {

//        if (uriFotoProduto == null && prod == null) {
//            toast("Insira uma foto");
//            return null;
//        }

        String nome = pegarTexto(et_cadastro_de_produto_nome);

        if (nome == null) {
            toast("Insira um nome para o produto");
            return null;
        }

        String labo = pegarTexto(et_cadastro_de_produto_labo);

        if (labo == null) {
            toast("Insira o fabricante");
            return null;
        }

        String descricao = pegarTexto(et_cadastro_de_produto_descricao);

        if (descricao == null) {
            toast("adicione uma descricao");
            return null;
        }

        String stringPrecoVenda = pegarTexto(et_cadastro_de_produto_preco_consumidor);

        if (stringPrecoVenda == null) {
            toast("Insira o preço de venda");
            return null;
        }
        float precoVenda = Float.valueOf(stringPrecoVenda);

        String stringPrecoAntigo = pegarTexto(et_cadastro_de_produto_preco_antigo);

        if (stringPrecoAntigo == null) {
            toast("Insira o preço de compra");
            return null;
        }
        float precoAntigo = Float.valueOf(stringPrecoAntigo);

        if (tagList.size() < 3) {
            int faltamXTags = 3 - tagList.size();
            toast("Insira mais " + faltamXTags + " Palavra chave (Tag)");
            return null;
        }

        Map<String, Boolean> tagMap = new HashMap<>();
        for (int i = 0; i < tagList.size(); i++) {
            tagMap.put(tagList.get(i).toLowerCase(), true);
        }

        if (categoriaList.size() < 1) {
            toast("Insira uma categoria ");
            return null;
        }

        Map<String, Boolean> catMap = new HashMap<>();
        for (int i = 0; i < categoriaList.size(); i++) {
            catMap.put(categoriaList.get(i).toLowerCase(), true);
        }


        ProdObj prodObj = new ProdObj(catMap, descricao, switch_disponibilidade_cadastro_de_produto.isChecked(), "null", "null", null, labo, 7, nome, precoVenda,precoAntigo,  switch_disponibilidade_cadastro_de_produto.isChecked(), tagMap, FORNECEDORES);

        return prodObj;
    }

    private ProdObj atualizarProduto(ProdObj obj) {
        if (uriFotoProduto == null && prod == null) {
            toast("Insira uma foto");
            return null;
        }

        String nome = pegarTexto(et_cadastro_de_produto_nome);

        if (nome == null) {
            toast("Insira um nome para o produto");
            return null;
        }

        String labo = pegarTexto(et_cadastro_de_produto_labo);

        if (labo == null) {
            toast("Insira o fabricante");
            return null;
        }

        String descricao = pegarTexto(et_cadastro_de_produto_descricao);

        if (descricao == null) {
            toast("adicione uma descricao");
            return null;
        }

        String stringPrecoVenda = pegarTexto(et_cadastro_de_produto_preco_consumidor);

        if (stringPrecoVenda == null) {
            toast("Insira o preço de venda");
            return null;
        }
        float precoVenda = Float.valueOf(stringPrecoVenda);

        String stringPrecoAntigo = pegarTexto(et_cadastro_de_produto_preco_antigo);

        if (stringPrecoAntigo == null) {
            toast("Insira o preço de compra");
            return null;
        }
        float precoAntigo = Float.valueOf(stringPrecoAntigo);

        if (tagList.size() < 3) {
            int faltamXTags = 3 - tagList.size();
            toast("Insira mais " + faltamXTags + " Palavra chave (Tag)");
            return null;
        }

        Map<String, Boolean> tagMap = new HashMap<>();
        for (int i = 0; i < tagList.size(); i++) {
            tagMap.put(tagList.get(i), true);
        }

        if (categoriaList.size() < 1) {
            toast("Insira uma categoria ");
            return null;
        }

        Map<String, Boolean> catMap = new HashMap<>();
        for (int i = 0; i < categoriaList.size(); i++) {
            catMap.put(categoriaList.get(i).toLowerCase(), true);
        }

        ProdObj prodObj = new ProdObj(catMap, descricao, switch_disponibilidade_cadastro_de_produto.isChecked(), obj.getIdProduto(), obj.getImgCapa(), null, labo, 7, nome, precoVenda, precoAntigo, switch_disponibilidade_cadastro_de_produto.isChecked(), tagMap, FORNECEDORES);

        return prodObj;
    }

    private void limparFormulario(){
        uriFotoProduto = null;
        imagem_cadastro_de_produto.setImageURI(null);
        et_cadastro_de_produto_nome.setText("");
        et_cadastro_de_produto_nome.requestFocus();
        et_cadastro_de_produto_labo.setText("");
        et_cadastro_de_produto_labo.clearFocus();
        et_cadastro_de_produto_descricao.setText("");
        et_cadastro_de_produto_descricao.clearFocus();
        et_cadastro_de_produto_preco_consumidor.setText("");
        et_cadastro_de_produto_preco_consumidor.clearFocus();
        et_cadastro_de_produto_preco_antigo.setText("");
        et_cadastro_de_produto_preco_antigo.clearFocus();
        etTags.setText("");
        etTags.clearFocus();
        containerTags.setVisibility(View.GONE);
        CATEGORIA = null;
        AdapterCategoriasCadastro adapterCategoriasCadastro = new AdapterCategoriasCadastro(this, this, null);
        recyclerCategorias.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerCategorias.setAdapter(adapterCategoriasCadastro);
        cb_cadastro_de_produto_promocional.setChecked(false);
        loadding.setVisibility(View.GONE);
        bt_cadastro_de_produto_salvar.setVisibility(View.VISIBLE);
        if (prod != null) {
            finish();
        }
    }

    private void toast(String s) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void atualizarListaTags() {

        String str = "";
        int tamanho = tagList.size();

        for (int i = 0; i < tamanho; i++) {
            if (i != (tamanho - 1)) {
                str = str + tagList.get(i) + ", ";
            } else {
                str = str + tagList.get(i);
            }
        }

        if (tamanho > 0) {
            containerTags.setVisibility(View.VISIBLE);
        }

        tvListTags.setText(str);
    }

    private String pegarTexto(TextInputEditText inputEditText) {
        String t = inputEditText.getText().toString();
        if (t.length() > 0) {
            return t;
        } else {
            return null;
        }
    }

    private void limparTags() {
        limparTagsArrayList();
        tvListTags.setText("");
        containerTags.setVisibility(View.GONE);
    }

    private void limparTagsArrayList() {
        tagList.clear();
        tagList = new ArrayList<>();
        categoriaList.clear();
        categoriaList = new ArrayList<>();
    }

    private void exibirFotoEscolhida(Uri uriFotoProduto) {
        tv_adicionar_foto_cadastro_de_produto.setVisibility(View.GONE);
        imagem_cadastro_de_produto.setVisibility(View.VISIBLE);
        imagem_cadastro_de_produto.setImageURI(uriFotoProduto);
    }

    private void escolherFoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GALERIA_CADASTRO_PRODUTO);
        } else {
            Toast.makeText(this, "Não é possivel escolher uma foto atualmente do seu aparelho", Toast.LENGTH_SHORT).show();
        }
    }

    private String getNomeImagem(String s) {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append("_");
        stringBuilder.append(s);
        stringBuilder.append(".jpeg");
        return stringBuilder.toString();
    }

    private void salvarFotoEmStorage(final ProdObj obj) {
        StorageReference child = this.storageReference.child("produto");

        final StorageReference child2 = child.child(getNomeImagem(obj.prodName));
        child2.putFile(uriFotoProduto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return child2.getDownloadUrl();
                }
                throw task.getException();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {

                    salvarDadosEmFirestore(((Uri) task.getResult()).toString(), obj);
                    return;
                }
                Toast.makeText(CadastroDeProdutoActivity.this, "Erro ao salvar Foto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarDadosEmFirestore(String pathFoto, ProdObj obj) {
        DocumentReference documentReference = firestore.collection("produtos").document();
        String prodId = documentReference.getId();
        ProdObj newProd = new ProdObj(obj.getCategorias(), obj.getDescr(), obj.isDisponivel(), prodId, pathFoto, null, obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores());
        documentReference.set(newProd).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                limparFormulario();
//                23:06   23-04
            }
        });
    }

    @Override
    public void mudanca(String id, int pos, boolean adicionar) {

        if (categoriaList == null) {
            categoriaList = new ArrayList<>();
        }

        if (adicionar) {
            if (!categoriaList.contains(String.valueOf(pos))) {
                categoriaList.add(String.valueOf(pos));
            }
        } else {
            if (categoriaList.contains(String.valueOf(pos))) {
                categoriaList.remove(String.valueOf(pos));
            }
        }
    }
}
