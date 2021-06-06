package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PasaporteActivity extends AppCompatActivity {
    private TextView tvdni,tvfullname,tvvacc,tvdatevacc;
    private String fullname, dni, vacc, datevacc, codeText = "";
    private ImageView qrView;
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
        qrView = (ImageView) findViewById(R.id.imgQR);

        tvfullname = (TextView) findViewById(R.id.txtNomApe);
        tvdni = (TextView) findViewById(R.id.txtDNI);
        tvvacc = (TextView) findViewById(R.id.txtVacuna);
        tvdatevacc = (TextView) findViewById(R.id.txtFechaVac);

        Vaccpass_db.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()){
                        fullname = snapshot.child("name").getValue().toString();
                        dni = snapshot.child("dni").getValue().toString();
                        vacc = snapshot.child("vaccine").getValue().toString();
                        datevacc = snapshot.child("date_vaccine").getValue().toString();

                        tvfullname.setText(fullname);
                        tvdni.setText(dni);
                        tvvacc.setText(vacc);
                        tvdatevacc.setText(datevacc);

                        codeText = "Nombre completo: " + fullname + "\n" +
                                   "DNI: " + dni + "\n" +
                                   "Vacuna - Laboratorio: " + vacc + "\n" +
                                   "Fecha de vacunacion: " + datevacc + "\n";

                        QrGenerator(codeText);
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
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE,120,120);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrView.setImageBitmap(bitmap);
        } catch (WriterException we){
            we.printStackTrace();
        }
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