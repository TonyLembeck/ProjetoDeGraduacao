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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.android.sample.R;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.view.cadastrar.MedicaoActivity;
import geo.GeoObj;
import gl.GL1Renderer;
import gl.GLFactory;
import system.ArActivity;
import system.DefaultARSetup;
import worldData.Obj;
import worldData.World;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
    private boolean controleDeEstados = true;
    private Sensor sensor;
    private SensorManager sensorManager;
    private SupportMapFragment mapFragment;
    private TextView textView;
    private DatabaseReference firebaseRef;
    private ArrayList<LatLng> listaLatLng;


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
        listaLatLng = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        firebaseRef = firebaseRef.child("Ponto");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<String> pontoRef = dataSnapshot.getValue(ArrayList.class);
                //String pontoRef = dataSnapshot.getValue(String.class);

                //for(int i = 0; i<5; i++)
                    //Toast.makeText(MapsActivity.this, pontoRef, Toast.LENGTH_LONG).show();

                //LatLng latLng = new LatLng(pontoRef.get("latitude"), pontoRef.get("longitude"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onAddPontoClick(View view){
        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              .setAction("Action", null).show();*/
        startActivity(new Intent(MapsActivity.this, MedicaoActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng latLng;
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Minha Localizacao"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        latLng = new LatLng(-25.462307339058595, -54.58712375590928);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 1"));
        latLng = new LatLng(-25.46140837041217, -54.58717086406893);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 2"));
        latLng = new LatLng(-25.46050775717279, -54.58715516772786);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 3"));

        latLng = new LatLng(-25.46322283590929, -54.58620825905759);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 1"));
        latLng = new LatLng(-25.463238541818576, -54.58530846811519);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 2"));
        latLng = new LatLng(-25.463254247727864, -54.58440867717278);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nova Localizacao 3"));

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
/*
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle par = intent.getExtras();
            if (par != null) {
                setListaMarkers(par.getDouble("Latitude"), par.getDouble("Longitude"));
                Toast.makeText(this, "Latitude :" + par.getDouble("Latitude") + " Longitude: " + par.getDouble("Longitude"), Toast.LENGTH_LONG);
            }
            Toast.makeText(this, "Teste", Toast.LENGTH_LONG);


            LatLng latLng;
            for (int i = 0; i < listaMarkers.size(); i++) {
                latLng = new LatLng(listaMarkers.get(i)[0], listaMarkers.get(i)[1]);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker no Brasil"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            //Toast.makeText(this, "Latitude :" + listaMarkers.get(0)[0] + " Longitude: " + listaMarkers.get(0)[1], Toast.LENGTH_LONG);
        }
    }
*/
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPause(){
        super.onPause();
        controleDeEstados = false;
        //finish();
    }

    public void onARClick(View view)
    {

        ArActivity.startWithSetup(this, new DefaultARSetup() {

            public void addObjectsTo(GL1Renderer renderer, final World world, GLFactory objectFactory) {

                final Location l = new Location("");

                l.setLatitude(location.getLatitude() - 0.0005);
                l.setLongitude(location.getLongitude());
                Obj o = new GeoObj(l);
                o.setComp(objectFactory.newArrow());
                world.add(o);

            }

        });
    }

}





