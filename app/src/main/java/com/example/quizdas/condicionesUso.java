package com.example.quizdas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class condicionesUso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones_uso);
    }
    public void volver(View view) {
        finish();
        //Intent intent = new Intent(this, Registrarse.class);
        //startActivity(intent);
    }
}