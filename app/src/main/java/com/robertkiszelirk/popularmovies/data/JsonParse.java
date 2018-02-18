package com.robertkiszelirk.popularmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kiszeli on 2/19/18.
 */

public class JsonParse {

    public static ArrayList<MovieData> jsonParseForMoviesList(String jsonString) throws JSONException{

        ArrayList<MovieData> moviesList = new ArrayList<>();

        JSONObject mainData = new JSONObject(jsonString);

        JSONArray moviesJsonArray = mainData.getJSONArray("results");

        for( int i = 0; i < moviesJsonArray.length(); i++){

            JSONObject movie = moviesJsonArray.getJSONObject(i);

            MovieData movieData = new MovieData(
                            movie.getInt("id"),
                            movie.getLong("vote_average"),
                            movie.getString("title"),
                            movie.getString("poster_path"),
                            movie.getString("overview"),
                            movie.getString("release_date")
                    );

            moviesList.add(movieData);

        }

        return moviesList;
    }
}
