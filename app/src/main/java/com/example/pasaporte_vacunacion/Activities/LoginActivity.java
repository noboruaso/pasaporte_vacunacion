package com.example.pasaporte_vacunacion.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.OpenHelper.SQLite_OpenHelper;
import com.example.pasaporte_vacunacion.R;

public class LoginActivity extends AppCompatActivity {
    private TextView recuperar, registrar;
    private EditText etDni, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SQLite_OpenHelper helper = new SQLite_OpenHelper(this);
        btnLogin = (Button) findViewById(R.id.btnIniciarSesion);
        btnLogin.setOnClickListener((v) -> {
            etDni = (EditText) findViewById(R.id.edDni);
            etPassword = (EditText) findViewById(R.id.edPassword);

            try (Cursor cursor = helper.consultarUsuario(etDni.getText().toString(), etPassword.getText().toString())){
                if(cursor.getCount() > 0){
                    PasaporteActivity();
                    Toast.makeText(getApplicationContext(), "Bienvenido!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o password incorrecta!!", Toast.LENGTH_SHORT).show();
                }
            }
            etDni.setText("");
            etPassword.setText("");
            etDni.requestFocus();
        });

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

    public void PasaporteActivity() {
        Intent intent = new Intent(this, PasaporteActivity.class);
        startActivity(intent);
    }
}