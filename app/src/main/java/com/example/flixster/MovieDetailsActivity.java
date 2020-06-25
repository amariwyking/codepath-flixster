package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;

import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    TextView tvOverview;
    TextView tvPopularity;

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        tvPopularity = binding.tvPopularity;
        ratingBar = binding.ratingBar;

        Intent i = getIntent();

        tvTitle.setText(i.getStringExtra("title"));
        tvOverview.setText(i.getStringExtra("overview"));
        tvPopularity.setText(String.format(Locale.US, "%d%%", (int) i.getDoubleExtra("popularity", 0.0)));
        String imageUrl = "https://image.tmdb.org/t/p/w342/%s/oCFbh4Mrd0fuGanCgIF6sG27WGD.jpg";

        float rating = (float) i.getDoubleExtra("voteAverage", 0.0);
        ratingBar.setRating(rating / 2);
//        ratingBar.setRating((float) i.getDoubleExtra("voteAverage", 0.0));

    }
}