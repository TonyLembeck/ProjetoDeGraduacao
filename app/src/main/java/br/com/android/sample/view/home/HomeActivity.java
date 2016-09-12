package br.com.android.sample.view.home;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import br.com.android.sample.R;
import geo.GeoObj;
import gl.GL1Renderer;
import gl.GLFactory;
import system.ArActivity;
import system.DefaultARSetup;
import worldData.Obj;
import worldData.World;

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

    /**
     *
     */
    @UiThread
    @Click(R.id.buttonAR)
    public void onARClick()
    {
        ArActivity.startWithSetup(this, new DefaultARSetup() {

            public void addObjectsTo(GL1Renderer renderer, final World world, GLFactory objectFactory) {

                final Location l = new Location("");

                l.setLatitude(-25.520911);
                l.setLongitude(-54.581084);
                Obj o = new GeoObj(l);
                o.setComp(objectFactory.newArrow());
                world.add(o);
            }
        });
    }

	/*-------------------------------------------------------------------
	 * 		 					   BEHAVIORS
	 *-------------------------------------------------------------------*/
}
