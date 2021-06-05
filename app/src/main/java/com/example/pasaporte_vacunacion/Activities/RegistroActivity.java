package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.pasaporte_vacunacion.Activities.Interfaces.PersonaAPI;
import com.example.pasaporte_vacunacion.BD.Estructura_BD;
import com.example.pasaporte_vacunacion.Beans.Persona;
import com.example.pasaporte_vacunacion.OpenHelper.SQLite_OpenHelper;
import com.example.pasaporte_vacunacion.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {
    private DatePickerDialog CadPickerDialog, NacPickerDialog;
    private Button fechaCad, fechaNac, btnRegistrar;
    private EditText etdni, etcorreo, etpassword, etrepassword;
    private CheckBox cbaceptar;
    private ProgressDialog progressDialog;
    private String api_principal = "https://dni.optimizeperu.com/api/";
    private String api = "";
    private Integer estado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initDateCad();
        initDateNac();
        fechaCad = findViewById(R.id.btnFechaCad);
        fechaNac = findViewById(R.id.btnFechaNac);

        etdni = (EditText) findViewById(R.id.edDNI);
        etcorreo = (EditText) findViewById(R.id.edCorreo);
        etpassword = (EditText) findViewById(R.id.edContraseña);
        etrepassword = (EditText) findViewById(R.id.edContraseá2);
        cbaceptar = (CheckBox) findViewById(R.id.checkAceptar);

        api = api_principal + "persons/";
        etdni.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        //final SQLite_OpenHelper helper = new SQLite_OpenHelper(this);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etdni.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Debe completar el campo de dni!!", Toast.LENGTH_SHORT).show();
                } else {
                    if(etcorreo.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Debe completar el campo de correo electrónico!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(etpassword.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Debe completar el campo de contraseña!!", Toast.LENGTH_SHORT).show();
                        } else {
                            if(etrepassword.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(),"Debe completar el campo de confirmación de contraseña!!", Toast.LENGTH_SHORT).show();
                            } else {
                                if(etpassword.getText().toString().equals(etrepassword.getText().toString())) {
                                    if (cbaceptar.isChecked()) {
                                        /*
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(Estructura_BD.NOMBRE_COLUMN2,etdni.getText().toString());
                                        values.put(Estructura_BD.NOMBRE_COLUMN3,etcorreo.getText().toString());
                                        values.put(Estructura_BD.NOMBRE_COLUMN4,etpassword.getText().toString());
                                        db.insert(Estructura_BD.TABLE_NAME, null,values);
                                         */
                                        IniciarDialog();
                                        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dni.optimizeperu.com/")
                                                .addConverterFactory(GsonConverterFactory.create()).build();
                                        PersonaAPI personaAPI = retrofit.create(PersonaAPI.class);
                                        Call<Persona> call = personaAPI.find(etdni.getText().toString());
                                        call.enqueue(new Callback<Persona>() {
                                            @Override
                                            public void onResponse(Call<Persona> call, retrofit2.Response<Persona> response) {
                                                try {
                                                    if(response.isSuccessful()){
                                                        Persona p = response.body();
                                                        //txtnombre.setText(p.getName());
                                                        if(etdni.getText().toString().equals(p.getDni())){
                                                            progressDialog.dismiss();
                                                            estado = 1;
                                                            Toast.makeText(getApplicationContext(),"Se ha registrado satisfactoriamente!!", Toast.LENGTH_SHORT).show();
                                                            //  LoginActivity();
                                                        } else {
                                                            progressDialog.dismiss();
                                                            estado = 0;
                                                            Toast.makeText(RegistroActivity.this, "El DNI ingresado no es válido!", Toast.LENGTH_SHORT).show();
                                                            etdni.setText("");
                                                        }
                                                    }
                                                } catch (Exception e){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegistroActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<Persona> call, Throwable t) {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistroActivity.this, "Ups! Error de conexión.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Debe aceptar los términos y condiciones para registrarse!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden!!", Toast.LENGTH_SHORT).show();
                                    etrepassword.setText("");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(RegistroActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Procesando Información ...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void LoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void initDateCad() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                mes = mes + 1;
                String fecha = StringFecha(dia, mes, año);
                fechaCad.setText(fecha);
            }
        };

        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        int estilo = AlertDialog.THEME_HOLO_LIGHT;

        CadPickerDialog = new DatePickerDialog(this, estilo, dateSetListener, año, mes, dia);
    }

    private void initDateNac() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                mes = mes + 1;
                String fecha = StringFecha(dia, mes, año);
                fechaNac.setText(fecha);
            }
        };

        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        int estilo = AlertDialog.THEME_HOLO_LIGHT;

        NacPickerDialog = new DatePickerDialog(this, estilo, dateSetListener, año, mes, dia);
    }

    private String StringFecha(int dia, int mes, int año) {
        return getFormatoMes(mes) + " " + dia + " " + año;
    }

    private String getFormatoMes(int mes) {
        if(mes == 1)
            return "ENE";
        if(mes == 2)
            return "FEB";
        if(mes == 3)
            return "MAR";
        if(mes == 4)
            return "ABR";
        if(mes == 5)
            return "MAY";
        if(mes == 6)
            return "JUN";
        if(mes == 7)
            return "JUL";
        if(mes == 8)
            return "AGO";
        if(mes == 9)
            return "SEP";
        if(mes == 10)
            return "OCT";
        if(mes == 11)
            return "NOV";
        if(mes == 12)
            return "DIC";

        //default should never happen
        return "ENE";
    }

    public void abrirDateCad(View view) {
        CadPickerDialog.show();
    }

    public void abrirDateNac(View view) {
        NacPickerDialog.show();
    }
}