package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailsActivity";

    TextView tvTitle;
    TextView tvOverview;
    TextView tvPopularity;

    ImageView ivPoster;
    RelativeLayout rlBanner;

    RatingBar ratingBar;

    Movie movie;

    String videoId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        tvPopularity = binding.tvPopularity;
        ivPoster = binding.ivPoster;
        rlBanner = binding.rlBanner;
        ratingBar = binding.ratingBar;


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPopularity.setText(String.format(Locale.US, "%d%%", (int) movie.getPopularity()));

        String backdropPath = movie.getBackdropPath();

        ratingBar.setRating((float) movie.getVoteAverage() / 2);

        loadImage(binding, backdropPath);
        getTrailer();

        rlBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!videoId.isEmpty()){
                    Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    i.putExtra("videoId", videoId);
                    startActivity(i);
                }
            }
        });
    }

    private void loadImage(com.example.flixster.databinding.ActivityMovieDetailsBinding binding, String imageUrl) {
        int radius = 20; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        Glide.with(binding.getRoot()).load(imageUrl).fitCenter().transform(new RoundedCornersTransformation(radius, margin)).into(ivPoster);
    }

    private void getTrailer() {
//        AsyncHttpClient client = new AsyncHttpClient();
        String videoURL = String.format(Locale.US, "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s", movie.getId(), getString(R.string.api_key_movie_db));

        new AsyncHttpClient().get(videoURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Pull the details of this movie from database
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.d(TAG, "Results: " + results.toString());

                    // Pull the video id from the movie details
                    videoId = results.getJSONObject(0).getString("key");
                    Log.d(TAG, "videoId: " + videoId);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Status: " + statusCode);
            }
        });

    }
}