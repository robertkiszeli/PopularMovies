package com.robertkiszelirk.popularmovies.data;

import android.os.AsyncTask;
import com.robertkiszelirk.popularmovies.uianddata.AsyncResponse;
import java.net.URL;
import java.util.ArrayList;

/** GetMoviesData is responsible to get the movies data from the API
 *  in the background.
 */

public class GetMoviesData extends AsyncTask<String, Void, ArrayList<MovieData>> {

    // Interface to pass movies list back to MovieList activity
    private AsyncResponse delegate = null;

    /** Get AsyncResponse from MovieList */
    public GetMoviesData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    /** Get movies data from API */
    @Override
    protected ArrayList<MovieData> doInBackground(String... strings) {

        // Create the URL based on the passed string[0]
        URL moviesDataUrl = HandleUrls.createMovieListUrl(strings[0]);

        try{
            // Get JSON from HttpURLConnection
            String moviesJsonData = HandleUrls.getJsonDataFromHttpResponse(moviesDataUrl);
            // Return the movie data in an ArrayList<MovieData>
            return JsonParse.jsonParseForMoviesList(moviesJsonData);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /** When background task finished, the ArrayList<MovieData> passed back to MovieList */
    @Override
    protected void onPostExecute(ArrayList<MovieData> moviesList) {
        delegate.processFinishData(moviesList);
    }

}
