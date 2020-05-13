package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popularmovies.url.url;
import com.squareup.picasso.Picasso;

public class movie extends AppCompatActivity {
    final static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    final static String MOVIE_KEY = "movie";
    final static String RELEASED_ON = "Released on: ";
    final static String RATING = "User Ratings: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent fromPrevActivity = getIntent();
        url movie = fromPrevActivity.getParcelableExtra(MOVIE_KEY);

        Picasso.with(this).load(IMAGE_URL + movie.getPoster_path()).into((ImageView) findViewById(R.id.movie_poster));

        TextView plotTextView = (TextView) findViewById(R.id.tv_plot);
        plotTextView.setText(movie.getOverview());

        TextView titleTextView = (TextView) findViewById(R.id.tv_title);
        titleTextView.setText(movie.getOriginal_name());

        TextView releasedOnTextView = (TextView) findViewById(R.id.release_date);
        String releasedOn = RELEASED_ON + movie.getRelease_date();
        releasedOnTextView.setText(releasedOn);

        TextView ratingTextView = (TextView) findViewById(R.id.tv_rating);
        String rating = RATING + movie.getRating();

        ratingTextView.setText(rating);

    }
}
