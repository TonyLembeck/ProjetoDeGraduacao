package br.com.android.sample.view.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.Calendar;

import br.com.android.sample.R;
import br.com.android.sample.domain.User;
import br.com.android.sample.domain.UserRole;
import br.com.android.sample.infrastructure.repository.RepositoryHelper;
import br.com.android.sample.view.home.HomeActivity_;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

/**
 *
 */
@EActivity(R.layout.authentication_activity)
public class AuthenticationActivity extends AppCompatActivity
{
    /*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    //---------
    //view elements
    //---------
    /**
     *
     */
    @ViewById(R.id.inputEmail)
    @NotEmpty(messageId = R.string.message_enterYourEmail)
    EditText inputEmail;

    @ViewById(R.id.inputPassword)
    @NotEmpty(messageId = R.string.message_enterYourPassword)
    @MinLength(value = 4, messageId = R.string.message_yourPasswordLengthWrong)
    EditText inputPassword;

    @ViewById(R.id.begin)
    Button buttonBegin;

    ProgressDialog progressDialog;

    //---------
    //system services
    //---------
    /**
     *
     */
    @SystemService
    InputMethodManager inputMethodManager;

    //---------
    //delegates
    //---------
    /**
     *
     */
    //private IExampleResource exampleResource = DelegateFactory.get(IExampleResource.class);

    //---------
    //repository
    //---------
    /**
     *
     */
    @OrmLiteDao(helper = RepositoryHelper.class)
    public Dao<User, Long> userRepository;

    /*-------------------------------------------------------------------
	 * 		 					   INITIALIZERS
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @AfterViews
    public void onAfterViews()
    {
    }

	/*-------------------------------------------------------------------
	 * 		 					   BEHAVIORS
	 *-------------------------------------------------------------------*/
    //---------
    // UIThreads
    //---------
    /**
     *
     */
    @UiThread
    @Click(R.id.begin)
    public void onBeginClick()
    {
        if ( !FormValidator.validate(this, new SimpleErrorPopupCallback(this)) ) return;

        //force hide keyboard
        this.inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        this.progressDialog = ProgressDialog.show(this,
                super.getString(R.string.label_loading),
                super.getString(R.string.message_loadingData)
        );

        this.authenticate();
    }

    //---------
    // Background
    //---------
    /**
     *
     * @return
     */
    @Background
    public void authenticate()
    {
        try
        {
            final User user = new User();
            user.setEmail( this.inputEmail.getText().toString() );
            user.setPassword( this.inputPassword.getText().toString() );
            user.setDisabled(false);
            user.setName("Nome de exemplo");
            user.setRole(UserRole.ADMINISTRATOR);
            user.setCreated( Calendar.getInstance() );

            this.userRepository.createOrUpdate( user );

            //open the new activity
            super.startActivity( new Intent(this, HomeActivity_.class) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Snackbar.make(buttonBegin, R.string.message_errorToAuthenticate, Snackbar.LENGTH_LONG).show();
        }

        this.progressDialog.dismiss();
    }
}