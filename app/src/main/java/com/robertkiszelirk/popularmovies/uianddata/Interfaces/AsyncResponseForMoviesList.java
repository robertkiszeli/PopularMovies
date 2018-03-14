package com.robertkiszelirk.popularmovies.uianddata.Interfaces;

import com.robertkiszelirk.popularmovies.data.ModelData.MovieData;

import java.util.ArrayList;

/** AsyncResponseForMoviesList is responsible to pass the movies list from AsyncTask to MovieList */

public interface AsyncResponseForMoviesList {
    void processFinishMovieData(ArrayList<MovieData> movieData);
}
