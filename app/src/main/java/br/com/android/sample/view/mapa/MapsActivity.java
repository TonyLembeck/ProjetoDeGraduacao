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
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.android.sample.R;
import br.com.android.sample.view.home.HomeActivity;
import br.com.android.sample.view.home.HomeActivity_;
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
    private SupportMapFragment mapFragment;
    private Location location;
    private LocationManager locationManager;

    @ViewById(R.id.fab)
    Button buttonFag;

    @AfterViews
    public void onAfterViews()
    {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);


    }

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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent();
                return false;
            }
        });

        
    }

    @UiThread
    @Click(R.id.fab)
    public void onFabClick(){
        startActivity(new Intent(this, HomeActivity_.class));
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //if (sensorEvent.values[2] < 8.0)
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
