package com.example.quizdas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizdas.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class aboutUs extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Añadimos un marcador a la ubicación elegida y hacemos zoom
        LatLng location = new LatLng(43.263609, -2.950702);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("QuizDAS S.L"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
        //Tipo de mapa: Hibrido
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }


    public void volver(View view){
        finish();
        Intent intent = new Intent(this, aboutUs.class);
        startActivity(intent);
    }
}