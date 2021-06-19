package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarActivity extends AppCompatActivity {
    Button btnRecuperar;
    private EditText edemailRe;
    private String email;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth Auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        Auth = FirebaseAuth.getInstance();
        edemailRe = (EditText)findViewById(R.id.edRemail);

        btnRecuperar = (Button) findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener((v) -> {
                email = edemailRe.getText().toString();
                if(!email.isEmpty()){
                    if (email.trim().matches(emailPattern)) {
                        IniciarDialog();
                        ReestablecerContraseña();
                    } else { Toast.makeText(RecuperarActivity.this,"Correo electrónico inválido!!", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(RecuperarActivity.this,"Ingresar correo electrónico!!", Toast.LENGTH_SHORT).show(); }
        });
    }

    public void ReestablecerContraseña(){
        Auth.setLanguageCode("es");
        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    if (task.isSuccessful()){
                        new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(RecuperarActivity.this,"Se envió el correo satisfactoriamente!!", Toast.LENGTH_SHORT).show();
                                    LoginActivity();
                                    finish();
                                }
                            }, 2000);
                    } else { ToastSetTimeOut("No se pudo enviar el correo de reestablecimiento!!"); }
                } catch (Exception e) { ToastSetTimeOut(e.getLocalizedMessage()); }
            }
        });
    }

    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(RecuperarActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Enviando correo ...");
        progressDialog.show();
    }

    private void ToastSetTimeOut(String message){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RecuperarActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
    }
}