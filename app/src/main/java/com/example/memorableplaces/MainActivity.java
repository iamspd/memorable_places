package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // widgets
    private ListView placesList;

    // variables
    private ArrayList<String> placesArrayList;

    //constants

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = findViewById(R.id.listPlaces);
        placesArrayList = new ArrayList<>();
        placesArrayList.add("Add a new place...");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placesArrayList);
        placesList.setAdapter(arrayAdapter);

        placesList.setOnItemClickListener((parent, view, position, id) -> {
            Intent mainIntent = new Intent(getApplicationContext(), MapsActivity.class);
            mainIntent.putExtra("placesNumber", position);
            startActivity(mainIntent);
        });
    }

}