package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizdas.R;

public class numPreguntas extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_preguntas);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");
    }

    /** Called when the user taps the Jugar button */
    public void jugar (View view){

        TextView textnumPreg = findViewById(R.id.textnumPreg);
        String numP = textnumPreg.getText().toString();
        /*Recuperación de datos de la actividad anterior*/
        Bundle datos = this.getIntent().getExtras();
        String tipo = datos.getString("tipo");

        if ( numP.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.numpregvacio), Toast.LENGTH_SHORT).show();
            textnumPreg.setText("");
        }
        else{
            int numPreg = Integer.parseInt(textnumPreg.getText().toString());
            if (numPreg < 1 || numPreg>20){
                Toast.makeText(getApplicationContext(), getString(R.string.minmaxpreg), Toast.LENGTH_SHORT).show();
                textnumPreg.setText("");
            }else{
                if (tipo.equals("noAleatorio")){
                    /*Recuperación de datos de la actividad anterior*/
                    String categoria = datos.getString("categoria");
                    Intent intent = new Intent(this, Jugar.class);
                    intent.putExtra("categoria",categoria);
                    intent.putExtra("numPreguntas",numPreg);
                    intent.putExtra("tipo", tipo);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(this, Jugar.class);
                    intent.putExtra("numPreguntas",numPreg);
                    intent.putExtra("tipo", tipo);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        }
    }
}