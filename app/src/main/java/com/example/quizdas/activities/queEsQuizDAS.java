package com.example.quizdas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizdas.R;

public class queEsQuizDAS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que_es);
    }

    /** Called when the user taps the About Us button */
    public void aboutUs(View view){
        finish();
        Intent intent = new Intent(this, aboutUs.class);
        startActivity(intent);
    }
}