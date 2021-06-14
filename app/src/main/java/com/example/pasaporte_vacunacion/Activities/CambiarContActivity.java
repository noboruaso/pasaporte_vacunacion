package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CambiarContActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button btnYes, btnNo;
    private Dialog dialog;
    private EditText etnewpass, etrenewpass;
    private String newpass, renewpass;
    private Button btnupdate;
    private ProgressDialog progressDialog;
    private FirebaseAuth Auth;
    private FirebaseUser user;
    private DatabaseReference Vaccpass_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_cont);

        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        Vaccpass_db = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        etnewpass = (EditText) findViewById(R.id.edPassnew);
        etrenewpass = (EditText) findViewById(R.id.edPassrenew);
        btnupdate = (Button) findViewById(R.id.btnUpdatepass);

        btnupdate.setOnClickListener((v) -> {
            newpass = etnewpass.getText().toString();
            renewpass = etrenewpass.getText().toString();

            if(!newpass.isEmpty()){
                if(!renewpass.isEmpty()){
                    if(newpass.length() >= 6 ){
                        if(newpass.equals(renewpass)){
                            IniciarDialog();
                            ActualizarContraseña();
                        } else {
                            Toast.makeText(CambiarContActivity.this,"La confirmación de la nueva contraseña es incorrecta!!", Toast.LENGTH_SHORT).show();
                            etrenewpass.setText("");
                            etrenewpass.requestFocus();
                        }
                    } else {
                        Toast.makeText(CambiarContActivity.this,"La contraseña debe tener mínimo 6 caracteres!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CambiarContActivity.this,"Debe confirmar la nueva contraseña!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CambiarContActivity.this,"Debe ingresar la nueva contraseña!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ActualizarContraseña(){
        user.updatePassword(renewpass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    if(task.isSuccessful()){
                        ToastSetTimeOut("Se ha actualizado correctamente!!");
                        etnewpass.setText("");
                        etrenewpass.setText("");
                    } else {
                        ToastSetTimeOut("Error: No se pudo actualizar!!");
                    }
                } catch (Exception e) { ToastSetTimeOut(e.getLocalizedMessage()); }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mPasaporte:
                Intent intent1 = new Intent(this, PasaporteActivity.class);
                startActivity(intent1);
                return true;
            case R.id.mNoticias:
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                Toast.makeText(CambiarContActivity.this,"Sección de noticias", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mContraseña:
                return true;
            case R.id.mCerrar:  cerrarSesion(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void cerrarSesion(){
        dialog = new Dialog(CambiarContActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCancelable(false);

        btnNo = dialog.findViewById(R.id.btn_cancel);
        btnYes = dialog.findViewById(R.id.btn_accept);

        btnNo.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        btnYes.setOnClickListener((v) -> {
            dialog.dismiss();
            Auth.signOut();
            LoginActivity();
            finish();
        });
    }

    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(CambiarContActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Actualizando contraseña...");
        progressDialog.show();
    }

    private void ToastSetTimeOut(String message){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(CambiarContActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
    }
}