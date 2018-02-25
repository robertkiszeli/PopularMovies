package com.robertkiszelirk.popularmovies.uianddata;

import com.robertkiszelirk.popularmovies.data.MovieData;

import java.util.ArrayList;

/** AsyncResponse is responsible to pass the movies list from AsyncTask to MovieList */

public interface AsyncResponse {
    void processFinishData(ArrayList<MovieData> movieData);
}
