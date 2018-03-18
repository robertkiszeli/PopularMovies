package com.robertkiszelirk.popularmovies.uianddata.interfaces;

import com.robertkiszelirk.popularmovies.data.modeldata.MovieData;

import java.util.ArrayList;

/** AsyncResponseForFavoriteMoviesList is responsible to pass the movies list from AsyncTask to MovieList */

public interface AsyncResponseForFavoriteMoviesList {
    void processFinishFavoriteMoviesData(ArrayList<MovieData> moviesList);
}
