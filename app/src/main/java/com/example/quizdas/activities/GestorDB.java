package com.example.quizdas.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quizdas.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;

public class GestorDB extends SQLiteOpenHelper{

    private static GestorDB sInstance;
    private static Context context;
    private static final String DATABASE_NAME = "quizDB";
    private static final int DATABASE_VERSION = 1;


    /**
     * Constructor should be private to prevent direct instantiation.
     */
    private GestorDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public static synchronized GestorDB getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new GestorDB(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLitedatabase){
        crearTablas(sqLitedatabase);
        insertarCategorias(sqLitedatabase);
        try {
            insertarPreguntas(sqLitedatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    /** Método para crear las tablas en la la BD local */
    public void crearTablas(SQLiteDatabase sqLiteDatabase){

        /*
        //Esquema de la tabla Usuario
        String query1 = "CREATE TABLE IF NOT EXISTS Usuario (idUser INTEGER NOT NULL PRIMARY KEY AutoIncrement," +
                "nombre VARCHAR(30) NOT NULL, " +
                "tel int(9)," +
                "email VARCHAR NOT NULL UNIQUE," +
                "password VARCHAR(15) NOT NULL);";
        Log.d("Tabla Usuario", query1);
        sqLiteDatabase.execSQL(query1);*/

        //Esquema de la tabla Categoria
        String query2 = "CREATE TABLE IF NOT EXISTS Categoria (idCat INT NOT NULL PRIMARY KEY, "+
                "categoria VARCHAR NOT NULL)";
        Log.d("Tabla Categoria", query2);
        sqLiteDatabase.execSQL(query2);

        //Esquema de la tabla Usuario
        String query3 = "CREATE TABLE IF NOT EXISTS Preguntas (idPreg INTEGER NOT NULL PRIMARY KEY, "+
                "pregunta VARCHAR NOT NULL, " +
                "resp1 VARCHAR NOT NULL, " +
                "resp2 VARCHAR NOT NULL," +
                "resp3 VARCHAR NOT NULL," +
                "respCorrecta VARCHAR NOT NULL," +
                "catPregunta INT NOT NULL, " +
                "FOREIGN KEY (catPregunta) REFERENCES Categoria(idCat));";

        Log.d("Tabla Preguntas", query3);
        sqLiteDatabase.execSQL(query3);

    }

    /** Método para cargar las categorías del juego a la BD local */
    public void insertarCategorias(SQLiteDatabase sqLiteDatabase){

        String query = "INSERT INTO Categoria VALUES (1,'Deportes'),(2,'Entretenimiento'),(3,'Historia'),(4,'Geografia'),(5,'Ciencias'), " +
                "(6, 'Lengua y Arte'),(7, 'Matematicas'),(8, 'Musica'),(9, 'Gastronomia'),(10, 'Moda'),(11, 'Tecnología');";
        sqLiteDatabase.execSQL(query);
    }

    /** Método para cargar las preguntas del juego a la BD local */
    public void insertarPreguntas(SQLiteDatabase sqLiteDatabase) throws IOException {

        //Carga de datos desde un archivo .txt usando res/raw
        InputStream file = context.getResources().openRawResource(R.raw.preguntas);
        BufferedReader buffer = new BufferedReader((new InputStreamReader(file)));
        boolean seguir = true;

        while (seguir){
            try{
                String query = buffer.readLine();
                Log.d("Query: ", query);
                sqLiteDatabase.execSQL(query);
            }catch (Exception e){
                seguir = false;
                buffer.close();
            }
        }

    }

    /** Método para insertar un nuevo usuario a la BD local */
    public void insertarUsuario(ContentValues values){
        SQLiteDatabase sqLiteDatabase = sInstance.getWritableDatabase();
        sqLiteDatabase.insert("Usuario", null, values);
        sqLiteDatabase.close();
    }

    /** Método para buscar si un usuario existe en la BD local */
    public boolean buscarUsuario(String email){
        SQLiteDatabase sqLiteDatabase = sInstance.getWritableDatabase();

        boolean existe = true;
        Cursor c = sqLiteDatabase.rawQuery("SELECT email FROM Usuario WHERE email = \'" + email + "\';", null);
        if (!c.moveToNext()){
            existe = false;
        }

        return existe;
    }

    /** Método para comprobar si las contraseñas coinciden */
    public boolean validarContraseña(String email, String passwd){
        SQLiteDatabase sqLiteDatabase = sInstance.getWritableDatabase();
        boolean existe = true;
        Cursor c = sqLiteDatabase.rawQuery("SELECT password FROM Usuario WHERE email = \'" + email + "\';", null);
        while (c.moveToNext()){
            String password = c.getString(0);
            if (!passwd.equals(password))
                existe = false;
        }

        return existe;
    }

    /** Método para obtener el nombre del usuario de la BD local */
    public String obtenerNombreUser(String email){
        SQLiteDatabase sqLiteDatabase = sInstance.getWritableDatabase();
        boolean existe = true;
        String name = "";
        Cursor c = sqLiteDatabase.rawQuery("SELECT nombre FROM Usuario WHERE email = \'" + email + "\';", null);
        while (c.moveToNext()){
            name = c.getString(0);

        }

        return name;
    }

    /** Método para obtener las categorías del juego desde la BD local */
    public String [] obtenerCategorias (){

        SQLiteDatabase sqLiteDatabase = sInstance.getWritableDatabase();

        String nameCat = "";
        int i = 0;
        String [] categorias = new String[11];

        Cursor c = sqLiteDatabase.rawQuery("SELECT categoria FROM Categoria ORDER BY categoria ASC;", null);
        while (c.moveToNext()){
            nameCat = c.getString(0);
            categorias[i] = nameCat;
            i++;
        }

        return categorias;
    }

    /** Método para obtener el nº de preguntas elegido para el juego
     *  de la categoria elegida desde la BD local */
    public Pregunta [] obtenerPreguntas(String categoria, int numPreg) {

        SQLiteDatabase sqLiteDatabase = sInstance.getReadableDatabase();

        //Obtenemos el número de preguntas total de la categoria seleccionadas
        int numPreguntasTotal = 0;
        Cursor c = sqLiteDatabase.rawQuery("SELECT COUNT(idPreg) FROM Preguntas WHERE catPregunta = (SELECT idCat FROM Categoria WHERE categoria=\'" + categoria + "\');", null);
        while (c.moveToNext()) {
            numPreguntasTotal = c.getInt(0);
        }
        c.close();

        //Obtenemos los ids de las preguntas de la categoría

        int[] idPregs = new int[numPreguntasTotal];
        int i = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT idPreg FROM Preguntas WHERE catPregunta = (SELECT idCat FROM Categoria WHERE categoria=\'" + categoria + "\');", null);
        while (cursor.moveToNext()) {
            int idPreg = cursor.getInt(0);
            idPregs[i] = idPreg;
            i++;
        }
        cursor.close();

        Stack<Integer> idPregsJuego = new Stack<Integer>();

        //Elegimos las preguntas al azar (sin repeticion)
        for (int j = 0; j < numPreg; j++) {
            int pos = (int) (Math.random() * (idPregs[idPregs.length - 1] - idPregs[0] + 1) + idPregs[0]);
            while (idPregsJuego.contains(pos)) {
                pos = (int) (Math.random() * (idPregs[idPregs.length - 1] - idPregs[0] + 1) + idPregs[0]);
            }
            idPregsJuego.push(pos);
            Log.d("Preguntas:", idPregsJuego.toString());
        }

        //Obtenemos los valores de las preguntas

        Pregunta[] preguntas = new Pregunta[numPreg];
        int k = 0;
        while (!idPregsJuego.isEmpty()) {
            Cursor cu = sqLiteDatabase.rawQuery("SELECT pregunta, resp1, resp2, resp3, respCorrecta FROM Preguntas WHERE idPreg = " + idPregsJuego.pop() + ";", null);
            while (cu.moveToNext()) {
                String pregunta = cu.getString(0);
                String resp1 = cu.getString(1);
                String resp2 = cu.getString(2);
                String resp3 = cu.getString(3);
                String respCorrecta = cu.getString(4);
                Pregunta preg = new Pregunta(pregunta, resp1, resp2, resp3, respCorrecta);
                Log.d("Pregunta", resp1);
                preguntas[k] = preg;
                k++;
            }
            cu.close();
        }
        return preguntas;
    }

    /** Método para obtener el nº de preguntas elegido para el juego
     *  desde la BD local */
    public Pregunta [] obtenerPreguntasAleatorio(int numPreg) {

        SQLiteDatabase sqLiteDatabase = sInstance.getReadableDatabase();

        //Obtenemos el número de preguntas total
        int numPreguntasTotal = 0;
        Cursor c = sqLiteDatabase.rawQuery("SELECT COUNT(idPreg) FROM Preguntas;", null);
        while (c.moveToNext()) {
            numPreguntasTotal = c.getInt(0);
        }
        c.close();

        //Obtenemos los ids de las preguntas

        int[] idPregs = new int[numPreguntasTotal];
        int i = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT idPreg FROM Preguntas;", null);
        while (cursor.moveToNext()) {
            int idPreg = cursor.getInt(0);
            idPregs[i] = idPreg;
            i++;
        }
        cursor.close();

        Stack<Integer> idPregsJuego = new Stack<Integer>();

        //Elegimos las preguntas al azar (sin repeticion)
        for (int j = 0; j < numPreg; j++) {
            int pos = (int) (Math.random() * (idPregs[idPregs.length - 1] - idPregs[0] + 1) + idPregs[0]);
            while (idPregsJuego.contains(pos)) {
                pos = (int) (Math.random() * (idPregs[idPregs.length - 1] - idPregs[0] + 1) + idPregs[0]);
            }
            idPregsJuego.push(pos);
            Log.d("Preguntas:", idPregsJuego.toString());
        }

        //Obtenemos los valores de las preguntas

        Pregunta[] preguntas = new Pregunta[numPreg];
        int k = 0;
        while (!idPregsJuego.isEmpty()) {
            Cursor cu = sqLiteDatabase.rawQuery("SELECT pregunta, resp1, resp2, resp3, respCorrecta FROM Preguntas WHERE idPreg = " + idPregsJuego.pop() + ";", null);
            while (cu.moveToNext()) {
                String pregunta = cu.getString(0);
                String resp1 = cu.getString(1);
                String resp2 = cu.getString(2);
                String resp3 = cu.getString(3);
                String respCorrecta = cu.getString(4);
                Pregunta preg = new Pregunta(pregunta, resp1, resp2, resp3, respCorrecta);
                Log.d("Pregunta", resp1);
                preguntas[k] = preg;
                k++;
            }
            cu.close();
        }
        return preguntas;
    }

}
