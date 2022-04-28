package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizdas.R;

import java.util.regex.Pattern;

public class IniciarSesion extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    EditText  textEmail, textPasswd;
    Button loginBoton;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textEmail = findViewById(R.id.textEmailLogin);
        textPasswd = findViewById(R.id.textPaswdLogin);

        request = Volley.newRequestQueue(getApplicationContext());

        loginBoton = findViewById(R.id.buttonAcceder);

        loginBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()){ //En caso de que todos los datos sean correctos:
                    cargarWebService();
                }
            }
        });


    }

    private void cargarWebService() {

        String url = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/lgonzalez184/WEB/inicioSesion.php?email="
        +textEmail.getText().toString() + "&passwd=" +textPasswd.getText().toString();

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
            case "Login_ok":
                acceder();
                Log.i("LOGIN", "Login Ok");
                break;
            case "Login_emailnoexiste":
                Toast.makeText(getApplicationContext(), getString(R.string.usuarioNoexiste), Toast.LENGTH_SHORT).show();
                Log.i("LOGIN", "Email no existe");
                break;
            case "Login_passwdnotvalid":
                Toast.makeText(getApplicationContext(),  getString(R.string.contraseñaIncorrecta), Toast.LENGTH_SHORT).show();
                Log.i("LOGIN", "Contraseña incorrecta");
                break;
        }

    }

    /** Called when the user taps the Acceder button (usando BD local) */
    public void acceder(){
        String email = textEmail.getText().toString();
        Intent intent = new Intent(this, bienvenida.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    /** Método utilizado para validar los datos del formulario de inicio de sesión */
    public boolean validarDatos() {

        boolean valido = true;
        //Validamos el email
        String email = textEmail.getText().toString();
        Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (email.equals("")) { //Si el email está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.emailVacio), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        } else if (!patternEmail.matcher(email).matches()) { //Si el email no es correcto
            Toast.makeText(getApplicationContext(), getString(R.string.emailNoValido), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }

        //Validamos la contraseña
        String passwd = textPasswd.getText().toString();
        if (passwd.equals("")) { //Si el EditText de Password está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.passwdVacia), Toast.LENGTH_SHORT).show();
            textPasswd.setText("");
            valido = false;
        } else if (passwd.length() <8 || passwd.length() > 16) { //Si el password no cumple la longitud mínima o máxima
            Toast.makeText(getApplicationContext(), getString(R.string.passwdLarga), Toast.LENGTH_SHORT).show();
            textPasswd.setText("");
            valido = false;
        }

        return valido;
    }

    /** Método utilizado para comprobar si existe el usuario en la BD local
     *  y la contraseña introducida coincide con la registrada en la BD local */
    public boolean validarInicioSesion() {

        boolean valido = true;

        //Validamos el email
        EditText textEmail = findViewById(R.id.textEmailLogin);
        String email = textEmail.getText().toString();

        GestorDB dbHelper = GestorDB.getInstance(this);
        if (!dbHelper.buscarUsuario(email)){
            Toast.makeText(getApplicationContext(), getString(R.string.usuarioNoexiste) + " " + email, Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }

        //Validamos la contraseña
        EditText textPasswd1 = findViewById(R.id.textPaswdLogin);
        String passwd = textPasswd1.getText().toString();
        if (!dbHelper.validarContraseña(email,passwd)){
            Toast.makeText(getApplicationContext(), getString(R.string.contraseñaIncorrecta), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }

        return valido;
    }
}