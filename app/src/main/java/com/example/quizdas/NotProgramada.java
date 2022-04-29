package com.example.quizdas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.quizdas.activities.Inicio;

import java.util.Locale;

public class NotProgramada extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("notificacion", "Llega aqui");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
        Locale.setDefault(nuevaloc);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context contexto = context.createConfigurationContext(configuration);
        context.getResources().updateConfiguration(configuration, contexto.getResources().getDisplayMetrics());

        Intent i = new Intent(context, Inicio.class);
        PendingIntent intentApp = PendingIntent.getActivity(context, 13, i, 0);

        NotificationManager elManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("aus", "Ausente",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "aus")
                        .setContentTitle("Registro")
                        .setContentText("Registrado")
                        .setSubText("Usuario registrado")
                        .setAutoCancel(true)
                        .setContentIntent(intentApp)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());
    }
}

