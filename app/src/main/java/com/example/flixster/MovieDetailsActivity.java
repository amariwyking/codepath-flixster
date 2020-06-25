package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    TextView tvOverview;
    TextView tvPopularity;

    ImageView ivPoster;

    RatingBar ratingBar;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        tvPopularity = binding.tvPopularity;
        ivPoster = binding.ivPoster;
        ratingBar = binding.ratingBar;


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText(String.format(Locale.US, "%d%%", (int) movie.getPopularity()));
        String imageUrl;

        // if phone is in landscape
        if (binding.getRoot().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // then imageUrl = backdrop image
            imageUrl = movie.getBackdropPath();
        } else {
            // else imageUrl = poster image
            imageUrl = movie.getPosterPath();
        }

//        float rating = (float) movie.getVoteAverage();
        ratingBar.setRating((float) movie.getVoteAverage() / 2);
//        ratingBar.setRating((float) i.getDoubleExtra("voteAverage", 0.0));

        int radius = 20; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        Glide.with(binding.getRoot()).load(imageUrl).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);
    }
}