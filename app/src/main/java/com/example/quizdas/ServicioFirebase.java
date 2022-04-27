package com.example.quizdas;

import com.google.firebase.messaging.FirebaseMessagingService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase() { }

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
     * Crea una notificación con el mensaje que recibe de Firebase, para avisar del éxito o fracaso en la creación de usuarios.
     * @param remoteMessage El mensaje que recibe de Firebase
     */
   /* public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("notificacion", "Llega aqui");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
        Locale.setDefault(nuevaloc);
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context contexto = getApplicationContext().createConfigurationContext(configuration);
        getApplicationContext().getResources().updateConfiguration(configuration, contexto.getResources().getDisplayMetrics());


        //TODO: ADAPTAR AL PROYECTO

        //https://stackoverflow.com/questions/41888161/how-to-create-a-custom-notification-layout-in-android
        RemoteViews contentView;
        //contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificacion);
        //contentView.setImageViewResource(R.id.planeta, R.mipmap.ic_launcher);

        NotificationManager elManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("aus", "Ausente",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        }

        //Establece el mensaje dependiendo de lo que haya recibido de Firebase
        switch (remoteMessage.toIntent().getExtras().getString("mensaje")){
            case ("okCrear"):
                contentView.setTextViewText(R.id.textoVuelve, getApplicationContext().getString(R.string.creadoCorrectamente));
                break;
            case ("errorCrear"):
                contentView.setTextViewText(R.id.textoVuelve, getApplicationContext().getString(R.string.errorCreado));
                break;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "aus")
                        .setSmallIcon(R.drawable.marte)
                        .setContentTitle(getApplicationContext().getString(R.string.app_name))
                        .setContentText(getApplicationContext().getString(R.string.borradoCorrectamente))
                        .setSubText(getApplicationContext().getString(R.string.notificacion))
                        .setAutoCancel(true)
                        .setContent(contentView)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01, mBuilder.build());
    }*/

        /**
         * Devuelve el token
         * @param context La actividad desde la que se lanza el método.
         * @return El token
         */
        //https://stackoverflow.com/questions/37787373/firebase-fcm-how-to-get-token
    /*public static String getToken(Context context){
        return  context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

    }*/

}
