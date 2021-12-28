package com.example.dbforpopularmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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


    Result result;
    Casting Cas;
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
        setContentView(R.layout.activity_details);
        result=(Result)getIntent().getSerializableExtra("result");

        RecyclerView newRv=findViewById(R.id.recycle);

        newRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        newRv.setAdapter(new ApurAdapter());

        TextView tv1,tv2,tv3;
        tv1=findViewById(R.id.maintitle);
        tv2=findViewById(R.id.rating1);
        tv3=findViewById(R.id.description);
        tv1.setText(result.getTitle());
        tv2.setText(""+result.getVoteAverage());
        tv3.setText(result.getOverview());
        TextView tv4=findViewById(R.id.release);
        tv4.setText(result.getReleaseDate());
        ImageView ima=findViewById(R.id.main);
        RatingBar r=findViewById(R.id.ratingBar);
        int a=(((int)result.getVoteAverage())/2);
        r.setRating(a);
        ImageView ima2=findViewById(R.id.cover);
        Glide
                .with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/"+result.getPosterPath())
                .centerCrop()
                .into(ima);
        Glide
                .with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500/"+result.getBackdropPath())
                .centerCrop()
                .into(ima2);
        ImageView back=findViewById(R.id.bk);

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent x=new Intent(Details.this, MainActivity.class);
                startActivity(x);
            }
        });
        String castData;
        try {
            castData = run("https://api.themoviedb.org/3/movie/"+result.getId()+"/credits?api_key=3fa9058382669f72dcb18fb405b7a831");
            Cas =new Gson().fromJson(castData,Casting.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class ApurAdapter extends RecyclerView.Adapter<ApurView>{

        @NonNull
        @Override
        public ApurView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View vv= LayoutInflater.from(Details.this).inflate(R.layout.casting_details,parent,false);
            return new ApurView(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull ApurView holder, int position) {
            holder.castingName.setText(Cas.getCast().get(position).getName());
            Glide
                    .with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500/"+Cas.getCast().get(position).getProfilePath())
                    .centerCrop()
                    .into(holder.castingImg);
        }

        @Override
        public int getItemCount() {
            return Cas.getCast().size();
        }
    }
    class ApurView extends RecyclerView.ViewHolder{
          TextView castingName;
          ImageView castingImg;
          TextView movieRating;
        public ApurView(@NonNull View itemView) {
            super(itemView);
            castingName = itemView.findViewById(R.id.name);
            castingImg=itemView.findViewById(R.id.pic);
            movieRating=itemView.findViewById(R.id.rating1);
        }
    }
}