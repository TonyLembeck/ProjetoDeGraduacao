

package br.com.android.sample.view.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.android.sample.R;
import br.com.android.sample.view.mapa.MapsActivity;
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
public class HomeActivity extends AppCompatActivity implements SensorEventListener
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

    Sensor sensor;
    SensorManager sensorManager;
    @ViewById(R.id.acelerometro)
    TextView textView;
    private float s;


    /**
     *
     */
    @AfterViews
    public void onAfterViews() {

       sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }


    private Location location;
    private LocationManager locationManager;

    /**
     *
     */
    @UiThread
    @Click(R.id.buttonAR)
    public void onARClick()
    {

        if (s < 9.0 ) {

            ArActivity.startWithSetup(this, new DefaultARSetup() {

                public void addObjectsTo(GL1Renderer renderer, final World world, GLFactory objectFactory) {

                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                    } else {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }


                    final Location l = new Location("");

                    l.setLatitude(location.getLatitude() - 0.0005);
                    l.setLongitude(location.getLongitude());
                    Obj o = new GeoObj(l);
                    o.setComp(objectFactory.newArrow());
                    world.add(o);
                }

            });
        }else{
           super.startActivity( new Intent(this, MapsActivity.class) );
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        s = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

	/*-------------------------------------------------------------------
	 * 		 					   BEHAVIORS
	 *-------------------------------------------------------------------*/
}
