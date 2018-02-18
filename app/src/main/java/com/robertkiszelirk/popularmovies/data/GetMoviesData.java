package com.robertkiszelirk.popularmovies.data;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kiszeli on 2/18/18.
 */

public class GetMoviesData extends AsyncTask<String, Void, ArrayList<MovieData>> {

    private ArrayList<MovieData> moviesList;

    public GetMoviesData(ArrayList<MovieData> moviesList){
        this.moviesList = moviesList;
    }

    @Override
    protected ArrayList<MovieData> doInBackground(String... strings) {

        URL moviesDataUrl = HandleUrls.createMovieListUrl(strings[0]);

        try{
            String moviesJsonData = HandleUrls.getJsonDataFromHttpResponse(moviesDataUrl);
            return JsonParse.jsonParseForMoviesList(moviesJsonData);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> movieData) {
        moviesList = movieData;
    }

}
