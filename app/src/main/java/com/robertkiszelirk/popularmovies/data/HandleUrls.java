package com.robertkiszelirk.popularmovies.data;

import android.app.Application;
import android.net.Uri;

import com.robertkiszelirk.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by kiszeli on 2/19/18.
 */

public class HandleUrls {

    public static URL createMovieListUrl(String selectingType){

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

    public static String getJsonDataFromHttpResponse(URL url) throws IOException{

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
