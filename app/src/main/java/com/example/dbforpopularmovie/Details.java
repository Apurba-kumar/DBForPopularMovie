package com.example.dbforpopularmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {


   Result apur;
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
        Casting Cas;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        RecyclerView newRv=findViewById(R.id.recycle);
        newRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        newRv.setAdapter(new ApurAdapter());
        apur=(Result)getIntent().getSerializableExtra("lag");
        TextView tv1,tv2,tv3;
        tv1=findViewById(R.id.maintitle);
        tv2=findViewById(R.id.rating1);
        tv3=findViewById(R.id.description);
        tv1.setText(apur.getTitle());
        tv2.setText(""+apur.getVoteAverage());
        tv3.setText(apur.getOverview());
        TextView tv4=findViewById(R.id.release);
        tv4.setText(apur.getReleaseDate());
        ImageView ima=findViewById(R.id.main);
        RatingBar r=findViewById(R.id.ratingBar);
        int a=(((int)apur.getVoteAverage())/2);
        r.setRating(a);
        ImageView ima2=findViewById(R.id.cover);
        Glide
                .with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/"+apur.getPosterPath())
                .centerCrop()
                .into(ima);
        Glide
                .with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/"+apur.getBackdropPath())
                .centerCrop()
                .into(ima2);
        ImageView back=findViewById(R.id.bk);

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent apurb=new Intent(Details.this, MainActivity.class);
                startActivity(apurb);
            }
        });
        String castData;
        try {
            castData = run("https://api.themoviedb.org/3/movie/"+apur.getId()+"/credits?api_key=3fa9058382669f72dcb18fb405b7a831");
            Cas =new Gson().fromJson(castData,Casting.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class ApurAdapter extends RecyclerView.Adapter<ApurView>{

        @NonNull
        @Override
        public ApurView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ApurView holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
    class ApurView extends RecyclerView.ViewHolder{

        public ApurView(@NonNull View itemView) {
            super(itemView);

        }
    }
}