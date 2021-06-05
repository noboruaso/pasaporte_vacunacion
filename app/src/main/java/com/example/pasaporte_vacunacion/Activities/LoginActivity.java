package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private TextView recuperar, registrar;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String email, password;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private DatabaseReference vacuna_pass_db;
    private ProgressDialog progressDialog;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vacuna_pass_db = FirebaseDatabase.getInstance().getReference();
        Auth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.edEmail);
        etPassword = (EditText) findViewById(R.id.edPassword);

        recuperar = findViewById(R.id.txtRecuperar);
        registrar = findViewById(R.id.txtRegistrar);

        btnLogin = (Button) findViewById(R.id.btnIniciarSesion);
        btnLogin.setOnClickListener((v) -> {

            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if(!email.isEmpty()) {
                if(!password.isEmpty()){
                    if (email.trim().matches(emailPattern)) {
                        IniciarDialog();
                        loginUsuario();

                        etEmail.setText("");
                        etPassword.setText("");
                        etEmail.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(),"Correo electrónico inválido!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Ingresar contraseña!!",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Ingresar correo electrónico!!",Toast.LENGTH_SHORT).show();
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarActivity();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroActivity();
            }
        });
    }

    public void loginUsuario(){
        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        PasaporteActivity();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión. Verifique sus credenciales!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
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

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Procesando Información ...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}