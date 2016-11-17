package br.com.android.sample.view.ar;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.plugin.radar.RadarView;
import com.beyondar.android.plugin.radar.RadarWorldPlugin;
import com.beyondar.android.util.ImageUtils;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.BeyondarObjectList;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.util.LibraryClass;
import br.com.android.sample.view.visualizar.VisualizaComentarioActivity;

public class PontoARActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener,
        OnClickBeyondarObjectListener {

    private BeyondarFragmentSupport mBeyondarFragment;
    private RadarView mRadarView;
    private RadarWorldPlugin mRadarPlugin;
    private World mWorld;
    private Location location;
    private LocationManager locationManager;
    private static final String TMP_IMAGE_PREFIX = "viewImage_";
    private static DatabaseReference firebaseRef;
    protected static ArrayList<Ponto> pontos = new ArrayList<>();
    private static long l = 0;
    public static final int LIST_TYPE_EXAMPLE_1 = 1;

    private SeekBar mSeekBarPushAwayDistance;
    private TextView mMinFarText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // The first thing that we do is to remove all the generated temporal
        // images. Remember that the application needs external storage write
        // permission.
        cleanTempFolder();

        setContentView(R.layout.activity_ponto_ar);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        if (ActivityCompat.checkSelfPermission(PontoARActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        mWorld = new World(this);

        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        mWorld.setDefaultImage(R.mipmap.logo_fundo_azul);

        // User position (you can change it using the GPS listeners form Android
        // API)
        firebaseRef = LibraryClass.getFirebase();
        firebaseRef = firebaseRef.child("pontos");

        if (location == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.gps_nao_responde));
            builder.setNeutralButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            builder.show();
        }else {
            mWorld.setGeoPosition(location.getLatitude(), location.getLongitude());
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
                            if (ponto != null) {
                                double nLatitude = ponto.getLatitude() - location.getLatitude();
                                double nLongitude = ponto.getLongitude() - location.getLongitude();
                                if (nLatitude > -0.01833 && nLatitude < 0.01833 && nLongitude > -0.01833 &&
                                        nLongitude < 0.01833 && !ponto.getNome().equals("pedra") &&
                                        !ponto.getNome().equals("catedral") && !ponto.getNome().equals("chico")) {
                                    pontos.add(ponto);
                                    GeoObject go = new GeoObject(l);
                                    go.setGeoPosition(location.getLatitude() + (nLatitude * 0.01), location.getLongitude() + (nLongitude * 0.01));
                                    //go.setImageResource(R.mipmap.logo_fundo_azul);
                                    go.setName(ponto.getNome());
                                    l++;
                                    mWorld.addBeyondarObject(go);
                                    replaceImagesByStaticViews(mWorld);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });

        }
        // set listener for the geoObjects
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);

        mMinFarText = (TextView) findViewById(R.id.textBarMin);
        mSeekBarPushAwayDistance = (SeekBar) findViewById(R.id.seekBarMin);
        mRadarView = (RadarView) findViewById(R.id.radarView);

        // Create the Radar plugin
        mRadarPlugin = new RadarWorldPlugin(this);
        // set the radar view in to our radar plugin
        mRadarPlugin.setRadarView(mRadarView);
        // Set how far (in meters) we want to display in the view
        mRadarPlugin.setMaxDistance(200);

        // We can customize the color of the items
        mRadarPlugin.setListColor(LIST_TYPE_EXAMPLE_1, Color.RED);
        // and also the size
        mRadarPlugin.setListDotRadius(LIST_TYPE_EXAMPLE_1, 3);

        // We create the world and fill it ...
        // .. and send it to the fragment
        mBeyondarFragment.setWorld(mWorld);

        // add the plugin
        mWorld.addPlugin(mRadarPlugin);

        // We also can see the Frames per seconds
       // mBeyondarFragment.showFPS(true);

        mSeekBarPushAwayDistance.setOnSeekBarChangeListener(this);
        mSeekBarPushAwayDistance.setMax(200);
        mSeekBarPushAwayDistance.setProgress(100);
    }

    public void replaceImagesByStaticViews(World world) {
        String path = getTmpPath();

        for (BeyondarObjectList beyondarList : world.getBeyondarObjectLists()) {
            for (BeyondarObject beyondarObject : beyondarList) {
                // First let's get the view, inflate it and change some stuff
                View view = getLayoutInflater().inflate(R.layout.static_beyondar_object_view, null);
                TextView textView = (TextView) view.findViewById(R.id.geoObjectName);
                textView.setText("  " + beyondarObject.getName() + "  \n");
                try {
                    // Now that we have it we need to store this view in the
                    // storage in order to allow the framework to load it when
                    // it will be need it
                    String imageName = TMP_IMAGE_PREFIX + beyondarObject.getName() + ".png";
                    ImageUtils.storeView(view, path, imageName);

                    // If there are no errors we can tell the object to use the
                    // view that we just stored
                    beyondarObject.setImageUri(path + imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get the path to store temporally the images. Remember that you need to
     * set WRITE_EXTERNAL_STORAGE permission in your manifest in order to
     * write/read the storage
     */
    private String getTmpPath() {
        return getExternalFilesDir(null).getAbsoluteFile() + "/tmp/";
    }

    /** Clean all the generated files */
    private void cleanTempFolder() {
        File tmpFolder = new File(getTmpPath());
        if (tmpFolder.isDirectory()) {
            String[] children = tmpFolder.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].startsWith(TMP_IMAGE_PREFIX)) {
                    new File(tmpFolder, children[i]).delete();
                }
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mRadarPlugin == null)
            return;
        if (seekBar == mSeekBarPushAwayDistance) {
            mBeyondarFragment.setPushAwayDistance(progress);
            mMinFarText.setText(this.getString(R.string.distancia) + " " + progress);

        }
    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            Intent intent = new Intent(this, VisualizaComentarioActivity.class);
            intent.putExtra("idPonto", pontos.get((int)beyondarObjects.get(0).getId()).getId());
            intent.putExtra("nome", pontos.get((int)beyondarObjects.get(0).getId()).getNome());

            startActivity(intent);
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}