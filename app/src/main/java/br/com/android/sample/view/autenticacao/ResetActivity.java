package br.com.android.sample.view.autenticacao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import br.com.android.sample.R;

public class ResetActivity extends ComumActivity {

    private Toolbar toolbar;
    private AutoCompleteTextView email;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        toolbar.setTitle( getResources().getString(R.string.reset) );
        progressBar = (ProgressBar) findViewById(R.id.reset_progress);
        email = (AutoCompleteTextView) findViewById(R.id.email);
    }

    public void reset( View view ){
        if (validaFormulario()) {
            firebaseAuth
                    .sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                email.setText("");
                                showSnackbar(ResetActivity.this.getString(R.string.recuperacao_acesso));
                            } else {
                                showSnackbar(ResetActivity.this.getString(R.string.tente_novamente));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report(e);
                        }
                    });
        }
    }

    private boolean validaFormulario(){
        if (email.getText().toString().equals("")){
            showSnackbar( ResetActivity.this.getString(R.string.entre_email) );
            return false;
        }
        return true;
    }
}
