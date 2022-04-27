package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quizdas.R;
import com.example.quizdas.activities.bienvenida;

public class Terminar extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminar);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");
        int pregsAcertadas = datos.getInt("pregsCorrectas");
        int pregsTotales = datos.getInt("totalPregs");

        TextView textPregsAcertado = findViewById(R.id.textPregsAcertado);
        textPregsAcertado.setText(pregsAcertadas + "/" + pregsTotales);

        TextView textMsgPreguntas = findViewById(R.id.textMsgPreguntas);

        double division = (double) pregsAcertadas/pregsTotales;
        System.out.println(division);

        if (division < 0.25){
            textMsgPreguntas.setText(R.string.menos25acertadas);
        } else if (division >= 0.25 && division < 0.5){
            textMsgPreguntas.setText(R.string.menos50acertadas);
        } else if (division >= 0.5 && division < 0.75){
            textMsgPreguntas.setText(R.string.menos75acertadas);
        } else if (division >= 0.75 && division < 1){
            textMsgPreguntas.setText(R.string.menos100acertadas);
        }else if (pregsAcertadas/pregsTotales == 1){
            textMsgPreguntas.setText(R.string.todaspregacertadas);
        }
    }

    /** Called when the user taps the Volver al Menu button */
    public void volverAlMenu(View view){
        finish();
        Intent intent = new Intent(this, bienvenida.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}