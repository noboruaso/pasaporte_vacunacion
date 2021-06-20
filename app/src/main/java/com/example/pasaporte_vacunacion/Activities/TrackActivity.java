package com.example.pasaporte_vacunacion.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasaporte_vacunacion.Activities.Interfaces.ApiCountryData;
import com.example.pasaporte_vacunacion.Activities.Interfaces.ApiTrackUtilities;
import com.example.pasaporte_vacunacion.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends AppCompatActivity {
    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTest;
    private TextView todayConfirm, todayRecovered, todayDeath, dateTrack;
    private ArrayList<ApiCountryData> list;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        list = new ArrayList<>();
        ApiTrackUtilities.getApiTrackInterface().getApiCountryData()
                .enqueue(new Callback<ArrayList<ApiCountryData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ApiCountryData>> call, Response<ArrayList<ApiCountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i < list.size(); i ++){
                            if (list.get(i).getCountry().equals("Peru")){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int test = Integer.parseInt(list.get(i).getTests());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalTest.setText(NumberFormat.getInstance().format(test));
                                totalDeath.setText(NumberFormat.getInstance().format(death));

                                //todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                //todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                //todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));

                                setDateText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirmados", confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Activos", active,getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Pruebas", test,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Muertos", death,getResources().getColor(R.color.red_pie)));
                                pieChart.startAnimation();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ApiCountryData>> call, Throwable t) {
                        Toast.makeText(TrackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        init();
    }

    private void setDateText(String updated){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        long milliseconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        dateTrack.setText("Actualizado al "+format.format(calendar.getTime()));
    }

    private void init(){
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalTest = findViewById(R.id.totalTest);
        totalDeath = findViewById(R.id.totalDeath);
        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        pieChart = findViewById(R.id.piechart);
        dateTrack = findViewById(R.id.date_track);
    }
}