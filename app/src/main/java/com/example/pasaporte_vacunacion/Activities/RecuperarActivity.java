package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.Activities.Activities.MainActivity;
import com.example.pasaporte_vacunacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarActivity extends AppCompatActivity {
    Button btnRecuperar;
    private EditText edemailRe;
    private String email;
    private FirebaseAuth Auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        Auth = FirebaseAuth.getInstance();
        edemailRe = (EditText)findViewById(R.id.edRemail);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edemailRe.getText().toString();
                if(!email.isEmpty()){
                    IniciarDialog();
                    ReestablecerContraseña();
                }
            }
        });
    }

    public void ReestablecerContraseña(){
        Auth.setLanguageCode("es");
        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Se envió el correo satisfactoriamente!!", Toast.LENGTH_SHORT).show();
                        LoginActivity();
                    } else {
                        Toast.makeText(getApplicationContext(),"No se pudo enviar el correo de reestablecimiento!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mPasaporte:
                Intent intent1 = new Intent(this, PasaporteActivity.class);
                return true;
            case R.id.mNoticias:
                Intent intent2 = new Intent(this, MainActivity.class);
                return true;
            case R.id.mContraseña:
                //Intent intent = new Intent(this, RecuperarActivity.class);
                return true;
            case R.id.mCerrar:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(RecuperarActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Procesando Información ...");
        progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
}