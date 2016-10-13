package br.com.android.sample.view.mapa;

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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.view.ar.PontoARActivity;
import br.com.android.sample.view.cadastrar.MedicaoActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
    private Sensor sensor;
    private SensorManager sensorManager;
    private SupportMapFragment mapFragment;
    private TextView textView;
    private DatabaseReference firebaseRef;
    private ArrayList<Ponto> listaPonto = new ArrayList<Ponto>();
    private MarkerOptions marker = null;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView) findViewById(R.id.acelerometro);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        firebaseRef = LibraryClass.getFirebase();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {



        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

    }

    public void onAddPontoClick(View view){
        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              .setAction("Action", null).show();*/
        Intent intent = new Intent(MapsActivity.this, MedicaoActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLng;
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Localizacao"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));



        firebaseRef = firebaseRef.child("pontos");


        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ref = dataSnapshot.getKey();
                DatabaseReference novaRef;
                novaRef = firebaseRef.child(ref);

                novaRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Ponto ponto = dataSnapshot.getValue(Ponto.class);
                        LatLng latLng = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(ponto.getNome()));
                        listaPonto.add(ponto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}

        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (marker != null) {
                    mMap.clear();
                    for (int i = 0; i < listaPonto.size(); i++) {
                        Ponto ponto = (Ponto)listaPonto.get(i);
                        LatLng latLngTemp = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLngTemp).title(ponto.getNome()));
                    }
                }
                marker = new MarkerOptions().position(latLng).title("Novo ponto");
                mMap.addMarker(marker);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (marker != null) {
                    mMap.clear();
                    for (int i = 0; i < listaPonto.size(); i++) {
                        Ponto ponto = (Ponto) listaPonto.get(i);
                        LatLng latLng = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(ponto.getNome()));
                    }
                    latitude = 0;
                    longitude = 0;
                    marker = null;
                }
            }
        });


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
       /* if (sensorEvent.values[2] < 7.5 && controleDeEstados) {
            controleDeEstados = false;
            // Limpa a pilha de activity
            Intent intent = new Intent(MapsActivity.this, HomeActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onPause(){
        super.onPause();
        //controleDeEstados = false;
        //finish();
    }

    public void onARClick(View view)
    {
        startActivity(new Intent(this, PontoARActivity.class));
    }

}





