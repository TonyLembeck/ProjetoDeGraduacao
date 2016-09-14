package br.com.android.sample.view.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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

    private Location location;
    private LocationManager locationManager;

    @UiThread
    @Click(R.id.buttonAR)
    public void onARClick()
    {
        ArActivity.startWithSetup(this, new DefaultARSetup() {

            public void addObjectsTo(GL1Renderer renderer, final World world, GLFactory objectFactory) {

                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                }else {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }


                final Location l = new Location("");

                l.setLatitude(location.getLatitude() -0.025911);
                l.setLongitude(location.getLongitude());
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
