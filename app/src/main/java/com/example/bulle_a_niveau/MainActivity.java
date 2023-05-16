package com.example.bulle_a_niveau;

// Importation des bibliothèques
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // Instanciation des variables
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView gyroscopeValues;
    private ImageView bubble;
    private int screenWidth;
    private int screenHeight;

    /**
     * S'exécute lors de la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instanciation des variables
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubble = findViewById(R.id.bubble);
        gyroscopeValues = findViewById(R.id.gyroscopeValues);
        bubble.setZ(0);
        bubble.setY(0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    /**
     * S'exécute quand l'activité est active
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * S'exécute quand l'activité est en pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * Change les valeurs des variables quand le capteur bouge
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float z = event.values[2];
            float y = event.values[1];
            gyroscopeValues.setText("Z: " + z + "\nY: " + y);
            moveBubble(y, z);
        }
    }

    /**
     * Le listener demande cette fonction mais nous ne l'utilisons pas
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * Bouge la bulle en fonction des paramètres passés en variable
     * @param z valeur de l'axe Z du capteur
     * @param y valeur de l'axe Y du capteur
     */
    private void moveBubble(float z, float y) {

        int width = bubble.getWidth();
        int height = bubble.getHeight();

        float newZ = (-z * (screenWidth/2f/10f) + screenWidth/2f - bubble.getWidth()/2f);
        float newY = (-y * (screenHeight/2f/10f) + screenHeight/2f) - bubble.getHeight()/2f;

        if (newZ < 0) {
            newZ = 0;
        } else if (newZ > (getWindowManager().getDefaultDisplay().getWidth() - width)) {
            newZ = getWindowManager().getDefaultDisplay().getWidth() - width;
        }

        if (newY < 0) {
            newY = 0;
        } else if (newY > (getWindowManager().getDefaultDisplay().getHeight() - height)) {
            newY = getWindowManager().getDefaultDisplay().getHeight() - height;
        }

        bubble.setX(newZ);
        bubble.setY(newY);
    }

}