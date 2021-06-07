package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.pasaporte_vacunacion.Activities.Activities.MainActivity;
import com.example.pasaporte_vacunacion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PasaporteActivity extends AppCompatActivity {
    private TextView tvdni,tvfullname,tvvacc,tvdatevacc;
    private String fullname, dni, vacc, datevacc, gender, codeText = "";
    private Button btnYes, btnNo;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView qrView, fotoUser;
    private Dialog dialog;
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

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        qrView = (ImageView) findViewById(R.id.imgQR);
        fotoUser = (ImageView) findViewById(R.id.imgFoto);

        tvfullname = (TextView) findViewById(R.id.txtNomApe);
        tvdni = (TextView) findViewById(R.id.txtDNI);
        tvvacc = (TextView) findViewById(R.id.txtVacuna);
        tvdatevacc = (TextView) findViewById(R.id.txtFechaVac);

        Vaccpass_db.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()){
                        gender = snapshot.child("gender").getValue().toString();
                        fullname = snapshot.child("name").getValue().toString();
                        dni = snapshot.child("dni").getValue().toString();
                        vacc = snapshot.child("vaccine").getValue().toString();
                        datevacc = snapshot.child("date_vaccine").getValue().toString();

                        if(gender.equals("Femenino")){
                            fotoUser.setImageResource(R.mipmap.ic_woman);
                        } else { fotoUser.setImageResource(R.mipmap.ic_man); }

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
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mPasaporte:
                return true;
            case R.id.mNoticias:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.mContraseña:
                Intent intent1 = new Intent(this, RecuperarActivity.class);
                startActivity(intent1);
                return true;
            case R.id.mCerrar:  cerrarSesion(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void cerrarSesion(){
        dialog = new Dialog(PasaporteActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(1000,ViewGroup.LayoutParams.WRAP_CONTENT);
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
}