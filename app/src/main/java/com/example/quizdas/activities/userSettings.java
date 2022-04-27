package com.example.quizdas.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.quizdas.R;
import com.example.quizdas.dialogs.ModificarDialogFragment;

import java.io.IOException;
import java.util.regex.Pattern;

public class userSettings extends AppCompatActivity {

    static final int REQUEST_PICK_IMAGE_CAPTURE = 10;
    static final int REQUEST_TAKE_IMAGE_CAPTURE = 11;
    String imgUri;
    String email;
    Bitmap bitmap;
    String UPLOAD_URL = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/lgonzalez184/WEB/quizdas/php/guardarImagen.php";
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        Button editfoto = findViewById(R.id.editFoto);
        editfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerImagen();
            }
        });

        Button modifButton = findViewById(R.id.buttonVolver);

        modifButton.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view) {
                //En caso de que todos los datos sean correctos:
                if (validarModificaciones()){ //Comprobar por que no funciona

                    //Obtenemos los datos de la interfaz
                    ContentValues values = obtenerDatosModif();

                    Bundle args = new Bundle();
                    args.putString("email", email);

                    //Modificamos los datos en la BD
                    //uploadImage();
                    //modifDatos();

                    //Mostramos una alerta de registro correcto
                    DialogFragment modifAlert = new ModificarDialogFragment();
                    modifAlert.setArguments(args);
                    modifAlert.show(getSupportFragmentManager(),"modificar_dialog");
                }
            }
        });
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
            ImageView imgPreview = findViewById(R.id.imagenperfil);
            imgPreview.setImageURI(imagenSeleccionada);
            imgUri=imagenSeleccionada.toString();
        }

    }

    /** Método para obtener los datos en la interfaz del usuario */
    public ContentValues obtenerDatosModif(){

        ContentValues values = new ContentValues();
        EditText texttel = findViewById(R.id.texttelefonomodif);
        String tel = texttel.getText().toString();
        values.put("tel", tel);
        EditText textPasswdmodif1 = findViewById(R.id.textPaswdModif1);
        String passwd = textPasswdmodif1.getText().toString();
        values.put("password", passwd);

        return values;
    }

    /** Método para validar los datos del formulario de modificar */
    public boolean validarModificaciones() {

        boolean valido = true;

        //Validamos el teléfono, en caso de que hubiera
        EditText texttel = findViewById(R.id.texttelefonomodif);
        String tel = texttel.getText().toString();
        Pattern tlfnoRegex = Pattern.compile("^?[67][0-9]{8}$"); //6 o 7 solo 1 vez y entre 0-9 se repite 8 veces
        if (!tel.equals("") && tel.length() > 9) {//Si el teléfono supera la longitud maxima
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoLargo), Toast.LENGTH_SHORT).show();
            texttel.setText("");
            valido = false;
        } else if (!tel.equals("") && !tlfnoRegex.matcher(tel).matches()){ //Si el teléfono no cumple los requisitos del regex
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoNoValido), Toast.LENGTH_SHORT).show();
            texttel.setText("");
            valido = false;
        }

        //Validamos la contraseña
        //NOTA: No se comprueba si la contraseña de confirmación no son correctas, ya que, en caso de que no coincidan, salta ya un error.
        EditText textPasswdmodif1 = findViewById(R.id.textPaswdModif1);
        String passwd = textPasswdmodif1.getText().toString();
        EditText textPasswdmodif2 = findViewById(R.id.textPaswdModif2);
        String passwdConf = textPasswdmodif2.getText().toString();
        if (!passwdConf.equals(passwd)) { //Si son distintas
            Toast.makeText(getApplicationContext(), getString(R.string.passwdNoCoincide), Toast.LENGTH_SHORT).show();
            textPasswdmodif1.setText("");
            textPasswdmodif2.setText("");
            valido = false;
        } else {
            if (passwd.equals("")) { //Si el EditText de Password está vacío
                Toast.makeText(getApplicationContext(), getString(R.string.passwdVacia), Toast.LENGTH_SHORT).show();
                textPasswdmodif1.setText("");
                textPasswdmodif2.setText("");
                valido = false;
            } else if (passwd.length() < 8 || passwd.length() > 16) { //Si el EditText de Password no cumple la longitud
                Toast.makeText(getApplicationContext(), getString(R.string.passwdLarga), Toast.LENGTH_SHORT).show();
                textPasswdmodif1.setText("");
                textPasswdmodif2.setText("");
                valido = false;
            }
        }

        return valido;

    }

    //Funciona, pero no guarda la imagen en el server
    /*public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Guardando foto...", "Espere por favor");
           StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.d("Foto: ", "FOTO SUBIDAAAAA");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("Foto: ", "ERROOOOOOR" + error.toString());
                //Toast.makeText(userSettings.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = "img_" + imgUri;

                Map<String, String> params = new Hashtable<String, String>();
                params.put("foto", imagen);
                params.put("nombre", nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

}