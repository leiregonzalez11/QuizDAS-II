package com.example.quizdas.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizdas.R;
import com.example.quizdas.dialogs.ModificarDialogFragment;

import java.io.IOException;
import java.util.regex.Pattern;

public class userSettings extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    String imgUri;
    String email;
    Bitmap bitmap;
    ImageView imgPreview;
    RequestQueue request;
    EditText textPasswdmodif1, texttel, textPasswdmodif2;
    static final int REQUEST_PICK_IMAGE_CAPTURE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        textPasswdmodif1 = findViewById(R.id.textPaswdModif1);
        textPasswdmodif2 = findViewById(R.id.textPaswdModif2);

        Button editfoto = findViewById(R.id.editFoto);
        editfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerImagen();
            }
        });

        request = Volley.newRequestQueue(getApplicationContext());

        Button modifButton = findViewById(R.id.buttonVolver);

        modifButton.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                //En caso de que todos los datos sean correctos:
                if (validarModificaciones()){ //Comprobar por que no funciona
                    cargarWebService();
                }
            }
        });
    }

    private void cargarWebService() {

        String url = "";

        url = url.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this,this);
        request.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), getString(R.string.errorServidor), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("Respuesta", response.trim());
        String respuesta = response.trim();
        switch (respuesta){
            case "Modif_ok":
                siguiente();
                Log.i("MODIF", "Modif Ok");
                break;
        }

    }

    /*Métodos empleados para gestionar la imagen de perfil del usuario*/

    /** Método utilizado para obtener una imagen, en este caso de la galería */
    private void obtenerImagen(){

        Intent intentFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentFoto, REQUEST_PICK_IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_PICK_IMAGE_CAPTURE) && resultCode == RESULT_OK) {
            //Obtengo la imagen seleccionada de la galeria
            Uri imagenSeleccionada = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagenSeleccionada);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgPreview = findViewById(R.id.imagenperfil);
            imgPreview.setImageURI(imagenSeleccionada);
            imgUri=imagenSeleccionada.toString();
        }

    }

    public void siguiente(){
        DialogFragment modifAlert = new ModificarDialogFragment();
        modifAlert.show(getSupportFragmentManager(),"modificar_dialog");
    }


    /** Método para validar los datos del formulario de modificar */
    public boolean validarModificaciones() {

        boolean valido = true;

        //Validamos la contraseña
        //NOTA: No se comprueba si la contraseña de confirmación no son correctas, ya que, en caso de que no coincidan, salta ya un error.
        String passwd = textPasswdmodif1.getText().toString();
        String passwdConf = textPasswdmodif2.getText().toString();
        if (!passwdConf.equals(passwd)) { //Si son distintas
            Toast.makeText(getApplicationContext(), getString(R.string.passwdNoCoincide), Toast.LENGTH_SHORT).show();
            textPasswdmodif1.setText("");
            textPasswdmodif2.setText("");
            valido = false;
        } else {
            if (passwd.equals("")) { //Si el EditText de Password está vacío

            } else if (passwd.length() < 8 || passwd.length() > 16) { //Si el EditText de Password no cumple la longitud
                Toast.makeText(getApplicationContext(), getString(R.string.passwdLarga), Toast.LENGTH_SHORT).show();
                textPasswdmodif1.setText("");
                textPasswdmodif2.setText("");
                valido = false;
            }
        }

        //Comprobamos que se ha elegido una foto de perfil
        if (imgUri.equals("")) { //Si no está seleccionado
            Toast.makeText(getApplicationContext(), getString(R.string.fotoperfil), Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;

    }
}