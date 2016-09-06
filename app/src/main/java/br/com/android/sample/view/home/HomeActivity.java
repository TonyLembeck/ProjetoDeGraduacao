package br.com.android.sample.view.home;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.android.sample.R;

/**
 *
 */
@EActivity(R.layout.home_activity)
public class HomeActivity extends AppCompatActivity
{
    /*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    //---------
    //view elements
    //---------

    //---------
    //system services
    //---------

    //---------
    //delegates
    //---------

    //---------
    //repository
    //---------

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
}
