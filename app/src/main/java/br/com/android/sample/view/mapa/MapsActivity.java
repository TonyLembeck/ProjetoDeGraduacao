package br.com.android.sample.view.mapa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.android.sample.R;
import br.com.android.sample.view.home.HomeActivity;
import geo.GeoObj;
import gl.GL1Renderer;
import gl.GLFactory;
import system.ArActivity;
import system.DefaultARSetup;
import worldData.Obj;
import worldData.World;

@EActivity(R.layout.activity_maps)
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;

    @AfterViews
    public void onAfterViews()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        sydney = new LatLng(-25.462946, -54.589164);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Fox do Iguaçu"));

        sydney = new LatLng(1.603068, 26.269743);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Africa"));

        sydney = new LatLng(50.396013, 9.845035);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Alemanha"));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[2] < 9.0)
            ArActivity.startWithSetup(this, new DefaultARSetup() {

                public void addObjectsTo(GL1Renderer renderer, final World world, GLFactory objectFactory) {

                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
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
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
