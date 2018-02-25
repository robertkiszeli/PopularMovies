package com.robertkiszelirk.popularmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/** JsonParse is responsible to parse the JSON strings to object[s] */

class JsonParse {

    /** Parse the JSON string to ArrayList<MovieData> to store the movies required data
     * and returns the list */
    static ArrayList<MovieData> jsonParseForMoviesList(String jsonString) throws JSONException{

        ArrayList<MovieData> moviesList = new ArrayList<>();

        JSONObject mainData = new JSONObject(jsonString);

        JSONArray moviesJsonArray = mainData.getJSONArray("results");

        for( int i = 0; i < moviesJsonArray.length(); i++){

            JSONObject movie = moviesJsonArray.getJSONObject(i);

            MovieData movieData = new MovieData(
                            movie.getInt("id"),
                            movie.getDouble("vote_average"),
                            movie.getString("title"),
                            movie.getString("poster_path"),
                            movie.getString("backdrop_path"),
                            movie.getString("overview"),
                            movie.getString("release_date")
                    );

            moviesList.add(movieData);

        }

        return moviesList;
    }
}
