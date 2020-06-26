package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity_main.xml -> ActivityMainBinding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        setContentView(binding.getRoot());


        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();

        // Initialize the movie adapter
        movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from the movies database api
        getMovies();
    }

    public void getMovies() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i("TAG", "Results: " + results.toString());

                    // Add Movie objects constructed with data set to the list
                    movies.addAll(Movie.fromJSONArray(results));

                    // Notify the adapter so that the recycler view can update
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure() called with: statusCode = [" + statusCode + "], response = [" + response + "], throwable = [" + throwable + "]");
            }
        });

    }
}