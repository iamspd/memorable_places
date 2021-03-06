package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.memorableplaces.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(getApplicationContext(), "Please allow all the permissions!", Toast.LENGTH_SHORT).show();
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your location");
            }
        }
    }

    public void centerMapOnLocation(Location location, String markerTitle) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();

        if(!markerTitle.equals("Your location")){
            mMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        Intent mainIntent = getIntent();
        int position = mainIntent.getIntExtra("placesNumber", 0);

        if (position == 0) {
            // bring user to their location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            listener = location -> centerMapOnLocation(location, "Your location");

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your location");
            }
        } else {

            Location locationOnList = new Location(LocationManager.GPS_PROVIDER);
            locationOnList.setLatitude(MainActivity.locationsArrayList
                    .get(mainIntent.getIntExtra("placesNumber", 0)).latitude);
            locationOnList.setLongitude(MainActivity.locationsArrayList
                    .get(mainIntent.getIntExtra("placesNumber", 0)).longitude);

            centerMapOnLocation(locationOnList, MainActivity.placesArrayList
                    .get(mainIntent.getIntExtra("placesNumber", 0)));
        }


    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        String selectedAddress = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressesList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressesList != null && addressesList.size() > 0) {
                if (addressesList.get(0).getThoroughfare() != null) {
                    if (addressesList.get(0).getSubThoroughfare() != null) {
                        selectedAddress += addressesList.get(0).getSubThoroughfare();
                    }

                    selectedAddress += addressesList.get(0).getThoroughfare();
                }
            }

            if (selectedAddress.equals("")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                selectedAddress = simpleDateFormat.format(new Date());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(selectedAddress));

        MainActivity.placesArrayList.add(selectedAddress);
        MainActivity.locationsArrayList.add(latLng);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", MODE_PRIVATE);

        try {

            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();

            for (LatLng coOrdinates : MainActivity.locationsArrayList) {
                latitudes.add(Double.toString(coOrdinates.latitude));
                longitudes.add(Double.toString(coOrdinates.longitude));
            }

            sharedPreferences.edit()
                    .putString("places", ObjectSerializer.serialize(MainActivity.placesArrayList)).apply();

            sharedPreferences.edit()
                    .putString("latitudes", ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit()
                    .putString("longitudes", ObjectSerializer.serialize(longitudes)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();

        MainActivity.arrayAdapter.notifyDataSetChanged();
    }
}