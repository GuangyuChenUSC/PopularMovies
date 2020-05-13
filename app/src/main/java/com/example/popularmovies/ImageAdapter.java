package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.url.url;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MovieViewHolder> {

    private url[] movies;
    private MovieClickHandler movieClickHandler;

    interface MovieClickHandler {
        void onClick(url movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        final static String IMAGE_URL = "https://image.tmdb.org/t/p/w780";

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(url movie) {
            Picasso.with(super.itemView.getContext()).load(IMAGE_URL + movie.getPoster_path()).into(imageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            url display = movies[position];
            movieClickHandler.onClick(display);
        }
    }

    public ImageAdapter(url[] movies, MovieClickHandler movieClickHandler) {
        this.movies = movies;
        this.movieClickHandler = movieClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.posters, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies[position]);
    }

    public void setMoviesArray(url[] movies) {
        this.movies = movies;
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.length;
    }
}