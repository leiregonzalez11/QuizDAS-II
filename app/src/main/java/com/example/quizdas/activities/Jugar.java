package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizdas.R;

public class Jugar extends AppCompatActivity {

    private boolean correcta = true;
    private int numPregCorrectas = 0;
    private int pregActual = 0;
    private String email;
    private Pregunta [] preguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        Bundle datos = this.getIntent().getExtras();
        String tipo = datos.getString("tipo");
        int numPregs = datos.getInt("numPreguntas");
        email = datos.getString("email");
        GestorDB dbHelper = GestorDB.getInstance(this);
        preguntas = new Pregunta[numPregs];
        if (tipo.equals("noAleatorio")){
            String categoria = datos.getString("categoria");
            preguntas = dbHelper.obtenerPreguntas(categoria,numPregs);
        } else {
            preguntas = dbHelper.obtenerPreguntasAleatorio(numPregs);
        }

        jugar();

    }

    /** Método utilizado para inicializar y controlar el juego */
    public void jugar (){

        mostrarPreguntaActual();

        Button bresp1 = findViewById(R.id.buttonresp1);
        Button bresp2 = findViewById(R.id.buttonresp2);
        Button bresp3 = findViewById(R.id.buttonresp3);

        bresp1.setOnClickListener((view -> {
            bresp2.setEnabled(false);
            bresp3.setEnabled(false);
            validarRespuestas(1);
        }));

        bresp2.setOnClickListener((view -> {
            bresp1.setEnabled(false);
            bresp3.setEnabled(false);
            validarRespuestas(2);
        }));

        bresp3.setOnClickListener((view -> {
            bresp1.setEnabled(false);
            bresp2.setEnabled(false);
            validarRespuestas(3);
        }));

    }

    /** Método utilizado para actualizar la interfaz del juego */
    public void mostrarPreguntaActual(){

        TextView preg = findViewById(R.id.textPregunta);
        Button resp1 = findViewById(R.id.buttonresp1);
        Button resp2 = findViewById(R.id.buttonresp2);
        Button resp3 = findViewById(R.id.buttonresp3);

        resp1.setEnabled(true);
        resp2.setEnabled(true);
        resp3.setEnabled(true);

        preg.setText(preguntas[pregActual].getPregunta());
        resp1.setText(preguntas[pregActual].getResp1());
        resp2.setText(preguntas[pregActual].getResp2());
        resp3.setText(preguntas[pregActual].getResp3());
    }

    /** Método utilizado para comprobar si la respuesta elegida es correcta o no */
    public void validarRespuestas(int boton){

        Button bresp1 = findViewById(R.id.buttonresp1);
        Button bresp2 = findViewById(R.id.buttonresp2);
        Button bresp3 = findViewById(R.id.buttonresp3);
        boolean correcta = false;

        //Comprobamos el botón que ha sido pulsado y comprobamos si la respuesta es correcta

        if (boton == 1){
            if (preguntas[pregActual].getRespCorrecta().equals(preguntas[pregActual].getResp1())) {
                correcta = true;
            }
        } else if (boton == 2){
            if (preguntas[pregActual].getRespCorrecta().equals(preguntas[pregActual].getResp2())) {
                correcta = true;
            }
        } else if (boton == 3){
            if (preguntas[pregActual].getRespCorrecta().equals(preguntas[pregActual].getResp3())){
                correcta = true;
            }
        }

        if (correcta) {numPregCorrectas++; }//Si la respuesta ha sido correcta la sumamos

        pregActual++;
        if (pregActual<preguntas.length) { //Mientras  haya preguntas que mostrar
            mostrarPreguntaActual();
        }

        else { //Si es la última pregunta
            System.out.println(numPregCorrectas);
            System.out.println(preguntas.length);
            Intent intent = new Intent(this, Terminar.class);
            intent.putExtra("pregsCorrectas", numPregCorrectas);
            intent.putExtra("totalPregs", preguntas.length);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();

        }

    }
}