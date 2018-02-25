package com.robertkiszelirk.popularmovies.data;

import android.net.Uri;
import com.robertkiszelirk.popularmovies.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/** HandleUrls is responsible to create and use URLs */

public class HandleUrls {

    /** Method to create any downloadable image with a width of 500, from the imagePath */
    public static String createImageUrl(String imagePath){
        return "https://image.tmdb.org/t/p/w500" + imagePath;
    }

    /** Create movies list url based on selectingType string("popular","top_rated")*/
    static URL createMovieListUrl(String selectingType){

        URL movieListUrl = null;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(selectingType)
                .appendQueryParameter("api_key", BuildConfig.API_KEY);

        try{
            movieListUrl = new URL(builder.build().toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return movieListUrl;
    }

    /** Returns the JSON String from a HttpURLConnection */
    static String getJsonDataFromHttpResponse(URL url) throws IOException{

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
