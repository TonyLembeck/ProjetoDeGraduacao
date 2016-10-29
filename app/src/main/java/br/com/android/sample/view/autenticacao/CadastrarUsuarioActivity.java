package br.com.android.sample.view.autenticacao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.android.sample.R;
import br.com.android.sample.infrastructure.mask.Mask;
import br.com.android.sample.domain.User;

public class CadastrarUsuarioActivity extends ComumActivity implements DatabaseReference.CompletionListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User user;
    private AutoCompleteTextView nome;
    private EditText data;
    protected EditText confirmarSenha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if( firebaseUser == null || user.getId() != null ){
                    return;
                }

                user.setId( firebaseUser.getUid() );
                user.saveDB( CadastrarUsuarioActivity.this );
            }
        };
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthStateListener != null ){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    protected void initViews(){
        nome = (AutoCompleteTextView) findViewById(R.id.nome);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        data = (EditText) findViewById(R.id.dataDeNascimento);
        senha = (EditText) findViewById(R.id.senhaCadastro);
        confirmarSenha = (EditText) findViewById(R.id.confirmaSenha);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        data.addTextChangedListener(Mask.insert("##/##/####", data));
    }

    protected void initUser(){
        user = new User();
        user.setName( nome.getText().toString() );
        user.setEmail( email.getText().toString() );
        user.setData( data.getText().toString() );
        user.setPassword( senha.getText().toString() );
    }

    public void sendSignUpData( View view ){
        if (validaFormulario()) {
            openProgressBar();
            initUser();
            saveUser();
        }
    }

    private void saveUser(){

        mAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( !task.isSuccessful() ){
                    closeProgressBar();
                }
            }
        })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                showSnackbar( e.getMessage() );
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();

        showToast( CadastrarUsuarioActivity.this.getString(R.string.conta_criada_sucesso) );
        closeProgressBar();
        finish();
    }

    private boolean validaFormulario(){
        if (nome.getText().toString().equals("")){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.entre_nome) );
            return false;
        }
        if (email.getText().toString().equals("")){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.entre_email) );
            return false;
        }
        if (data.getText().toString().equals("")){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.entre_data_nasc) );
            return false;
        }
        if (senha.getText().toString().equals("")){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.entre_senha) );
            return false;
        }
        if (confirmarSenha.getText().toString().equals("")){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.entre_conf_senha) );
            return false;
        }
        if (!senha.getText().toString().equals(confirmarSenha.getText().toString())){
            showSnackbar( CadastrarUsuarioActivity.this.getString(R.string.senha_nao_conf) );
            return false;
        }

        return true;
    }
}