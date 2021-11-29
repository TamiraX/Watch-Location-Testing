package com.example.testgps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    Location lastLocation;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, HelloService.class);
        startService(intent);


        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {


        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        if(locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null){
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
//                    0, mLocationListener);
//        }

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
          //      0, mLocationListener);



    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            TextView textView = findViewById(R.id.Location);
            textView.setText(location.toString());
        }
    };

    public void showLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(lastLocation == null){
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation == null){
                lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        TextView textView = findViewById(R.id.GPS);
        TextView loc = findViewById(R.id.Location);
        // the line below shows "Network Provider: null" on the watch, but works fine on any device with Gapps installed.
        textView.setText("Network Provider: " + String.valueOf(locationManager.getProvider(LocationManager.NETWORK_PROVIDER)));
        if(lastLocation != null){
            // the line below works fine on the watch, as long as gps is available. The moment gps becomes unavailable, no more new locations, as the network provider is nonfunctional.
            loc.setText(lastLocation.toString());
        } else{
            loc.setText("No location found");
        }
    }


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
}