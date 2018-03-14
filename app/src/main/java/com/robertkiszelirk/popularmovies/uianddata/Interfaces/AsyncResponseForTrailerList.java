package com.robertkiszelirk.popularmovies.uianddata.Interfaces;

import java.util.ArrayList;

/** AsyncResponseForTrailerList is responsible to pass the movies list from AsyncTask to MovieDetails */

public interface AsyncResponseForTrailerList {
    void processFinishTrailerData(ArrayList<String> trailerData);
}
