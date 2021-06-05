package com.example.pasaporte_vacunacion.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.OpenHelper.SQLite_OpenHelper;
import com.example.pasaporte_vacunacion.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private TextView recuperar, registrar;
    private EditText etDni, etPassword;
    private Button btnLogin;
    private DatabaseReference vacuna_pass_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vacuna_pass_db = FirebaseDatabase.getInstance().getReference();

        final SQLite_OpenHelper helper = new SQLite_OpenHelper(this);
        btnLogin = (Button) findViewById(R.id.btnIniciarSesion);
        btnLogin.setOnClickListener((v) -> {

            etDni = (EditText) findViewById(R.id.edDni);
            etPassword = (EditText) findViewById(R.id.edPassword);

            try (Cursor cursor = helper.consultarUsuario(etDni.getText().toString(), etPassword.getText().toString())){
                if(cursor.getCount() > 0){
                    PasaporteActivity();
                    Toast.makeText(getApplicationContext(), "Bienvenido(a) !!", Toast.LENGTH_SHORT).show();
                } else {
                    if(etDni.getText().toString().equals("") && etPassword.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Debes ingresar dni y contraseña!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Dni o contraseña incorrecta!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            etDni.setText("");
            etPassword.setText("");
            etDni.requestFocus();

        });

        recuperar = findViewById(R.id.txtRecuperar);
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarActivity();
            }
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

    public void RecuperarActivity() {
        Intent intent = new Intent(this, RecuperarActivity.class);
        startActivity(intent);
    }

}