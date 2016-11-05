package br.com.android.sample.view.mapa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.User;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.view.ar.PontoARActivity;
import br.com.android.sample.view.autenticacao.LoginActivity;
import br.com.android.sample.view.cadastrar.MedicaoActivity;
import br.com.android.sample.view.visualizar.VisualizaComentarioActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    private LocationManager locationManager;
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

        firebaseRef = LibraryClass.getFirebase();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.liberar_acesso_gps));
            builder.setNeutralButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            builder.show();
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (location == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.gps_nao_responde));
            builder.setNeutralButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            builder.show();
        }else {
            LatLng latLng;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        }

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
                        mMap.addMarker(new MarkerOptions().position(latLng).title(ponto.getNome()).zIndex(listaPonto.size()+1));
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
                    LatLng latLngTemp;
                    if (location == null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setMessage(MapsActivity.this.getString(R.string.gps_nao_responde));
                        builder.setNeutralButton(MapsActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        builder.show();
                    }else {
                        latLngTemp = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.setMyLocationEnabled(true);
                    }
                    for (int i = 0; i < listaPonto.size(); i++) {
                        Ponto ponto = listaPonto.get(i);
                        latLngTemp = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLngTemp).title(ponto.getNome()).zIndex(i+1));
                    }
                }
                marker = new MarkerOptions().position(latLng).title(MapsActivity.this.getString(R.string.novo_ponto));
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
                    if (location == null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setMessage(MapsActivity.this.getString(R.string.gps_nao_responde));
                        builder.setNeutralButton(MapsActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        builder.show();
                    }else {
                        mMap.setMyLocationEnabled(true);
                    }
                    for (int i = 0; i < listaPonto.size(); i++) {
                        Ponto ponto = (Ponto) listaPonto.get(i);
                        LatLng latLng = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(ponto.getNome()).zIndex(i+1));
                    }
                    latitude = 0;
                    longitude = 0;
                    marker = null;
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getZIndex() > 0) {
                    Intent intent = new Intent(MapsActivity.this, VisualizaComentarioActivity.class);
                    intent.putExtra("idPonto", listaPonto.get((int) marker.getZIndex() - 1).getId());
                    intent.putExtra("nome", listaPonto.get((int) marker.getZIndex() - 1).getNome());
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User user = new User();

        if( user.isSocialNetworkLogged( this ) ){
            getMenuInflater().inflate(R.menu.menu_social_network_logged, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.ar){
            startActivity(new Intent(this, PontoARActivity.class));
        } else if(id == R.id.addPonto){
            Intent intent = new Intent(this, MedicaoActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        } else if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            File cacheDir = getCacheDir();
            deleteDirectory(cacheDir.getParent());
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void deleteDirectory(String path){
        try {
            Runtime.getRuntime().exec(String.format("rm -rf %s", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





