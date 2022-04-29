package com.example.quizdas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase() {

    }

    /**
     * Se guarda el token en las preferencias
     * @param s El token
     */
    //https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    /**
     * Crea una notificación con el mensaje que recibe de Firebase, para avisar del éxito o fracaso en la creación o borrado de usuarios.
     * @param remoteMessage El mensaje que recibe de Firebase
     */
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationManager elManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("aus", "Ausente",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "aus")
                        .setContentTitle(getApplicationContext().getString(R.string.app_name))
                        .setContentText(getApplicationContext().getString(R.string.dialog_registrarse))
                        .setSubText("Usuario registrado")
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());
    }

    /**
     * Devuelve el token del dispositivo
     */
    //https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
    public static String getToken(Context context){
        return  context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

    }

}
