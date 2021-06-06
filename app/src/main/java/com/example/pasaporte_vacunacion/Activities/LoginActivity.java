package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    private DatabaseReference db;
    private ProgressDialog progressDialog;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseDatabase.getInstance().getReference();
        Auth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.edEmail);
        etPassword = (EditText) findViewById(R.id.edPassword);
        recuperar = (TextView) findViewById(R.id.txtRecuperar);
        registrar = (TextView) findViewById(R.id.txtRegistrar);

        btnLogin = (Button) findViewById(R.id.btnIniciarSesion);
        btnLogin.setOnClickListener((v) -> {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            if(!email.isEmpty()) {
                if(!password.isEmpty()){
                    if (email.trim().matches(emailPattern)) {
                        IniciarDialog();
                        loginUsuario();
                    } else { Toast.makeText(LoginActivity.this,"Correo electrónico inválido!!", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(LoginActivity.this,"Ingresar contraseña!!", Toast.LENGTH_SHORT).show(); }
            } else { Toast.makeText(LoginActivity.this,"Ingresar correo electrónico!!", Toast.LENGTH_SHORT).show(); }
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
                        new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    PasaporteActivity();
                                    Toast.makeText(LoginActivity.this, "Bienvenido(a)!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, 2000);
                    } else {
                        ToastSetTimeOut("No se pudo iniciar sesión. Verifique sus credenciales!!");
                        etEmail.requestFocus();
                    }
                } catch (Exception e) { ToastSetTimeOut(e.getLocalizedMessage()); }
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
        progressDialog.setMessage("Cargando ...");
        progressDialog.show();
    }

    private void ToastSetTimeOut(String message){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
    }
}