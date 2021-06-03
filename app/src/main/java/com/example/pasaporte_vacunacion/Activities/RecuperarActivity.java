package com.example.pasaporte_vacunacion.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.Activities.Activities.MainActivity;
import com.example.pasaporte_vacunacion.R;

public class RecuperarActivity extends AppCompatActivity {
    Button btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Se te ha enviado un correo de recuperación!!", Toast.LENGTH_SHORT).show();
                LoginActivity();
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
}