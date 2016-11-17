package br.com.android.sample.view.cadastrar;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import br.com.android.sample.R;
import br.com.android.sample.infrastructure.calcular.Calcular;
import br.com.android.sample.view.autenticacao.ComumActivity;


public class MedicaoActivity extends ComumActivity implements SensorEventListener{


    private Camera camera;
    private CameraPreview cameraPreview;
    private Location location;
    private LocationManager locationManager;

    private TextView tvAlturaUsuario, tvDistancia, tvAltitude, tvLatitude, tvLongitude, orientacoesUser;

    private double alturaUsuario, distancia, altura, altitude, latitude, longitude, userAltitude, userLatitude,
            userLongitude, anguloBussula, anguloAccelerometro;

    private static double eixoZOrien;
    private static double eixoZAccel;
    private static double eixoYOrien;

    private View viewAddValores;
    private AlertDialog alertaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvAlturaUsuario = (TextView) findViewById(R.id.altura_usuario);
        tvDistancia = (TextView) findViewById(R.id.distancia);
        tvAltitude = (TextView) findViewById(R.id.altura);
        tvLatitude = (TextView) findViewById(R.id.latitude);
        tvLongitude = (TextView) findViewById(R.id.longitude);
        orientacoesUser = (TextView) findViewById(R.id.orientacoesUser);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null){
                latitude = params.getDouble("latitude");
                longitude = params.getDouble("longitude");
            }
        }

        if(latitude != 0 && longitude != 0) {
            tvLatitude.setText(this.getString(R.string.latitude) + " " + latitude);
            tvLongitude.setText(this.getString(R.string.longitude) + " " + longitude);
        }

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(MedicaoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        orientacoesUser.setText(this.getString(R.string.orientacoes_user_base));
        Carregar();
    }


    public void onCalcularClick(View view){

        if(alturaUsuario == 0){
            showToast(this.getString(R.string.inserir_altura_usuario));
        }else if (distancia == 0) {
            if(eixoZAccel > 0) {
                anguloAccelerometro = eixoZOrien;
                distancia = Calcular.distancia(alturaUsuario, eixoZOrien);
                tvDistancia.setText(this.getString(R.string.distancia) + " " + distancia + " " + this.getString(R.string.unidade_medida));
                orientacoesUser.setText(this.getString(R.string.orientacoes_user_topo));
            } else
                Toast.makeText(this, this.getString(R.string.horizonte), Toast.LENGTH_LONG).show();
        }else {
            tvDistancia.setText(this.getString(R.string.distancia) + " " + distancia + " " + this.getString(R.string.unidade_medida));
            altura = Calcular.altura(alturaUsuario, distancia, eixoZOrien, eixoZAccel);
            if (location == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(this.getString(R.string.gps_nao_responde));
                builder.setNeutralButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                builder.show();
            }else {
                anguloBussula = eixoYOrien + 88;
                if (anguloBussula > 359)
                    anguloBussula = anguloBussula - 360;
                if (altitude == 0){
                        userAltitude = location.getAltitude();
                        altitude = userAltitude + altura;
                        tvAltitude.setText(this.getString(R.string.altitude) + " " + altitude + " " + this.getString(R.string.unidade_medida));
                        orientacoesUser.setText("");
                }
                if (latitude == 0){
                    userLatitude = location.getLatitude();
                    latitude = Calcular.latitude(distancia, eixoYOrien, userLatitude);
                    tvLatitude.setText(this.getString(R.string.latitude) + " " + latitude);
                }
                if (longitude == 0) {
                    userLongitude = location.getLongitude();
                    longitude = Calcular.longitude(distancia, eixoYOrien, userLongitude);
                    tvLongitude.setText(this.getString(R.string.longitude) + " " + longitude);
                }
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        camera.stopPreview();
    }
    @Override
    protected void onResume(){
        super.onResume();
        // Create an instance of Camera
        camera = getCameraInstance();

        //setCameraDisplayOrientation(this, 0, camera);
        cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera);
        preview.addView(cameraPreview);
    }

    public void onCadPontoClick(View view) {
        if (altitude > 0 && distancia > 0) {
            Intent intent = new Intent(MedicaoActivity.this, CadastrarPontoActivity.class);
            intent.putExtra("altura", altura);
            intent.putExtra("altitude", altitude);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("userAltitude", userAltitude);
            intent.putExtra("userLatitude", userLatitude);
            intent.putExtra("userLongitude", userLongitude);
            intent.putExtra("distancia", distancia);
            intent.putExtra("anguloBussula", anguloBussula);
            intent.putExtra("anguloAccelerometro", anguloAccelerometro);
            startActivityForResult(intent, 0);
        } else {
            showToast(this.getString(R.string.calcular_distancia_altitude));
        }
    }

    public void onInserirValoresManualmenteClick(View view) {
        LayoutInflater li = getLayoutInflater();

        //inflamos o layout alerta.xml na view
        viewAddValores = li.inflate(R.layout.add_valores, null);

        ((EditText)viewAddValores.findViewById(R.id.addAltUser)).setText((alturaUsuario==0)? "": " " + alturaUsuario);
        ((EditText)viewAddValores.findViewById(R.id.addDistancia)).setText((distancia==0)? "": " " + distancia);
        ((EditText)viewAddValores.findViewById(R.id.addAltitude)).setText((altitude==0)? "": " " + altitude);
        ((EditText)viewAddValores.findViewById(R.id.addLatitude)).setText((latitude==0)? "": " " + latitude);
        ((EditText)viewAddValores.findViewById(R.id.addLongitude)).setText((longitude==0)? "": " " + longitude);

        //definimos para o botão do layout um clickListener
        viewAddValores.findViewById(R.id.confirmarAddValores).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Double temp;
                temp = getDouble(((TextView)viewAddValores.findViewById(R.id.addAltUser)).getText().toString());
                if (temp > 0){
                    alturaUsuario = temp;
                    tvAlturaUsuario.setText(MedicaoActivity.this.getString(R.string.altura_usuario) + " " + alturaUsuario + " " + MedicaoActivity.this.getString(R.string.unidade_medida));
                }else {
                    alturaUsuario = 0;
                    tvAlturaUsuario.setText(MedicaoActivity.this.getString(R.string.altura_usuario));
                }
                salvar(temp);
                temp = getDouble(((TextView)viewAddValores.findViewById(R.id.addDistancia)).getText().toString());
                if (temp > 0){
                    distancia = temp;
                    tvDistancia.setText(MedicaoActivity.this.getString(R.string.distancia) + " " + distancia + " " + MedicaoActivity.this.getString(R.string.unidade_medida));
                }else {
                    distancia = 0;
                    tvDistancia.setText(MedicaoActivity.this.getString(R.string.distancia));
                    orientacoesUser.setText(MedicaoActivity.this.getString(R.string.orientacoes_user_base));
                }
                temp = getDouble(((TextView)viewAddValores.findViewById(R.id.addAltitude)).getText().toString());
                if (temp > 0){
                    altitude = temp;
                    tvAltitude.setText(MedicaoActivity.this.getString(R.string.altitude) + " " + altitude + " " + MedicaoActivity.this.getString(R.string.unidade_medida));
                }else {
                    altitude = 0;
                    tvAltitude.setText(MedicaoActivity.this.getString(R.string.altitude));
                    if (distancia > 0)
                        orientacoesUser.setText(MedicaoActivity.this.getString(R.string.orientacoes_user_topo));
                }

                temp = getDouble(((TextView)viewAddValores.findViewById(R.id.addLatitude)).getText().toString());
                if (temp > 90 || temp < -90 || temp == 0){
                    latitude = 0;
                    tvLatitude.setText(MedicaoActivity.this.getString(R.string.latitude));
                }else {
                    latitude = temp;
                    tvLatitude.setText(MedicaoActivity.this.getString(R.string.latitude) + " " + latitude + " " + MedicaoActivity.this.getString(R.string.unidade_medida));
                }

                temp = getDouble(((TextView)viewAddValores.findViewById(R.id.addLongitude)).getText().toString());
                if (temp > 180 || temp < -180 || temp == 0){
                    longitude = 0;
                    tvLongitude.setText(MedicaoActivity.this.getString(R.string.latitude));
                }else {
                    longitude = temp;
                    tvLongitude.setText(MedicaoActivity.this.getString(R.string.longitude) + " " + longitude + " " + MedicaoActivity.this.getString(R.string.unidade_medida));
                }
                alertaDialog.dismiss();

            }
        });
        viewAddValores.findViewById(R.id.cancelarAddValores).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alertaDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Informe os dados do ponto!");
        builder.setView(viewAddValores);
        alertaDialog  = builder.create();
        alertaDialog.show();
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
                if (distancia == 0)
                    tvDistancia.setText(this.getString(R.string.distancia) + " " + Calcular.distancia(alturaUsuario, eixoZOrien) + " " + this.getString(R.string.unidade_medida));
                else if(altitude == 0 )
                    tvAltitude.setText(this.getString(R.string.altitude) + " " + (Calcular.altura(alturaUsuario, distancia, eixoZOrien, eixoZAccel)) + " " + this.getString(R.string.unidade_medida));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                eixoZAccel = sensorEvent.values[2];
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private String ObterDiretorio()
    {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.toString();
    }

    private void Carregar() {
        File arq;
        BufferedReader br;
        try {
            arq = new File(ObterDiretorio(), "autura_user");
            br = new BufferedReader(new FileReader(arq));

            alturaUsuario = Double.parseDouble(br.readLine());
            tvAlturaUsuario.setText(MedicaoActivity.this.getString(R.string.altura_usuario) + " " + alturaUsuario + " " + MedicaoActivity.this.getString(R.string.unidade_medida));

        } catch (Exception e) {
        }
    }
    public void salvar(Double temp) {
        File arq;
        byte[] dados;
        try {
            arq = new File(ObterDiretorio(), "autura_user");
            FileOutputStream fos;
            dados = (temp+"").getBytes();

            fos = new FileOutputStream(arq);
            fos.write(dados);
            fos.flush();
            fos.close();
        } catch (Exception e) {
        }
    }

    private double getDouble(String numero){
        try{
            return Double.parseDouble(numero);
        }catch (NumberFormatException e){
            Toast.makeText(this, this.getString(R.string.nao_e_numero), Toast.LENGTH_LONG).show();
        }
        return 0;
    }
}
