package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.quizdas.R;

public class Inicio extends AppCompatActivity {

    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        /*Cargamos la BD...*/
        GestorDB dbHelper = GestorDB.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    /** Called when the user taps the Qué Es button */
    public void whatIs(View view){
        Intent intent = new Intent(this, queEsQuizDAS.class);
        startActivity(intent);
    }

    /** Called when the user taps the Iniciar Sesión button */
    public void iniciarSesion(View view) {
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
    }

    /** Called when the user taps the registrase button */
    public void registrase(View view) {
        Intent intent = new Intent(this, Registrarse.class);
        startActivity(intent);

    }

    /** Called when the user taps the Salir button */
    public void salir(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}