package com.example.fresher.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.fresher.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private final int GPS_REQUEST_CODE = 99;

    private SettingsClient settingsClient;

    private LocationSettingsRequest locationSettingsRequest;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        init();

    }

    private void init() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        settingsClient = LocationServices.getSettingsClient(getApplicationContext());

        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setInterval(60000);

        locationRequest.setFastestInterval(10000);

        locationRequest.setSmallestDisplacement(10);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        locationSettingsRequest = builder.build();

        checkPermission();

    }

    private void showDialogGPS() {

        settingsClient.checkLocationSettings(locationSettingsRequest)

                .addOnCompleteListener(task -> loadMap())

                .addOnFailureListener(e -> {

                    try {

                        int statusCode = ((ApiException) e).getStatusCode();

                        switch (statusCode) {

                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;

                                resolvableApiException.startResolutionForResult(this, GPS_REQUEST_CODE);

                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";

                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();

                                break;

                        }

                    } catch (Exception exception) {

                        exception.printStackTrace();

                    }

                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                loadMap();

            }

        }

    }

    private final ActivityResultLauncher<String[]> locationActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                if (result != null && result.size() > 0 && result.get(Manifest.permission.ACCESS_FINE_LOCATION)) {

                    if (checkGPS()) {

                        loadMap();

                    } else {

                        showDialogGPS();

                    }

                }

            });

    private void checkPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

            locationActivityResultLauncher.launch(permissions);

        } else if (!checkGPS()) {

            showDialogGPS();

        } else {

            loadMap();

        }

    }

    private boolean checkGPS() {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @SuppressLint("MissingPermission")
    private void loadMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {

            mapFragment.getMapAsync(googleMap -> {

                try {

                    googleMap.setMyLocationEnabled(true);

                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

                    if (location != null) {

                        LatLng myLatLong = new LatLng(location.getLatitude(), location.getLongitude());

                        googleMap.addMarker(new MarkerOptions().position(myLatLong).title("My Location"));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLong));

                    } else {

                        Toast.makeText(getApplicationContext(), "Location not found...", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception exception) {

                    exception.printStackTrace();

                }

            });

        }

    }

}