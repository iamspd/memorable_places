package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

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
        placesArrayList.add("Add a new place...");
        locationsArrayList.add(new LatLng(0, 0));

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placesArrayList);
        placesList.setAdapter(arrayAdapter);

        placesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent mainIntent = new Intent(getApplicationContext(), MapsActivity.class);
            mainIntent.putExtra("placesNumber", position);
            startActivity(mainIntent);
        });
    }

}