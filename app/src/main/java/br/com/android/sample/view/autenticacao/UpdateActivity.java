package br.com.android.sample.view.autenticacao;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.android.sample.R;
import br.com.android.sample.domain.User;

@EActivity(R.layout.activity_update)
public class UpdateActivity extends AppCompatActivity implements ValueEventListener, DatabaseReference.CompletionListener {

    private Toolbar toolbar;
    private User user;
    private AutoCompleteTextView name;


    @AfterViews
    public void onAfterViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        toolbar.setTitle( getResources().getString(R.string.atualiza_profile) );
        name = (AutoCompleteTextView) findViewById(R.id.name);

        user = new User();
        user.setId( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        user.contextDataDB( this );
    }

    public void update( View view ){
        user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid() );
        user.setName( name.getText().toString() );
        user.updateDB( UpdateActivity.this );
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue( User.class );
        name.setText( u.getName() );
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report( firebaseError.toException() );
    }

    @Override
    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {

        if( firebaseError != null ){
            FirebaseCrash.report( firebaseError.toException() );
            Toast.makeText( this, "Falhou: "+firebaseError.getMessage(), Toast.LENGTH_LONG ).show();
        }
        else{
            Toast.makeText( this, "Atualização realizada com sucesso.", Toast.LENGTH_SHORT ).show();
        }
    }
}