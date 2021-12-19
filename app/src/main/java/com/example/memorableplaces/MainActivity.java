package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    // widgets
    private ListView placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = findViewById(R.id.listPlaces);
    }

    public void onAddNewPlacesClick(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }
}