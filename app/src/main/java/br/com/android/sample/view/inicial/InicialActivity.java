package br.com.android.sample.view.inicial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import br.com.android.sample.R;
import br.com.android.sample.view.authentication.AuthenticationActivity_;

@EActivity(R.layout.inicial_activity)
public class InicialActivity extends AppCompatActivity {

    @UiThread
    @Click(R.id.login)
    public void onLoginClick(){
        super.startActivity( new Intent(this, AuthenticationActivity_.class) );
    }
}
