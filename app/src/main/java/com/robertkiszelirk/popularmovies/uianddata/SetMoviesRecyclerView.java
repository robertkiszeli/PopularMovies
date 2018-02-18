package com.robertkiszelirk.popularmovies.uianddata;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.robertkiszelirk.popularmovies.data.GetMoviesData;
import com.robertkiszelirk.popularmovies.data.MovieData;

import java.util.ArrayList;

/**
 * Created by kiszeli on 2/18/18.
 */

public class SetMoviesRecyclerView {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<MovieData> moviesList;

    private GetMoviesData getMoviesData;

    public SetMoviesRecyclerView(){}

    public void setPopularMovies(Context context, RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        this.context = context;

        setLayout();
        getMoviesList("popular");
    }

    private void setLayout() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);

        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void getMoviesList(String string) {

        getMoviesData = new GetMoviesData(moviesList);

        getMoviesData.execute(string);

    }
}
