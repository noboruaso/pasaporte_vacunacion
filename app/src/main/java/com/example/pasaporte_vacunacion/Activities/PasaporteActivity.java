package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.Activities.Activities.MainActivity;
import com.example.pasaporte_vacunacion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasaporteActivity extends AppCompatActivity {
    private TextView tvdni,tvfullname,tvvacc,tvdatevacc;
    private DatabaseReference Vaccpass_db;
    private String uid;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasaporte);

        Auth = FirebaseAuth.getInstance();
        uid = Auth.getCurrentUser().getUid();
        Vaccpass_db = FirebaseDatabase.getInstance().getReference();

        tvfullname = (TextView) findViewById(R.id.txtNomApe);
        tvdni = (TextView) findViewById(R.id.txtDNI);
        tvvacc = (TextView) findViewById(R.id.txtVacuna);
        tvdatevacc = (TextView) findViewById(R.id.txtFechaVac);

        Vaccpass_db.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()){
                        tvfullname.setText(snapshot.child("name").getValue().toString());
                        tvdni.setText(snapshot.child("dni").getValue().toString());
                        tvvacc.setText(snapshot.child("vaccine").getValue().toString());
                        tvdatevacc.setText(snapshot.child("date_vaccine").getValue().toString());
                    } else {
                        Toast.makeText(PasaporteActivity.this,"Error al obtener los datos!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PasaporteActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PasaporteActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void QrGenerator(String text){

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

                return true;
            case R.id.mNoticias:
                Intent intent1 = new Intent(this, MainActivity.class);
                return true;
            case R.id.mContraseña:
                Intent intent = new Intent(this, RecuperarActivity.class);
                return true;
            case R.id.mCerrar:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}