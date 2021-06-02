package com.example.pasaporte_vacunacion.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.pasaporte_vacunacion.R;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {
    private DatePickerDialog CadPickerDialog, NacPickerDialog;
    private Button fechaCad, fechaNac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initDateCad();
        initDateNac();
        fechaCad = findViewById(R.id.btnFechaCad);
        fechaNac = findViewById(R.id.btnFechaNac);

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