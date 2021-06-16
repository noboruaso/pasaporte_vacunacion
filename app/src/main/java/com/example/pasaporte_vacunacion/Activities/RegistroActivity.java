package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.Activities.Interfaces.PersonaAPI;
import com.example.pasaporte_vacunacion.Beans.Persona;
import com.example.pasaporte_vacunacion.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {
    private DatePickerDialog CadPickerDialog, NacPickerDialog;
    private Dialog tDialog;
    private TextView txtTerminos;
    private Button fechaCad, fechaNac, btnRegistrar;
    private EditText etdni, etcorreo, etpassword, etrepassword;
    private RadioButton rdF, rdM;
    private CheckBox cbaceptar;
    private String BASE_URL = "https://consulta.api-peru.com/";
    private String BASE_URL_2 = "https://dni.optimizeperu.com/";
    private ProgressDialog progressDialog;
    private String dni, vacuna, dateBirth, genero, dateExpiration, dateVaccine, email, password, hashPassword, repass = "";
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String fullName = "Persona no identificada";
    private String token = "k4d2956bd531ab61d44f4fa07304b20e13913815";
    FirebaseAuth Auth;
    DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Auth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference();

        initDateCad();
        initDateNac();
        fechaCad = findViewById(R.id.btnFechaCad);
        //fechaNac = findViewById(R.id.btnFechaNac);

        etdni = (EditText) findViewById(R.id.edDNI);
        etcorreo = (EditText) findViewById(R.id.edCorreo);
        etpassword = (EditText) findViewById(R.id.edContraseña);
        etrepassword = (EditText) findViewById(R.id.edContraseá2);
        cbaceptar = (CheckBox) findViewById(R.id.checkAceptar);
        rdF = (RadioButton) findViewById(R.id.rbWomen);
        rdM = (RadioButton) findViewById(R.id.rbMen);

        /*Términos y condiciones */
        txtTerminos = (TextView) findViewById(R.id.txtTerminos);
        tDialog = new Dialog(this);
        String texto = "Acepto los términos y condiciones";
        SpannableString ss = new SpannableString(texto);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                tDialog.setContentView(R.layout.popup_terminos);
                tDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tDialog.getWindow().setLayout(1400, ViewGroup.LayoutParams.WRAP_CONTENT);
                tDialog.show();
                //tDialog.setCancelable(true);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };
        ss.setSpan(clickableSpan,11, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTerminos.setText(ss);
        txtTerminos.setMovementMethod(LinkMovementMethod.getInstance());
        /*Fin términos y condiciones*/


        etdni.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = etdni.getText().toString();
                email = etcorreo.getText().toString();
                password = etpassword.getText().toString();
                repass = etrepassword.getText().toString();
                //dateBirth = fechaNac.getText().toString();
                dateExpiration = fechaCad.getText().toString();
                if(rdM.isChecked()){ genero = "Masculino"; }
                if(rdF.isChecked()){ genero = "Femenino"; }

                if(!dni.isEmpty()){
                    if(dni.length() >= 8){
                        if(rdF.isChecked() || rdM.isChecked()){
                            if(!email.isEmpty()){
                                if(email.trim().matches(emailPattern)) {
                                    if(!password.isEmpty()){
                                        if(password.length() >= 6) {
                                            if(!repass.isEmpty()){
                                                if(password.equals(repass)) {
                                                    if (cbaceptar.isChecked()) {
                                                        ValidarDni();
                                                    } else {
                                                        Toast.makeText(RegistroActivity.this, "Debe aceptar los términos y condiciones para registrarse!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else{
                                                    Toast.makeText(RegistroActivity.this,"Las contraseñas no coinciden!!", Toast.LENGTH_SHORT).show();
                                                    etrepassword.setText("");
                                                }
                                            } else {
                                                Toast.makeText(RegistroActivity.this,"Debe completar el campo de confirmación de contraseña!!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(RegistroActivity.this,"La contraseña debe tener mínimo 6 caracteres!!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegistroActivity.this,"Debe completar el campo de contraseña!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegistroActivity.this,"Correo electrónico inválido!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegistroActivity.this,"Debe completar el campo de correo electrónico!!", Toast.LENGTH_SHORT).show();
                            }
                        }  else {
                            Toast.makeText(RegistroActivity.this,"Debe seleccionar su sexo!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this,"El dni requiere de 8 digitos!!", Toast.LENGTH_SHORT).show();
                        etdni.setText("");
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "Debe completar el campo de dni!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ValidarDni(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PersonaAPI personaAPI = retrofit.create(PersonaAPI.class);
        Call<Persona> call = personaAPI.find(dni);
        call.enqueue(new Callback<Persona>() {
            @Override
            public void onResponse(Call<Persona> call, retrofit2.Response<Persona> response) {
                if(response.isSuccessful()){
                    try {
                        Persona p = response.body();
                        if(dni.equals(p.getData().getNumero())){
                            fullName = p.getData().getNombre_completo();
                        } else {
                            fullName = "Usuario por defecto";
                        }
                        IniciarDialog();
                        registrarUsuario();
                    } catch (Exception e){
                        Toast.makeText(RegistroActivity.this,"DNI no existe o consulta directa no disponible", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Persona> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void IniciarDialog(){
        progressDialog = new ProgressDialog(RegistroActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Procesando Información ...");
        progressDialog.show();
    }

    private void ToastSetTimeOut(String message){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RegistroActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
    }

    private void registrarUsuario(){
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    vacuna = vacunaGenerate();
                    dateVaccine = vacunaDateGenerate();
                    if(vacuna.equals("NO")){
                        dateVaccine = "- - -";
                    }
                    hashPassword = BCrypt.withDefaults().hashToString(12,password.toCharArray());

                    Map<String, Object> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password",hashPassword);
                    map.put("dni", dni);
                    map.put("name",fullName);
                    map.put("vaccine",vacuna);
                    map.put("gender", genero);
                    //map.put("date_birth",dateBirth);
                    map.put("date_issue",dateExpiration);
                    map.put("date_vaccine",dateVaccine);

                    String id = Auth.getCurrentUser().getUid();
                    Database.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            try {
                                if (task2.isSuccessful()){
                                    new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistroActivity.this, "Se ha registrado satisfactoriamente!!", Toast.LENGTH_SHORT).show();
                                                LoginActivity();
                                                finish();
                                            }
                                        }, 2000);
                                } else { ToastSetTimeOut("No se pudo crear los datos correctamente!!"); }
                            } catch (Exception e){ ToastSetTimeOut(e.getLocalizedMessage()); }
                        }
                    });
                } else { ToastSetTimeOut("No se pudo registrar al usuario!!"); }
            }
        });
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

    public String vacunaGenerate() {
        String[] vacc = {
                    "AZD1222 - Oxford/AstraZeneca",
                    "BBIBP-CorV - Sinopharm",
                    "BNTI62b2 - BioNTech/Pfizer",
                    "Covaxina - Bharat Biotech",
                    "Covishield - Instituto de suero de la india",
                    "mRNA-1273 - Moderna",
                    "Sputnik V - Gamaleya",
                    "NO"
                };
        Random rand = new Random();
        int index = rand.nextInt(8);
        return vacc [index];
    }

    public String vacunaDateGenerate(){
        String[] mesA = { "JUL","AGO","SEP","OCT","NOV","DIC" };
        String[] mesB = { "ENE","FEB","MAR","ABR","MAY","JUN", };
        String mes = "";
        int[] año = {2020, 2021};
        Random rand = new Random();

        int iAño = rand.nextInt(2);
        int dia = (int)(Math.random()*28+1);
        int iMes = rand.nextInt(6);
        if(año[iAño] == 2020){  mes = mesA[iMes];
        } else { mes = mesB[iMes]; }
        return mes + " " + dia + " " + año[iAño];
    }
}