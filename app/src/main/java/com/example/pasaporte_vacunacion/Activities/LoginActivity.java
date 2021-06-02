package com.example.pasaporte_vacunacion.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pasaporte_vacunacion.R;

public class LoginActivity extends AppCompatActivity {
    private TextView recuperar, registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrar = findViewById(R.id.txtRegistrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroActivity();
            }
        });
    }

    public void RegistroActivity() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
}