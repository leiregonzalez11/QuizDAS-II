package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizdas.R;

import java.util.regex.Pattern;

public class IniciarSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void acceder(View view){

        if (validarDatos()) {
            if (validarInicioSesion()) {

                EditText textEmail = findViewById(R.id.textEmailLogin);
                String email = textEmail.getText().toString();

                Intent intent = new Intent(this, bienvenida.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        }
    }

    public boolean validarDatos() {

        boolean valido = true;
        //Validamos el email
        EditText textEmail = findViewById(R.id.textEmailLogin);
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
        EditText textPasswd1 = findViewById(R.id.textPaswdLogin);
        String passwd = textPasswd1.getText().toString();
        if (passwd.equals("")) { //Si el EditText de Password está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.passwdVacia), Toast.LENGTH_SHORT).show();
            textPasswd1.setText("");
            valido = false;
        } else if (passwd.length() <8 || passwd.length() > 16) { //Si el password no cumple la longitud mínima o máxima
            Toast.makeText(getApplicationContext(), getString(R.string.passwdLarga), Toast.LENGTH_SHORT).show();
            textPasswd1.setText("");
            valido = false;
        }

        return valido;
    }

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