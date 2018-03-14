package com.robertkiszelirk.popularmovies.data.AsyncTasks;

import android.os.AsyncTask;

import com.robertkiszelirk.popularmovies.data.HandleUrls;
import com.robertkiszelirk.popularmovies.data.JsonParse;
import com.robertkiszelirk.popularmovies.data.ModelData.MovieData;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForMoviesList;

import java.net.URL;
import java.util.ArrayList;

/** GetMoviesData is responsible to get the movies data from the API
 *  in the background.
 */

public class GetMoviesData extends AsyncTask<String, Void, ArrayList<MovieData>> {

    // Interface to pass movies list back to MovieList activity
    private AsyncResponseForMoviesList delegate = null;

    /** Get AsyncResponseForMoviesList from MovieList */
    public GetMoviesData(AsyncResponseForMoviesList delegate){
        this.delegate = delegate;
    }

    /** Get movies data from API */
    @Override
    protected ArrayList<MovieData> doInBackground(String... strings) {

        ArrayList<MovieData> moviesList;

        // Create the URL based on the passed string[0]
        URL moviesDataUrl = HandleUrls.createMovieListUrl(strings[0]);

        try{
            // Get JSON from HttpURLConnection
            String moviesJsonData = HandleUrls.getJsonDataFromHttpResponse(moviesDataUrl);
            // Return the movie data in an ArrayList<MovieData>
            moviesList = JsonParse.jsonParseForMoviesList(moviesJsonData);

            return moviesList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /** When background task finished, the ArrayList<MovieData> passed back to MovieList */
    @Override
    protected void onPostExecute(ArrayList<MovieData> moviesList) {
        delegate.processFinishMovieData(moviesList);
    }

}
