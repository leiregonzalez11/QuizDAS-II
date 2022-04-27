package com.example.quizdas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class elegirCategorias extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_categorias);

        GestorDB dbHelper = GestorDB.getInstance(this);
        String [] categorias = dbHelper.obtenerCategorias();

        Bundle datos = this.getIntent().getExtras();
        email = datos.getString("email");

        ListView catView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textRow, categorias);
        catView.setAdapter(adapter);

        catView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String categoria =  ((TextView)view.findViewById(R.id.textRow)).getText().toString();
                siguiente(categoria);
            }
        });

    }

    public void siguiente (String cat){
        Intent intent = new Intent(this, numPreguntas.class);
        intent.putExtra("tipo", "noAleatorio");
        intent.putExtra("categoria", cat);
        intent.putExtra("email", email);
        startActivity(intent);
    }


}