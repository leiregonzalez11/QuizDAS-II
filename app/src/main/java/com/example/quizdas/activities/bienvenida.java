package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quizdas.R;

public class bienvenida extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        GestorDB dbHelper = GestorDB.getInstance(this);
        TextView textUser = findViewById(R.id.textBienvenida);

        /*Recuperación de datos de la actividad anterior*/
        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        //String nameUser = dbHelper.obtenerNombreUser(email);
        //textUser.setText("¡Bienvenid@ " + nameUser + "!");
        textUser.setText("¡Bienvenid@!");

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.quizdas1)
                .setContentTitle("¡Bienvenido!")
                .setContentText("¡Es un placer verte por aqui!")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("1", "Bienvenida",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(canal);
        }
        manager.notify(1, builder.build());
    }

    /** Called when the user taps the Jugar Aleatorio button */
    public void aleatorio(View view){

        Intent intent = new Intent(this, numPreguntas.class);
        intent.putExtra("tipo", "aleatorio");
        startActivity(intent);
        finish();
    }

    /** Called when the user taps the Elegir Categoria button */
    public void categorias(View view){
        Intent intent = new Intent(this, elegirCategorias.class);
        startActivity(intent);
    }

    /** Called when the user taps the Configuracion button */
    public void config(View view){
        Intent intent = new Intent(this, userSettings.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    /** Called when the user taps the Cerrar Sesion button */
    public void cerrarSesion(View view){
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
        finish();
    }


}