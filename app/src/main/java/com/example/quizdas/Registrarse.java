package com.example.quizdas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Registrarse extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registrarBoton = findViewById(R.id.buttonRegistrarse);
        GestorDB dbHelper = GestorDB.getInstance(this);

        registrarBoton.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    public boolean validarRegistro() {
        boolean valido = true;
        GestorDB dbHelper = GestorDB.getInstance(this);

        //Validamos el nombre
        EditText textNombre = findViewById(R.id.textNombre);
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

        /*//Validamos el DNI

        NOTA: Actualmente no se utiliza el DNI en ninguna validación de la aplicación, pero no se descarta utilizarlo en
              futuras mejoras de la aplicación.

        EditText textDNI = findViewById(R.id.textDNI);
        String dni = textDNI.getText().toString();
        Pattern dniregex = Pattern.compile("[0-9]{8}[A-Z]");
        String letrasDNI = "TRWAGMYFPDXBNJZSQVHLCKE";
        if(dni.equals("")){ //Si el DNI está vacío
            Toast.makeText(getApplicationContext(), getString(R.string.dniVacio), Toast.LENGTH_SHORT).show();
            textDNI.setText("");
            valido = false;
        }
        else if (!dniregex.matcher(dni).matches() || dni.charAt(8) != letrasDNI.charAt(Integer.parseInt(dni.substring(0, 8)) % 23)) {
            //Si el DNI no cumple el patrón de 8 numeros y una letra o la letra no es correcta
            Toast.makeText(getApplicationContext(), getString(R.string.dniNoValido), Toast.LENGTH_SHORT).show();
            textDNI.setText("");
            valido = false;
        }
        else if (dbHelper.buscarDni(dni)) {
            //Si el DNI ya existe en la BD
            Toast.makeText(getApplicationContext(), getString(R.string.dniYaExiste), Toast.LENGTH_SHORT).show();
            textDNI.setText("");
            valido = false;
        }*/

        //Validamos el teléfono, en caso de que hubiera
        EditText texttlfno = findViewById(R.id.textPhone);
        String tlfno = texttlfno.getText().toString();
        Pattern tlfnoRegex = Pattern.compile("^?[67][0-9]{8}$"); //6 o 7 solo 1 vez y entre 0-9 se repite 8 veces
        if (!tlfno.equals("") && tlfno.length() > 9) {//Si el teléfono supera la longitud maxima
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoLargo), Toast.LENGTH_SHORT).show();
            texttlfno.setText("");
            valido = false;
        } else if (!tlfno.equals("") && !tlfnoRegex.matcher(tlfno).matches()){ //Si el teléfono no cumple los requisitos del regex
            Toast.makeText(getApplicationContext(), getString(R.string.tlfnoNoValido), Toast.LENGTH_SHORT).show();
            texttlfno.setText("");
            valido = false;
        }

        //Validamos el email
        EditText textEmail = findViewById(R.id.textEmailRegistro);
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
        }else if (dbHelper.buscarUsuario(email)) { //Si el email utilizado ya existe
            Toast.makeText(getApplicationContext(), getString(R.string.emailYaExiste), Toast.LENGTH_SHORT).show();
            textEmail.setText("");
            valido = false;
        }

        //Validamos la contraseña
        //NOTA: No se comprueba si la contraseña de confirmación no son correctas, ya que, en caso de que no coincidan, salta ya un error.
        EditText textPasswd1 = findViewById(R.id.textPasswdRegistro);
        String passwd = textPasswd1.getText().toString();
        EditText textPasswd2 = findViewById(R.id.textPasswdRegistro2);
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

        return valido;

    }

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

    public void condicionesdeUso(View view) {
        Intent intent = new Intent(this, condicionesUso.class);
        startActivity(intent);
    }
}