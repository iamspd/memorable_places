package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // widgets
    private ListView placesList;

    // variables
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> placesArrayList = new ArrayList<>();
    static ArrayList<LatLng> locationsArrayList = new ArrayList<>();

    //constants

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = findViewById(R.id.listPlaces);

        SharedPreferences sharedPreferences = this
                .getSharedPreferences("com.example.memorableplaces", MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        placesArrayList.clear();
        latitudes.clear();
        longitudes.clear();
        locationsArrayList.clear();

        try {

            placesArrayList = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("places", ObjectSerializer.serialize(new ArrayList<String>())));

            latitudes = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("latitudes", ObjectSerializer.serialize(new ArrayList<String>())));

            longitudes = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("longitudes", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(placesArrayList.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {
            if (placesArrayList.size() == latitudes.size() && latitudes.size() == longitudes.size()) {

                for (int i = 0; i < latitudes.size(); i++) {
                    locationsArrayList
                            .add(new LatLng(Double.parseDouble(latitudes.get(i)),
                                    Double.parseDouble(longitudes.get(i))));
                }
            }
        } else {

            placesArrayList.add("Add a new place...");
            locationsArrayList.add(new LatLng(0, 0));
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placesArrayList);
        placesList.setAdapter(arrayAdapter);

        placesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent mainIntent = new Intent(getApplicationContext(), MapsActivity.class);
            mainIntent.putExtra("placesNumber", position);
            startActivity(mainIntent);
        });
    }

}