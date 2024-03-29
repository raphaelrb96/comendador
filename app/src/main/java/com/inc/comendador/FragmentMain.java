package com.inc.comendador;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.inc.comendador.analitycs.AnalitycsFacebook;
import com.inc.comendador.analitycs.AnalitycsGoogle;
import com.inc.comendador.objects.TokenFcm;
import com.inc.comendador.objects.UserStreamView;
import com.inc.comendador.objects.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static android.app.Activity.RESULT_OK;
import static com.inc.comendador.MainActivity.ids;

public class FragmentMain extends Fragment implements AdapterProdutos.ClickProdutoCliente, FacebookCallback<LoginResult> {

    private AnalitycsGoogle analitycsGoogle;
    private AnalitycsFacebook analitycsFacebook;

    private static final int RC_SIGN_IN = 124;
    private static final int RC_SIGN_IN_ADM = 567;

    public static Usuario documentoPrincipalDoUsuario;

    //private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private FirebaseFirestore firestore;

    public static FrameLayout containerFrag;
    public static RecyclerView mListMercadorias;
    public static FragmentManager manager;
    private LinearLayout containerLogin;
    private HorizontalScrollView containerMenu;
    private FirebaseAuth auth;
    private ExtendedFloatingActionButton efabCart;
    private CollectionReference carrinhoDoUsuario;
    private AdapterProdutos mAdapter;
    private CarComprasActivy mComprasActivy;
    private String myUserAtual;
    private int referencia = 0;
    private ArrayList<ProdObj> prodObjs;
    private ArrayList<ProdObj> resultadoPesquisa;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser user = null;
    public static String pathFotoUser = "";
    private ProgressBar pb;
    private ImageButton btPesquisar;
    private TextView tvErro;
    private LinearLayout toolbar;

    private FrameLayout scrol_main;

    private LinearLayout btZap, btSair, btMensagem, btMinhasCompras;
    private ExtendedFloatingActionButton btMeuCarrinho;
    private EditText etpesquisar;

    private View whatsappBt, faceBt, instaBt;

    private int tipoReferencia = 0;
    private Query query;
    public static final boolean ADMINISTRADOR = false;
    private ImageView fundo;

    //private FrameLayout  bt_painel_revendedor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        whatsappBt = (View) view.findViewById(R.id.bt_whats);
        faceBt = (View) view.findViewById(R.id.bt_face);
        instaBt = (View) view.findViewById(R.id.bt_insta);

        scrol_main = (FrameLayout) view.findViewById(R.id.scrol_main);
        //mensagem_main = (FrameLayout) view.findViewById(R.id.mensagem_main);
        //bt_painel_revendedor = (FrameLayout) view.findViewById(R.id.bt_painel_revendedor);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);
        toolbar = (LinearLayout) view.findViewById(R.id.toolbar_main);
        etpesquisar= (EditText) view.findViewById(R.id.et_pesquisar);
        btZap = (LinearLayout) view.findViewById(R.id.ll_bt_zap_menu);
        btSair = (LinearLayout) view.findViewById(R.id.ll_bt_sair);
        btMensagem = (LinearLayout) view.findViewById(R.id.ll_bt_mensagem_menu);
        btMinhasCompras = (LinearLayout) view.findViewById(R.id.ll_bt_minhas_compras_menu);

        btMeuCarrinho = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_meu_carrinho);
        containerMenu = (HorizontalScrollView) view.findViewById(R.id.container_menu);
        btPesquisar = (ImageButton) view.findViewById(R.id.bt_pesquisar);

        //filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);
        pb = (ProgressBar) view.findViewById(R.id.pb_main);
        efabCart = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_meu_carrinho);
        tvErro = (TextView) view.findViewById(R.id.tv_error_main);
        //View btChat = (View) view.findViewById(R.id.bt_abrir_chat);
        //View btcar = (View) view.findViewById(R.id.bt_abrir_carrinho);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        query = firestore.collection("produtos");


        //sheetBehavior.setFitToContents(false);
        //sheetBehavior.setHideable(false);
        //prevents the boottom sheet from completely hiding off the screen

        //initially state to fully expanded

        prodObjs = new ArrayList<>();


        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("237281954777-45kb6q3j06k2mejnef0cbfn3jpf46f43.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        LoginManager.getInstance().registerCallback(callbackManager, FragmentMain.this);

        ligarOuvintes();

        analitycsFacebook = new AnalitycsFacebook(getActivity());
        analitycsGoogle = new AnalitycsGoogle(getActivity());

//        telaInicialLoadding();
//        auth.addAuthStateListener(mAuthStateListener);

        btMeuCarrinho.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getActivity(), MeuPerfilActivity.class));
                return false;
            }
        });


        telaInicialLoadding();
        auth.addAuthStateListener(mAuthStateListener);

        return view;
    }

    private void ligarOuvintes() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                auth = firebaseAuth;
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    onSignedInInitialize();

                    if (user.isAnonymous()) {
                        //efabCart.setVisibility(View.GONE);
                        efabCart.setClickable(false);
                        //toggleBackContainer(false);
                        obterListaDeProdutos(tipoReferencia);
                        return;
                    } else {
                        //toggleBackContainer(true);
                        carregarFotoPerfil();
                        if (!ADMINISTRADOR) {
                            analitycsGoogle.logUserStreamViewEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
                            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
                            firestore.collection("Eventos").document("stream").collection("app").document(user.getUid()).set(userStreamView);
                        }
                        obterListaDeProdutos(tipoReferencia);
                        //getListCart();
                        verificarUsuario();
                        getTokenNoificacoes();
                    }

                } else {

                    //toggleBackContainer(false);
                    ids.clear();
                    ids = new ArrayList();
                    if (isDeviceOnline()) {
                        //TODO 001: TROCAR NA COMPILACAO DE ADM
                        //loginAdmin();
                        //ou
                        //loginUsuarioAnonimo();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginMainActivity.class));
                        return;
                    } else {
                        //exibir interface vazia
                        telaInicialErro("Baixa conexão com a internet");
                    }
                }
            }
        };

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesq = etpesquisar.getText().toString();
                if (pesq.length() > 0) {
                    pesquisar(pesq);
                }
            }
        });

        /*
        mensagem_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMensagens();
            }
        });

        mensagem_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                auth.signOut();
                return false;
            }
        });

        bt_painel_revendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


         */
        efabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = null;

                if (user.isAnonymous()) {
                    showDialog(1, "Faça login para poder ter seu carrinho de compras");
                } else if (!isDeviceOnline()) {
                    showDialog(2, "Baixa conectividade. Verifique sua internet e tente novamente");
                } else if (ids.size() == 0) {
                    showDialog(3, "Seu carrinho está vazio");
                } else {
                    startActivity(new Intent(getActivity(), CartActivity.class));
                }

            }
        });



        faceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/101644948088992";
                String urlPage = "http://www.facebook.com/101644948088992";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    //Log.e(TAG, "Aplicación no instalada.");
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
            }
        });

        instaBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/amacompras.com.br");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/amacompras.com.br")));
                }
            }
        });

        whatsappBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                String urlWhats = "https://api.whatsapp.com/send?phone=+5592991933525";
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlWhats));
                    getActivity().startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                //sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //sendIntent.setType("text/plain");
                //sendIntent.setPackage("com.whatsapp");
                //startActivity(sendIntent);
            }
        });

        /*

        btMinhasCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Faça login para ver suas compras");
                        return;
                    }
                    Intent intent = new Intent(getActivity(), MinhasComprasActivity.class);
                    intent.putExtra("uid", user.getUid());
                    startActivity(intent);
                }
            }
        });


        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Faça login para poder enviar uma mensagem");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MensagemDetalheActivity.class).putExtra("id", user.getUid()));
                }
            }
        });


        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    if (user.isAnonymous()) {
                        showDialog(1, "Você ainda não fez Login");
                        return;
                    }
                    auth.signOut();
                    getActivity().recreate();
                }
            }
        });

*/

        etpesquisar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etpesquisar.setFocusable(true);
                etpesquisar.setFocusableInTouchMode(true);
                return false;
            }
        });

        etpesquisar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String pesq = etpesquisar.getText().toString();
                    if (pesq.length() > 0) {
                        pesquisar(pesq);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void verificarUsuario() {


        DocumentReference usuarioRef = firestore.collection("Usuario").document(auth.getUid());
        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {


                if (userDoc.exists()) {

                    documentoPrincipalDoUsuario = userDoc.toObject(Usuario.class);

                    Log.d("TesteCadastroAfiliados", "VerificarUsuario() chamado, Uid Adm: " + documentoPrincipalDoUsuario.getUidAdm());

                    if (documentoPrincipalDoUsuario != null) {

                        if (documentoPrincipalDoUsuario.getUserName() == null || documentoPrincipalDoUsuario.getUserName().length() == 0) {



                        }

                    }

                } else {

                    getTokenNoificacoes();

                    Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                    startActivity(intent);

                }

            }
        });
    }

    private void checkDadosUsuario(final String tokenAtual) {

        final DocumentReference usuarioRef = firestore.collection("Usuario").document(user.getUid());
        final DocumentReference admRef = firestore.collection("Adm").document(user.getUid());

        usuarioRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDoc) {
                WriteBatch batch = firestore.batch();

                boolean usuarioExiste = false;

                if (ADMINISTRADOR) {
                    TokenFcm tokenFcmAdmin = new TokenFcm(tokenAtual, user.getDisplayName());
                    batch.set(admRef, tokenFcmAdmin);
                }

                if (pathFotoUser.equals("")) {
                    pathFotoUser = getFotoUser(user);
                }

                if (userDoc.exists()) {

                    usuarioExiste = true;

                    boolean mudarFoto = false;
                    boolean mudarToken = false;

                    Usuario usuarioObj = userDoc.toObject(Usuario.class);

                    documentoPrincipalDoUsuario = usuarioObj;

                    if(!pathFotoUser.equals(usuarioObj.getPathFoto())) {
                        mudarFoto = true;
                    }

                    if (!tokenAtual.equals(usuarioObj.getTokenFcm())) {
                        mudarToken = true;
                    }

                    if (mudarFoto && mudarToken) {
                        batch.update(usuarioRef, "tokenFcm", tokenAtual);
                        batch.update(usuarioRef, "pathFoto", pathFotoUser);
                    } else if (!mudarFoto && mudarToken){
                        batch.update(usuarioRef, "tokenFcm", tokenAtual);
                    } else if (!mudarToken && mudarFoto) {
                        batch.update(usuarioRef, "pathFoto", pathFotoUser);
                    }
                } else {

                    usuarioExiste = false;

                    //novo
                    String num = "";
                    String provedor = "Google";
                    if (user.getPhoneNumber() != null) {
                        num = user.getPhoneNumber();
                    }



                    if (user.getProviderId().equals("facebook.com")) {
                        provedor = "Facebook";
                    }
                    long time = System.currentTimeMillis();
                    Usuario noovoUsuario = new Usuario(user.getDisplayName(), user.getEmail(), num, Constantes.CONTROLE_VERSAO_USUARIO, user.getUid(), pathFotoUser, Constantes.USUARIO_TIPO_CLIENTE, provedor, time, time, tokenAtual, null, "", "", "", "", "", false);
                    batch.set(usuarioRef, noovoUsuario);


                }

                final boolean finalUsuarioExiste = usuarioExiste;
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getActivity(), user.getDisplayName() + ", Ok", Toast.LENGTH_LONG).show();

                        if (!finalUsuarioExiste) {
                            Intent intent = new Intent(getActivity(), MeuPerfilActivity.class);
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getTokenNoificacoes() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        checkDadosUsuario(token);

                    }
                });
    }

    private void onSignedInInitialize() {
        carrinhoDoUsuario = FirebaseFirestore.getInstance().collection("carComprasActivy").document("usuario").collection(user.getUid());
    }

    private void abrirMensagens() {
        Intent intent = new Intent(getActivity(), MensagemDetalheActivity.class);
        intent.putExtra("id", auth.getCurrentUser().getUid());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        } else if (requestCode == RC_SIGN_IN_ADM) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                //ADMINISTRADOR = true;
                //abrirMensagem();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        } else {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mAuthStateListener != null) {
            auth.removeAuthStateListener(this.mAuthStateListener);
        }
    }

    private void showDialog(int tipo, String msg) {
        switch (tipo) {
            case 1:
                //R.style.AppCompatAlertDialog
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 2:
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            default:
                break;
        }
    }

    private String getFotoUser (FirebaseUser user) {
        if (user == null) return "";

        for(int xis = 0; xis < user.getProviderData().size(); xis++) {
            try {
                String xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "";

                if (user.getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }
                return xs;
            } catch (NullPointerException e) {
                return "";
            }

        }

        return "";

    }

    private void carregarFotoPerfil() {
        if (user == null) {
            return;
        }
        for(int xis = 0; xis < user.getProviderData().size(); xis++) {
            try {
                String xs = user.getProviderData().get(xis).getPhotoUrl().toString();

                if (user.getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }

                pathFotoUser = xs;
                //Glide.with(getActivity()).load(xs).into(imgPerfil);
                /*
                Glide.with(getActivity())
                        .asBitmap()
                        .load(xs)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                //imgPerfil.setImageBitmap(resource);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {
                                        // Use generated instance
                                        //fundo.setBackground(new ColorDrawable(p.getDarkVibrantSwatch().getRgb()));
                                    }
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                */
            } catch (NullPointerException e) {

            }

        }
    }

    private void toggleFilters(){

    }

    @Override
    public void openDetalhe(ProdObj obj) {
        ProdObjParcelable objParcelable = new ProdObjParcelable(obj.getCategorias(), obj.getDescr(),obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(),obj.getImagens() ,obj.getFabricante(), obj.getNivel(), obj.getProdName(), obj.getProdValor(), obj.getValorAntigo(), obj.isPromocional(), obj.getTag(), obj.getFornecedores(), obj.getQuantidade(), obj.getComissao(), obj.getCores());
        Intent intent = new Intent(getActivity(), ProdutoDetalheActivity.class);
        ArrayList<ProdObjParcelable> relacionados = new ArrayList<>();
        String st = obj.getProdName().substring(0, 3);
        for (int x = 0; x < prodObjs.size(); x++) {
            Log.d("Teste123", String.valueOf(x));
            String nomeProd = prodObjs.get(x).getProdName().toLowerCase();
            boolean palavraIdentica = nomeProd.equals(st.toLowerCase());
            boolean palavraParecida = nomeProd.contains(st.toLowerCase());
            boolean palavraChaveExiste = prodObjs.get(x).getTag().containsKey(st.toLowerCase());

            if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                ProdObj obj1 = prodObjs.get(x);
                ProdObjParcelable objParc = new ProdObjParcelable(obj1.getCategorias(), obj1.getDescr(),obj1.isDisponivel(), obj1.getIdProduto(), obj1.getImgCapa(),obj1.getImagens() ,obj1.getFabricante(), obj1.getNivel(), obj1.getProdName(), obj1.getProdValor(), obj1.getValorAntigo(), obj1.isPromocional(), obj1.getTag(), obj1.getFornecedores(), obj1.getQuantidade(), obj1.getComissao(), obj1.getCores());
                if (obj.getIdProduto() != obj1.getIdProduto()) {
                    relacionados.add(objParc);
                }
                //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                //Log.d("Teste123", resultadoPesquisa.toString());
            }
        }
        intent.putExtra("prod", objParcelable);
        intent.putParcelableArrayListExtra("relacionados" , relacionados);
        startActivity(intent);
    }

    @Override
    public void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj) {
        if (user.isAnonymous()) {
            showDialog(1, "Faça login para poder adicionar algum produto no seu carrinho de compras");
            return;
        }
        String str = prodObj.getIdProduto();
        CarComprasActivy carComprasActivy = new CarComprasActivy(str, prodObj.getProdName(), prodObj.getFabricante(), prodObj.getImgCapa(), prodObj.getProdValor(), 1, ((float) 1) * prodObj.getProdValor());
        DocumentReference reference = carrinhoDoUsuario.document(str);
        if (colorStateList == ColorStateList.valueOf(getActivity().getResources().getColor(R.color.fab1))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findViewById(R.id.fab_produto_item).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
            if (!ids.contains(str)) {
                ids.add(str);
            }
            reference.set(carComprasActivy);
            //mAdapter.notifyItemChanged(i);
            mAdapter.notifyDataSetChanged();

            if (!ADMINISTRADOR) {
                analitycsFacebook.logAdicionarAoCarrinhoEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.prodValor);
                analitycsGoogle.logAdicionarAoCarrinhoEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), prodObj.prodValor);

                analitycsFacebook.logUsuarioAdicionaProdutoAoCartEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), user.getDisplayName(), user.getUid(), user.getEmail(), pathFotoUser, prodObj.prodValor);
                analitycsGoogle.logUsuarioAdicionaProdutoAoCartEvent(prodObj.getProdName(), prodObj.getIdProduto(), 0, prodObj.isPromocional(), prodObj.getImgCapa(), user.getDisplayName(), user.getUid(), user.getEmail(), pathFotoUser, prodObj.prodValor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.fab_produto_item).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab1)));
        }
        if (ids.contains(str)) {
            ids.remove(str);
        }
        reference.delete();
        //mAdapter.notifyItemChanged(i);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void categoria(int categ) {
        obterListaDeProdutos(categ);
    }

    @Override
    public void adm() {
        if (ADMINISTRADOR) {
            startActivity(new Intent(getActivity(), AdmActivity.class));
        } else {
            startActivity(new Intent(getActivity(), MeuPerfilActivity.class));
        }
    }

    @Override
    public void openChat() {

        Intent intent = new Intent(getActivity(), PainelRevendedorActivity.class);
        intent.putExtra("id", user.getUid());
        intent.putExtra("nome", user.getDisplayName());
        intent.putExtra("path", user.getPhotoUrl());
        intent.putExtra("zap", user.getPhoneNumber());
        startActivity(intent);

        /*

        if (user != null) {
            if (user.isAnonymous()) {
                showDialog(1, "Faça login para poder enviar uma mensagem");
                return;
            }
            startActivity(new Intent(getActivity(), MensagemDetalheActivity.class).putExtra("id", user.getUid()));
        }
        */
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException error) {

    }

    private void loginAdmin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_ADM);
    }

    private void loginUsuarioAnonimo() {
        auth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG_Sucess", "signInAnonymously:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void myQuery(Query meuQuery, final boolean isPesquisa, final String sPesquisa, final int tipo) {
        prodObjs.clear();

        if (!isPesquisa) {
            meuQuery = meuQuery.whereEqualTo("disponivel", true);
        }

        meuQuery.limit(200).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int sizeDoc = queryDocumentSnapshots.size();
                if (!queryDocumentSnapshots.isEmpty()) {

                    ArrayList<ProdObj> list = new ArrayList<>();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                        if (prod.isDisponivel()) {
                            prodObjs.add(prod);
                        }
                    }


                    if (isPesquisa) {
                        analitycsFacebook.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), true, sizeDoc + " resultado(s)");
                        analitycsGoogle.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), true);
                    }


                    if (tipo == 0) {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            if (prodObjs.get(y).isPromocional()) {
                                list.add(prodObjs.get(y));
                            }
                        }
                    } else if(tipo == -1) {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            list.add(prodObjs.get(y));
                        }
                    } else {
                        for (int y = 0; y < prodObjs.size(); y++) {
                            if (prodObjs.get(y).getCategorias().containsKey(String.valueOf(tipo))) {
                                list.add(prodObjs.get(y));
                            }
                        }
                    }

                    mAdapter = new AdapterProdutos(FragmentMain.this, getActivity(), list, true);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    mListMercadorias.setLayoutManager(layoutManager);
                    mListMercadorias.setAdapter(mAdapter);
                    telaInicialSucesso();


                    return;
                }



                if (isPesquisa && sizeDoc == 0){
                    Toast.makeText(getActivity(), "Nenhum resultado para sua pesquisa", Toast.LENGTH_LONG).show();
                    if (!ADMINISTRADOR) {
                        analitycsFacebook.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), false, sizeDoc + " resultado(s)");
                        analitycsGoogle.logPesquisaProdutoEvent(sPesquisa, user.getDisplayName(), user.getUid(), false);
                    }
                    obterListaDeProdutos(4);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }

    private void obterListaDeProdutos(int tipo) {

        telaInicialLoadding();
        myQuery(firestore.collection("produtos").whereEqualTo("disponivel", true), false, "", tipo);

    }

    private void esconderTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etpesquisar.getWindowToken(), 0);
    }

    private void pesquisar(final String st) {

        String busca = "tag." + st.toLowerCase();
        telaInicialLoadding();
        esconderTeclado();

        resultadoPesquisa = new ArrayList<>();

        /*
        for (int x = 0; x < prodObjs.size(); x++) {
            Log.d("Teste123", String.valueOf(x));
            String nomeProd = prodObjs.get(x).getProdName().toLowerCase();
            boolean palavraIdentica = nomeProd.equals(st.toLowerCase());
            boolean palavraParecida = nomeProd.contains(st.toLowerCase());
            boolean palavraChaveExiste = prodObjs.get(x).getTag().containsKey(st.toLowerCase());
            if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                resultadoPesquisa.add(prodObjs.get(x));
                //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                //Log.d("Teste123", resultadoPesquisa.toString());
            }
        }



        if (resultadoPesquisa.size() < 1) {

            for (int i = 0; i < prodObjs.size(); i++) {

                final ProdObj obj = prodObjs.get(i);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    obj.getTag().forEach(new BiConsumer<String, Boolean>() {
                        @Override
                        public void accept(String s, Boolean aBoolean) {
                            if (s.equals(st) || st.contains(s) || s.contains(st)) {
                                if (!resultadoPesquisa.contains(obj)) {
                                    resultadoPesquisa.add(obj);
                                }
                            }
                        }
                    });

                }

                if (obj.getProdName().contains(st) || obj.getProdName().equals(st) || st.contains(obj.getProdName())) {
                    if (!resultadoPesquisa.contains(obj)) {
                        resultadoPesquisa.add(obj);
                    }
                }
            }


         */

        if (resultadoPesquisa.size() < 1) {
            myQuery(firestore.collection("produtos").whereEqualTo(busca, true), true, st, -1);
        } else {
            mAdapter = new AdapterProdutos(FragmentMain.this, getActivity(), resultadoPesquisa, true);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mListMercadorias.setLayoutManager(layoutManager);
            mListMercadorias.setAdapter(mAdapter);
            telaInicialSucesso();

        }


    }

    private void getListCart () {
        carrinhoDoUsuario.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                                String id = querySnapshot.getDocuments().get(i).getId();
                                if (!ids.contains(id)) {
                                    ids.add(id);
                                }
                            }

                            //obterListaDeProdutos(tipoReferencia);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
//                            Log.d("TesteLogin", "Goglle sucess");
                            String foto = getFotoUser(user);
                            analitycsFacebook.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid());
                            analitycsGoogle.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
                        } else {
                            Log.d("TesteLogin", "GoogleErro");
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
                            String foto = getFotoUser(user);
                            analitycsFacebook.logLoginFaceEvent(true, user.getDisplayName(),user.getUid());
                            analitycsGoogle.logLoginFaceEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
//                            Log.d("TesteLogin", "Facesucess");
                        } else {
                            updateUI(null);
//                            Log.d("TesteLogin", "Faceerro");
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
//        Timber.d("UpdateUI");
        if (firebaseUser != null) {
//            Log.d("TesteLogin", "UpdateUISucesso");
            //tirar container login e subir a lista de produtos

            carregarFotoPerfil();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isDeviceOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void telaInicialLoadding() {
        mListMercadorias.setVisibility(View.GONE);
        //efabCart.setVisibility(View.GONE);
        efabCart.setClickable(false);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    private void telaInicialErro(String erro) {
        mListMercadorias.setVisibility(View.GONE);
        //efabCart.setVisibility(View.GONE);
        efabCart.setClickable(false);
        tvErro.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvErro.setText(erro);
    }

    private void telaInicialSucesso() {
        mListMercadorias.setVisibility(View.VISIBLE);
        //efabCart.setVisibility(View.VISIBLE);
        efabCart.setClickable(true);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        scrol_main.scrollTo(0,0);
    }



}
