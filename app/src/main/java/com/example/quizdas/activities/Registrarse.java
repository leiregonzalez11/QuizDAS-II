package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizdas.R;
import com.example.quizdas.Usuario;
import com.example.quizdas.dialogs.RegistrarseDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class Registrarse extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    String imgUriReg = "";
    Bitmap bitmapReg;
    EditText textNombre, textTel, textEmail, textPasswd1, textPasswd2;
    Button registrarBoton;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    static final int REQUEST_PICK_IMAGE_CAPTURE_REG = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textNombre = findViewById(R.id.textNombre);
        textTel = findViewById(R.id.textPhone);
        textEmail = findViewById(R.id.textEmailRegistro);
        textPasswd1 = findViewById(R.id.textPasswdRegistro);
        textPasswd2 = findViewById(R.id.textPasswdRegistro2);

        request = Volley.newRequestQueue(getApplicationContext());

        /** Called when the user taps the Elegir foto button */
        Button fotoRegistro = findViewById(R.id.registerFotoButton);
        fotoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerImagen();
            }
        });

        registrarBoton = findViewById(R.id.buttonRegistrarse);

        registrarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarRegistro()){ //En caso de que todos los datos sean correctos:
                    cargarWebService();
                }
            }
        });

        //Con BD LOCAL
        //GestorDB dbHelper = GestorDB.getInstance(this);

        /*registrarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarRegistro()){ //En caso de que todos los datos sean correctos:

                    //Obtenemos los datos de la interfaz
                    ContentValues values = obtenerDatos();

                    //Añadir datos a la BD
                    dbHelper.insertarUsuario(values);

                    //Mostramos una alerta de registro correcto
                    DialogFragment registraseAlert = new RegistrarseDialogFragment();
                    registraseAlert.show(getSupportFragmentManager(),"registrarse_dialog");
                }
            }
        });*/
    }

    private void cargarWebService() {

        String url = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/lgonzalez184/WEB/registrarUser.php?nombre="
                + textNombre.getText().toString() + "&tel=" + textTel.getText().toString() + "&foto=" + imgUriReg + "&email="
                +textEmail.getText().toString() + "&passwd=" + textPasswd1.getText().toString();

        url = url.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this,this);
        request.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario en este momento. Pruebe de nuevo más tarde", Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(String response) {
        if (response.equals("Registro_done")){
            DialogFragment registraseAlert = new RegistrarseDialogFragment();
            registraseAlert.show(getSupportFragmentManager(),"registrarse_dialog");
            Log.i("REGISTRO", "Registrado");
        }else{
            Toast.makeText(getApplicationContext(), "No registrado: pruebe de nuevo.", Toast.LENGTH_SHORT).show();
            Log.i("REGISTRO", "No Registrado");
        }

    }

    /** Método utilizado para obtener una imagen, en este caso de la galería */
    public void obtenerImagen(){

        Intent intentFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentFoto, REQUEST_PICK_IMAGE_CAPTURE_REG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_PICK_IMAGE_CAPTURE_REG) && resultCode == RESULT_OK) {
            //Obtengo la imagen seleccionada de la galeria
            Uri imagenSeleccionada = data.getData();
            try {
                bitmapReg = MediaStore.Images.Media.getBitmap(getContentResolver(),imagenSeleccionada);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imgPreviewReg = findViewById(R.id.imagenperfilReg);
            imgPreviewReg.setImageURI(imagenSeleccionada);
            imgUriReg=imagenSeleccionada.toString();
        }

    }

    /** Método utilizado para validar los datos del formulario de registro */
    public boolean validarRegistro() {
        boolean valido = true;
        GestorDB dbHelper = GestorDB.getInstance(this);

        //Validamos el nombre
        String nombre = textNombre.getText().toString();
        if (nombre.equals("")) { //En caso de que esté vacío
            Toast.makeText(getApplicationContext(), getString(R.string.nombreVacio), Toast.LENGTH_SHORT).show();
            textNombre.setText("");
            valido = false;
        } else if (nombre.length() > 31) { //En caso de que supere la longitud máxima
            Toast.makeText(getApplicationContext(), getString(R.string.nombreLargo), Toast.LENGTH_SHORT).show();
            textNombre.setText("");
            valido = false;
        }

        //Validamos el teléfono, en caso de que hubiera
        String tlfno = textTel.getText().toString();
        Pattern tlfnoRegex = Pattern.compile("^?[67][0-9]{8}$"); //6 o 7 solo 1 vez y entre 0-9 se repite 8 veces
        if (!tlfno.equals("") && tlfno.length() > 9) {//Si el teléfono supera la longitud maxima
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoLargo), Toast.LENGTH_SHORT).show();
            textTel.setText("");
            valido = false;
        } else if (!tlfno.equals("") && !tlfnoRegex.matcher(tlfno).matches()){ //Si el teléfono no cumple los requisitos del regex
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoNoValido), Toast.LENGTH_SHORT).show();
            textTel.setText("");
            valido = false;
        }

        //Validamos el email
        String email = textEmail.getText().toString();
        Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (email.equals("")){ //Si el email está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.emailVacio), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }else if (!patternEmail.matcher(email).matches()) { //Si el email no es correcto
            Toast.makeText(getApplicationContext(), getString(R.string.emailNoValido), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }/*else if (dbHelper.buscarUsuario(email)) { //Si el email utilizado ya existe
            Toast.makeText(getApplicationContext(), getString(R.string.emailYaExiste), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }*/

        //Validamos la contraseña
        //NOTA: No se comprueba si la contraseña de confirmación no son correctas, ya que, en caso de que no coincidan, salta ya un error.
        String passwd = textPasswd1.getText().toString();
        String passwdConf = textPasswd2.getText().toString();
        if (!passwdConf.equals(passwd)) { //Si son distintas
            Toast.makeText(getApplicationContext(), getString(R.string.passwdNoCoincide), Toast.LENGTH_SHORT).show();
            textPasswd1.setText("");
            textPasswd2.setText("");
            valido = false;
        } else {
            if (passwd.equals("")) { //Si el EditText de Password está vacío
                Toast.makeText(getApplicationContext(), getString(R.string.passwdVacia), Toast.LENGTH_SHORT).show();
                textPasswd1.setText("");
                valido = false;
            } else if (passwd.length() < 8 || passwd.length() > 16) { //Si el EditText de Password no cumple la longitud
                Toast.makeText(getApplicationContext(), getString(R.string.passwdLarga), Toast.LENGTH_SHORT).show();
                textPasswd1.setText("");
                valido = false;
            }
        }

        //Comprobamos que el Check Box esté marcado
        CheckBox checkCondiciones = findViewById(R.id.checkBox);
        if (!checkCondiciones.isChecked()) { //Si no está seleccionado
            Toast.makeText(getApplicationContext(), getString(R.string.checkboxNoSelecc), Toast.LENGTH_SHORT).show();
            valido = false;
        }

        //Comprobamos que se ha elegido una foto de perfil
        if (imgUriReg.equals("")) { //Si no está seleccionado
            Toast.makeText(getApplicationContext(), getString(R.string.checkboxNoSelecc), Toast.LENGTH_SHORT).show();
            valido = false;
        }


        return valido;

    }

    /** Método para obtener los datos en la interfaz del usuario */
    public ContentValues obtenerDatos(){

        ContentValues values = new ContentValues();
        EditText textNombre = findViewById(R.id.textNombre);
        String nombre = textNombre.getText().toString();
        values.put("nombre", nombre);
        /*EditText textDNI = findViewById(R.id.textDNI);
        String dni = textDNI.getText().toString();
        values.put("dni", dni);*/
        EditText texttlfno = findViewById(R.id.textPhone);
        String tlfno = texttlfno.getText().toString();
        values.put("tel", tlfno);
        EditText textEmail = findViewById(R.id.textEmailRegistro);
        String email = textEmail.getText().toString();
        values.put("email", email);
        EditText textPasswd1 = findViewById(R.id.textPasswdRegistro);
        String passwd = textPasswd1.getText().toString();
        values.put("password", passwd);

        return values;
    }

    /** Called when the user taps the Condiciones de Uso Imagebutton */
    public void condicionesdeUso(View view) {
        Intent intent = new Intent(this, condicionesUso.class);
        startActivity(intent);
    }

}