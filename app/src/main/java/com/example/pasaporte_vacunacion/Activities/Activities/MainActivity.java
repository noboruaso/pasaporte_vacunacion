package com.example.pasaporte_vacunacion.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pasaporte_vacunacion.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Noticia> lstNoti;
    RecyclerView rvNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstNoti = new ArrayList<>();
        rvNoticia = (RecyclerView) findViewById(R.id.recyclerId);
        rvNoticia.setLayoutManager(new LinearLayoutManager(this));
        llenarNoticias();
        AdaptadorNoticias adapter = new AdaptadorNoticias(lstNoti);
        rvNoticia.setAdapter(adapter);
    }

    private void llenarNoticias(){
        lstNoti.add(new Noticia("covid 19","fkfjewkfw","wede",R.drawable.img1));
    }
}