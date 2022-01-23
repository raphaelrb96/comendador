package com.inc.comendador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.inc.comendador.adapter.AdapterAfilidos;
import com.inc.comendador.analitycs.AnalitycsFacebook;
import com.inc.comendador.analitycs.AnalitycsGoogle;
import com.inc.comendador.objects.Usuario;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.inc.comendador.FragmentMain.ADMINISTRADOR;
import static com.inc.comendador.FragmentMain.documentoPrincipalDoUsuario;
import static com.inc.comendador.FragmentMain.pathFotoUser;
import static com.inc.comendador.FragmentMain.user;

public class PainelDeAfiliados extends AppCompatActivity {

    private TextView tv_toolbar_painel_afiliados, tv_info_card_afiliados, bt_add_revendedor;
    private View bt_voltar_painel_afiliados;

    private ProgressBar pb_add_revendedor_afiliado, pb_meus_afiliados;
    private ImageView info_add_revendedor_afiliado;


    private String nickUser;

    private TextInputEditText et_username_add_afiliado;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private TextView numero_de_analize, total_afiliados_autenticados, titulo_meus_revendedores;
    private Query colecaoMeusAfiliados;
    private RecyclerView rv_painel_afiliados;

    private boolean cadastroIniciado = false;
    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_de_afiliados);
        tv_toolbar_painel_afiliados = (TextView) findViewById(R.id.tv_toolbar_painel_afiliados);
        numero_de_analize = (TextView) findViewById(R.id.numero_de_analize);
        titulo_meus_revendedores = (TextView) findViewById(R.id.titulo_meus_revendedores);

        bt_voltar_painel_afiliados = (View) findViewById(R.id.bt_voltar_painel_afiliados);

        rv_painel_afiliados = (RecyclerView) findViewById(R.id.rv_painel_afiliados);

        bt_add_revendedor = (TextView) findViewById(R.id.bt_add_revendedor);
        total_afiliados_autenticados = (TextView) findViewById(R.id.total_afiliados_autenticados);

        et_username_add_afiliado = (TextInputEditText) findViewById(R.id.et_username_add_afiliado);

        pb_add_revendedor_afiliado = (ProgressBar) findViewById(R.id.pb_add_revendedor_afiliado);
        pb_meus_afiliados = (ProgressBar) findViewById(R.id.pb_meus_afiliados);
        info_add_revendedor_afiliado = (ImageView) findViewById(R.id.info_add_revendedor_afiliado);
        tv_info_card_afiliados = (TextView) findViewById(R.id.tv_info_card_afiliados);

        if (documentoPrincipalDoUsuario == null) {
            verificarUsuario(null);
        } else {

            if (documentoPrincipalDoUsuario.getUid() == null || documentoPrincipalDoUsuario.getUid().length() == 0) {
                verificarUsuario(null);
            }
        }

        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);


        nickUser = documentoPrincipalDoUsuario.getUserName();

        Log.d("TesteCadastroAfiliados", "Document Uid Adm: " + documentoPrincipalDoUsuario.getUidAdm());

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        colecaoMeusAfiliados = mFirestore.collection("Usuario").whereEqualTo("uidAdm", mAuth.getCurrentUser().getUid());

        pb_meus_afiliados.setVisibility(View.VISIBLE);
        rv_painel_afiliados.setVisibility(View.GONE);

        colecaoMeusAfiliados.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                pb_meus_afiliados.setVisibility(View.GONE);
                rv_painel_afiliados.setVisibility(View.VISIBLE);

                if (queryDocumentSnapshots != null) {

                    ArrayList<Usuario> meusAfiliados = new ArrayList<>();

                    int numAutenticados = 0;
                    int numEmAnalise = 0;

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                        Usuario umAfiliado = queryDocumentSnapshots.getDocuments().get(i).toObject(Usuario.class);
                        if (umAfiliado.isAdmConfirmado()) {
                            numAutenticados++;
                        } else {
                            numEmAnalise++;
                        }
                        meusAfiliados.add(umAfiliado);
                    }

                    total_afiliados_autenticados.setText(String.valueOf(numAutenticados));
                    numero_de_analize.setText(String.valueOf(numEmAnalise));

                    if (meusAfiliados.size() > 0) {
                        titulo_meus_revendedores.setVisibility(View.VISIBLE);
                    } else {
                        titulo_meus_revendedores.setVisibility(View.GONE);
                    }

                    AdapterAfilidos adapterAfilidos = new AdapterAfilidos(meusAfiliados,PainelDeAfiliados.this);
                    rv_painel_afiliados.setLayoutManager(new LinearLayoutManager(PainelDeAfiliados.this));
                    rv_painel_afiliados.setAdapter(adapterAfilidos);

                } else {
                    //nenhum afiliado ainda
                }
            }
        });



        tv_toolbar_painel_afiliados.setText("@" + nickUser);
        bt_voltar_painel_afiliados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bt_add_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cadastroIniciado) {
                    return;
                }

                String apelido = et_username_add_afiliado.getText().toString().toLowerCase();

                et_username_add_afiliado.setText(apelido);

                cadastroIniciado = true;


                if (apelido.length() > 0) {

                    if (documentoPrincipalDoUsuario == null) {
                        verificarUsuario(apelido);
                        return;
                    } else {

                        if (documentoPrincipalDoUsuario.getUid() == null || documentoPrincipalDoUsuario.getUid().length() == 0) {
                            verificarUsuario(apelido);
                            return;
                        }
                    }

                    verificarApelido(apelido);
                } else {
                    tv_info_card_afiliados.setText("Insira um apelido válido!");
                }
            }
        });

        infoPadraoRevendedor();

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!ADMINISTRADOR) {

            analitycsFacebook.visitaAoPainelAfiliados(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.visitaAoPainelAfiliados(user.getDisplayName(), user.getUid(), pathFotoUser);

        }

    }

    private void verificarUsuario(final String apelido) {
        DocumentReference usuarioRef = mFirestore.collection("Usuario").document(mAuth.getUid());

        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {


                if (userDoc.exists()) {

                    Usuario usuarioObj = userDoc.toObject(Usuario.class);

                    documentoPrincipalDoUsuario = usuarioObj;

                    nickUser = documentoPrincipalDoUsuario.getUserName();

                    if (apelido != null) {
                        verificarApelido(apelido);
                    }

                } else {

                    cadastroIniciado = false;

                    tv_info_card_afiliados.setText("Nenhum usuario encontrado");
                    info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                    pb_add_revendedor_afiliado.setVisibility(View.GONE);

                    et_username_add_afiliado.setText("");

                }

            }
        });
    }

    private void verificarApelido(String apelido) {
        info_add_revendedor_afiliado.setVisibility(View.GONE);
        pb_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        addRevendedorLoading();

        mFirestore.collection("Usuario").whereEqualTo("userName", apelido).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        tv_info_card_afiliados.setText("Registrando afiliação");

                        Usuario usuario = queryDocumentSnapshots.getDocuments().get(0).toObject(Usuario.class);


                        if (usuario.getUid().equals(documentoPrincipalDoUsuario.getUid())) {
                            tv_info_card_afiliados.setText("Seu proprio apelido não é válido!");
                            info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                            pb_add_revendedor_afiliado.setVisibility(View.GONE);
                            cadastroIniciado = false;
                        } else {

                            if (usuario.isAdmConfirmado()) {
                                tv_info_card_afiliados.setText("Esse usuario ja possui um ADM");
                                cadastroIniciado = false;
                                return;
                            }

                            Usuario atualizado = new Usuario(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getControleDeVersao(), usuario.getUid(), usuario.getPathFoto(), usuario.getTipoDeUsuario(), usuario.getProvedor(), usuario.getUltimoLogin(), usuario.getPrimeiroLogin(), usuario.getTokenFcm(), usuario.getEndereco(), usuario.getUserName(), mAuth.getUid(), nickUser, mAuth.getCurrentUser().getDisplayName(), pathFotoUser, false);

                            Log.d("TesteCadastroAfiliados", "Uid Adm: " + atualizado.getUidAdm());
                            Log.d("TesteCadastroAfiliados", "Apelido Adm: " + atualizado.getUsernameAdm());

                            queryDocumentSnapshots.getDocuments().get(0).getReference().set(atualizado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    revendedorAdicionado();
                                    et_username_add_afiliado.setText("");
                                    cadastroIniciado = false;
                                }
                            });

                        }

                    } else {

                        tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                        pb_add_revendedor_afiliado.setVisibility(View.GONE);
                        cadastroIniciado = false;

                    }

                } else {
                    //apelido nao encontrado
                    tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                    info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                    pb_add_revendedor_afiliado.setVisibility(View.GONE);
                    cadastroIniciado = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tv_info_card_afiliados.setText("Nenhum usuario encontrado com esse apelido!");
                info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
                pb_add_revendedor_afiliado.setVisibility(View.GONE);
                cadastroIniciado = false;
            }
        });
    }

    private void addRevendedorLoading () {
        tv_info_card_afiliados.setText("Aguarde, estamos validando sua solicitação");
        info_add_revendedor_afiliado.setVisibility(View.GONE);
        pb_add_revendedor_afiliado.setVisibility(View.VISIBLE);
    }

    private void revendedorAdicionado () {
        tv_info_card_afiliados.setText("Solicitação enviada com sucesso, aguarde a confirmação do revendedor");
        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        pb_add_revendedor_afiliado.setVisibility(View.GONE);
        et_username_add_afiliado.setText("");
    }

    private void infoPadraoRevendedor () {
        tv_info_card_afiliados.setText("Insira o apelido do revendedor que você recrutou");
        info_add_revendedor_afiliado.setVisibility(View.VISIBLE);
        pb_add_revendedor_afiliado.setVisibility(View.GONE);
    }

}