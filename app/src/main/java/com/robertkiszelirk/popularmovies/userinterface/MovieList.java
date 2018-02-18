package com.robertkiszelirk.popularmovies.userinterface;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.databinding.MovieListBinding;
import com.robertkiszelirk.popularmovies.uianddata.SetMoviesRecyclerView;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;

public class MovieList extends AppCompatActivity {

    MovieListBinding movieListBinding;

    SetVisibility setVisibility;

    SetMoviesRecyclerView setMoviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        movieListBinding = DataBindingUtil.setContentView(this,R.layout.movie_list);

        setVisibility = new SetVisibility();

        setVisibility.setInvisible(movieListBinding.moviesListRecyclerView);

        setMoviesRecyclerView = new SetMoviesRecyclerView();

        setMoviesRecyclerView.setPopularMovies(this, movieListBinding.moviesListRecyclerView);

    }

}
