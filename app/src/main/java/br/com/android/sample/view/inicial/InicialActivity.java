package br.com.android.sample.view.inicial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import br.com.android.sample.R;
import br.com.android.sample.view.autenticacao.LoginActivity;
import br.com.android.sample.view.autenticacao.CadastrarUsuarioActivity;

@EActivity(R.layout.inicial_activity)
public class InicialActivity extends AppCompatActivity {

    @UiThread
    @Click(R.id.btnLogin)
    public void onLoginClick(){
        super.startActivity( new Intent(this, LoginActivity.class) );
    }

    @UiThread
    @Click(R.id.cadastrarNovaConta)
    public void onCadastrarNovaContaClick(){
        super.startActivity( new Intent(this, CadastrarUsuarioActivity.class) );
    }
}
