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
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private final int GPS_REQUEST_CODE = 99;

    private SettingsClient settingsClient;

    private LocationSettingsRequest locationSettingsRequest;

    private LocationManager locationManager;

    private GoogleMap googleMap;

    private ArrayList<LatLng> latLongArrayList;

    private AppCompatButton btnPolylineGenerate, btnPolylineClear;

    private Polyline lastPolyline;

    private ArrayList<Polyline> polylineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        init();

    }

    private void init() {

        polylineList = new ArrayList<>();

        btnPolylineGenerate = findViewById(R.id.btnPolylineGenerate);

        btnPolylineClear = findViewById(R.id.btnPolylineClear);

        btnPolylineGenerate.setVisibility(View.GONE);

        btnPolylineClear.setVisibility(View.GONE);

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

                    this.googleMap = googleMap;

                    googleMap.setMyLocationEnabled(true);

                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

                    if (location != null) {

                        setOnClickListener();

                        latLongArrayList = new ArrayList<>();

                        LatLng myLatLong = new LatLng(location.getLatitude(), location.getLongitude());

                        googleMap.addMarker(new MarkerOptions().position(myLatLong).title("My Location"));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLong));

                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                        googleMap.setOnMapClickListener(latLng -> latLongArrayList.add(latLng));

                    } else {

                        Toast.makeText(getApplicationContext(), "Location not found...", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception exception) {

                    exception.printStackTrace();

                }

            });

        }

    }

    private void setOnClickListener() {

        btnPolylineGenerate.setVisibility(View.VISIBLE);

        btnPolylineClear.setVisibility(View.VISIBLE);

        btnPolylineGenerate.setOnClickListener(v -> {

            if (googleMap != null && latLongArrayList != null) {

                PolylineOptions polylineOptions = new PolylineOptions().clickable(true);

                if (latLongArrayList.size() > 2) {

                    for (int i = 0; i < latLongArrayList.size(); i++) {

                        polylineOptions.add(latLongArrayList.get(i));

                        if (i == latLongArrayList.size() - 1) {

                            polylineOptions.add(latLongArrayList.get(0));

                        }

                    }

                    lastPolyline = googleMap.addPolyline(polylineOptions);

                    polylineList.add(lastPolyline);

                    latLongArrayList.clear();

                } else {

                    Toast.makeText(getApplicationContext(), "Please, pick more than 3points...", Toast.LENGTH_SHORT).show();

                }

            }

        });

        btnPolylineClear.setOnClickListener(v -> {

            for (int i = 0; i < polylineList.size(); i++) {

                polylineList.get(i).remove();

            }

            polylineList.clear();

        });

    }

}