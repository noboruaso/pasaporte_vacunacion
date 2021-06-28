package com.example.pasaporte_vacunacion.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.pasaporte_vacunacion.Activities.Interfaces.ApiClient;
import com.example.pasaporte_vacunacion.Activities.Interfaces.ApiInterface;
import com.example.pasaporte_vacunacion.Activities.Adapter.Adapter;
import com.example.pasaporte_vacunacion.Beans.Article;
import com.example.pasaporte_vacunacion.Beans.News;
import com.example.pasaporte_vacunacion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "ef2de640a7f049b6a0a6d3b10973e83a";
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button btnYes, btnNo;
    private Dialog dialog;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private FirebaseAuth Auth;
    private String TAG = NewsActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Auth = FirebaseAuth.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipe_refresher);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");
    }

    public void LoadJSON(final String keyword){

        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = "es";
        String q = "covid";

        Call<News> call;
        call = apiInterface.getNews(q,language,API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticle() != null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, NewsActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                    Toast.makeText(NewsActivity.this,"Sin resultados!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        LoadJSON("");
    }

    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJSON(keyword);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mPasaporte:
                Intent intent = new Intent(this, PasaporteActivity.class);
                startActivity(intent);
                return true;
            case R.id.mNoticias:
                return true;
            /* case R.id.mStats:
                //Intent intent1 = new Intent(this, TrackActivity.class);
                //startActivity(intent1);
                Toast.makeText(NewsActivity.this, "Estadísticas COVID", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.mContraseña:
                Intent intent2 = new Intent(this, CambiarContActivity.class);
                startActivity(intent2);
                return true;
            case R.id.mMapa:
                Intent intent3 = new Intent(this, MapsActivity.class);
                startActivity(intent3);
                return true;
            case R.id.mCerrar:
                cerrarSesion();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void cerrarSesion(){
        dialog = new Dialog(NewsActivity.this);
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
}