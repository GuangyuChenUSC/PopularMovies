package com.example.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.url.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ImageAdapter.MovieClickHandler {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    TextView mMessageTextView;
    final static String MOVIE_KEY = "movie";
    final static String TITLE_KEY = "original_title";
    final static String PLOT_KEY = "overview";
    final static String RELEASE_DATE_KEY = "release_date";
    final static String POSTER_KEY = "poster_path";
    final static String RATING_KEY = "vote_average";

    final static String LOG_TAG = "MOVIE_DB_API_ERROR";

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.movies_poster);
        mMessageTextView = (TextView) findViewById(R.id.tv_message);
        mProgressBar = (ProgressBar) findViewById(R.id.show_progress);

        if(!isNetworkAvailable()) {
            showMessageText(getResources().getString(R.string.show_network_error_message));
        }
        showProgressBar();
        if(mRecyclerView.getAdapter() != null) {
            ((ImageAdapter) mRecyclerView.getAdapter()).setMoviesArray(null);
            mRecyclerView.getAdapter().notifyDataSetChanged();

        }
        new AsyncTask<String, Void, url[]>() {

            @Override
            protected void onProgressUpdate(Void... values) {
            }

            @Override
            protected url[] doInBackground(String... strings) {
                List<url> movieList = new ArrayList<>();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(strings[0]+strings[1]).build();
                try (Response response = client.newCall(request).execute()) {
                    String jsonData =  response.body().string();
                    JSONObject object = new JSONObject(jsonData);
                    JSONArray results = object.getJSONArray("results");
                    for(int i = 0; i < results.length(); ++i) {
                        JSONObject movie = results.getJSONObject(i);
                        String original_name = movie.getString(TITLE_KEY);
                        String overview = movie.getString(PLOT_KEY);
                        String release_date = movie.getString(RELEASE_DATE_KEY);
                        String poster_path = movie.getString(POSTER_KEY);
                        String rating = movie.getString(RATING_KEY);
                        movieList.add(new url(original_name, overview, release_date, poster_path,rating));
                    }
                } catch (IOException | JSONException e) {
                    Log.e(LOG_TAG, Objects.requireNonNull(e.getLocalizedMessage()));
                }

                return (url[]) movieList.toArray(new url[] {});
            }

            @Override
            protected void onPostExecute(url[] movies) {
                mRecyclerView.setAdapter(new ImageAdapter(movies, MainActivity.this));
                GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
                showRecyclerView();
            }

        }.execute(getResources().getString(R.string.popular_url), getResources().getString(R.string.api));
    }

    private boolean isNetworkAvailable() {
        boolean returnValue = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showMessageText(String message) {
        mMessageTextView.setText(message);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mMessageTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sort_movies, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        switch (itemSelected) {
            case R.id.sort_by_popular:
                if(!isNetworkAvailable()) {
                    showMessageText(getResources().getString(R.string.show_network_error_message));
                    break;
                }
                showProgressBar();
                if(mRecyclerView.getAdapter() != null) {
                    ((ImageAdapter) mRecyclerView.getAdapter()).setMoviesArray(null);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    actionBar.setTitle("Popular Movies");
                }
                new AsyncTask<String, Void, url[]> () {
                    @Override
                    protected void onProgressUpdate(Void... values) {
                    }
                    @Override
                    protected url[] doInBackground(String... strings) {
                        List<url> movieList = new ArrayList<>();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(strings[0]+strings[1]).build();
                        try (Response response = client.newCall(request).execute()) {
                            String jsonData =  response.body().string();
                            JSONObject object = new JSONObject(jsonData);
                            JSONArray results = object.getJSONArray("results");
                            for(int i = 0; i < results.length(); ++i) {
                                JSONObject movie = results.getJSONObject(i);
                                String original_name = movie.getString(TITLE_KEY);
                                String overview = movie.getString(PLOT_KEY);
                                String release_date = movie.getString(RELEASE_DATE_KEY);
                                String poster_path = movie.getString(POSTER_KEY);
                                String rating = movie.getString(RATING_KEY);
                                movieList.add(new url(original_name, overview, release_date, poster_path,rating));

                            }
                        } catch (IOException | JSONException e) {
                            Log.e(LOG_TAG, Objects.requireNonNull(e.getLocalizedMessage()));
                        }

                        return (url[]) movieList.toArray(new url[] {});
                    }

                    @Override
                    protected void onPostExecute(url[] movies) {
                        mRecyclerView.setAdapter(new ImageAdapter(movies, MainActivity.this));
                        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), 2);
                        mRecyclerView.setLayoutManager(layoutManager);
                        showRecyclerView();
                    }

                }.execute(getResources().getString(R.string.popular_url), getResources().getString(R.string.api));
                break;
            case R.id.sort_by_top_rated:
                if(!isNetworkAvailable()) {
                    showMessageText(getResources().getString(R.string.show_network_error_message));
                    break;
                }
                showProgressBar();
                if(mRecyclerView.getAdapter() != null) {
                    ((ImageAdapter) mRecyclerView.getAdapter()).setMoviesArray(null);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    actionBar.setTitle("Top Rated Movies");
                }
                new AsyncTask<String, Void, url[]> () {

                    @Override
                    protected void onProgressUpdate(Void... values) {
                    }

                    @Override
                    protected url[] doInBackground(String... strings) {
                        List<url> movieList = new ArrayList<>();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(strings[0]+strings[1]).build();
                        try (Response response = client.newCall(request).execute()) {
                            String jsonData =  response.body().string();
                            JSONObject object = new JSONObject(jsonData);
                            JSONArray results = object.getJSONArray("results");
                            for(int i = 0; i < results.length(); ++i) {
                                JSONObject movie = results.getJSONObject(i);
                                String original_name = movie.getString(TITLE_KEY);
                                String overview = movie.getString(PLOT_KEY);
                                String release_date = movie.getString(RELEASE_DATE_KEY);
                                String poster_path = movie.getString(POSTER_KEY);
                                String rating = movie.getString(RATING_KEY);
                                movieList.add(new url(original_name, overview, release_date, poster_path,rating));
                            }
                        } catch (IOException | JSONException e) {
                            Log.e(LOG_TAG, Objects.requireNonNull(e.getLocalizedMessage()));
                        }
                        return (url[]) movieList.toArray(new url[] {});
                    }

                    @Override
                    protected void onPostExecute(url[] movies) {
                        mRecyclerView.setAdapter(new ImageAdapter(movies, MainActivity.this));
                        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), 2);
                        mRecyclerView.setLayoutManager(layoutManager);
                        showRecyclerView();
                    }
                }.execute(getResources().getString(R.string.top_rated_url), getResources().getString(R.string.api));
                break;
        }
        return true;
    }

    @Override
    public void onClick(url movie) {
        Intent displayActivity = new Intent(this, movie.class);
        displayActivity.putExtra(MOVIE_KEY, movie);
        startActivity(displayActivity);
    }
}
