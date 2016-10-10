package br.com.android.sample.view.cadastrar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.android.sample.R;
import br.com.android.sample.infrastructure.calcular.Calcular;


public class MedicaoActivity extends AppCompatActivity implements SensorEventListener{


    private Camera camera;
    private CameraPreview cameraPreview;
    private Location location;
    private LocationManager locationManager;

    TextView tvAlturaUsuario;
    TextView tvDistancia;
    TextView tvAltura;
    TextView tvLatitude;
    TextView tvLongitude;

    private double alturaUsuario;
    private double distancia;
    private double altura;
    private double altitude;
    private double latitude;
    private double longitude;
    private static double eixoZOrien;
    private static double eixoZAccel;
    private static double eixoYOrien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvAlturaUsuario = (TextView) findViewById(R.id.altura_usuario);
        tvDistancia = (TextView) findViewById(R.id.distancia);
        tvAltura = (TextView) findViewById(R.id.altura);
        tvLatitude = (TextView) findViewById(R.id.latitude);
        tvLongitude = (TextView) findViewById(R.id.longitude);


        // Create an instance of Camera
        camera = getCameraInstance();

        //setCameraDisplayOrientation(this, 0, camera);
        cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera);
        preview.addView(cameraPreview);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        alturaUsuario = 1.70;
        tvAlturaUsuario.setText(this.getString(R.string.altura_usuario) + " " + alturaUsuario + " m");

        if (ActivityCompat.checkSelfPermission(MedicaoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "negado", Toast.LENGTH_LONG);

        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Toast.makeText(this, "autorizado", Toast.LENGTH_LONG);
        }

    }


    public void onFabClick(View view){

        if(alturaUsuario == 0){

        }else if (distancia == 0) {
            distancia = Calcular.distancia(alturaUsuario, eixoZOrien);
            tvDistancia.setText(this.getString(R.string.distancia) + " " + distancia + " m");
        }else {
            tvDistancia.setText(this.getString(R.string.distancia) + " " + distancia + " m");
            altura = Calcular.altura(alturaUsuario, distancia, eixoZOrien, eixoZAccel);
            tvAltura.setText(this.getString(R.string.altura) + " " + altura + " m");
            latitude = Calcular.latitude(distancia, eixoYOrien, location.getLatitude());
            tvLatitude.setText(this.getString(R.string.latitude) + " " + latitude);
            longitude = Calcular.longitude(distancia, eixoYOrien, location.getLongitude());
            tvLongitude.setText(this.getString(R.string.longitude) + " " + longitude);
        }

    }


    public void onMenuClick(View view){

    }

    public void onCadPontoClick(View view) {
        Intent intent = new Intent(MedicaoActivity.this, CadastrarPontoActivity.class);
        intent.putExtra("altura", altura);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivityForResult(intent, 0);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                eixoYOrien = sensorEvent.values[0];
                eixoZOrien = sensorEvent.values[2];
                break;
            case Sensor.TYPE_ACCELEROMETER:
                eixoZAccel = sensorEvent.values[2];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
