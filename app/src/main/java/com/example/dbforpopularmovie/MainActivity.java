package com.example.dbforpopularmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.zip.Inflater;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    PopularMovieRes movieRes;


    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        RecyclerView rv=findViewById(R.id.movie_cardview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rv.setLayoutManager(new GridLayoutManager(this,2));

        rv.setAdapter(new MyAdapter());
        try {
           String data= run("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1&api_key=3fa9058382669f72dcb18fb405b7a831&gt");
        movieRes = new Gson().fromJson(data,PopularMovieRes.class);
         //System.out.println("Total page: "+movieRes.getResults().get(15).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<movieViewHolder>{

        @NonNull
        @Override
        public movieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.movie_cardview,parent,false);
            return new movieViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull movieViewHolder holder, int position) {
           holder.textView.setText(movieRes.getResults().get(position).getTitle());
            holder.rating.setText(""+movieRes.getResults().get(position).getVoteAverage());

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+movieRes.getResults().get(position).getPosterPath())
                    .centerCrop()
                    .into(holder.flagImg);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MainActivity.this,Details.class);
                   intent.putExtra("lag", movieRes.getResults().get(position));
                   startActivity(intent);
               }
           });




            }
        @Override
        public int getItemCount() {
            return movieRes.getResults().size();
        }
    }
}


    class movieViewHolder extends RecyclerView.ViewHolder{
          TextView textView;
          ImageView flagImg;
          TextView rating;
        public movieViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.title);
            flagImg=itemView.findViewById(R.id.poster);
            rating=itemView.findViewById(R.id.rating);
        }
    }
