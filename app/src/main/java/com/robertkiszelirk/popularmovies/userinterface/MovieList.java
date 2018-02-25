package com.robertkiszelirk.popularmovies.userinterface;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.GetMoviesData;
import com.robertkiszelirk.popularmovies.data.MovieData;
import com.robertkiszelirk.popularmovies.uianddata.AsyncResponse;
import com.robertkiszelirk.popularmovies.uianddata.MoviesRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/** MoviesList is responsible to show movies list and to select the
 *  popular or top rated ones.
 */

public class MovieList extends AppCompatActivity implements AsyncResponse {

    @BindView(R.id.movies_list_recycler_view) RecyclerView recyclerView;

    @BindView(R.id.movies_list_progress_bar) ProgressBar progressBar;

    @BindView(R.id.movies_list_no_connection_text_view) TextView noConnection;

    SetVisibility setVisibility;

    private static final int REQUEST_PERMISSION = 1;

    private ArrayList<MovieData> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        ButterKnife.bind(this);

        setVisibility = new SetVisibility();

        if(checkInternetConnection()) {
            checkPermissions();

            // Set RecyclerView column numbers
            setRecyclerView();

            // Check if orientation has changed
            if (savedInstanceState != null) {

                moviesList = savedInstanceState.getParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key));
                if (moviesList != null) {
                    loadMoviesToRecyclerView();
                    setVisibility.setVisible(recyclerView);
                    setVisibility.setInvisible(progressBar);
                }else{
                    getMoviesList(getString(R.string.movie_type_popular));
                    setVisibility.setVisible(recyclerView);
                }

            } else {
                getMoviesList(getString(R.string.movie_type_popular));
            }
        }else{
            setVisibility.setInvisible(recyclerView);
            setVisibility.setInvisible(progressBar);
            setVisibility.setVisible(noConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    // Create options menu to select popular or top rated movies
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popular_movies:
                if(checkInternetConnection()) {
                    getMoviesList(getString(R.string.movie_type_popular));
                }else{
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setInvisible(progressBar);
                    setVisibility.setVisible(noConnection);
                }
                break;
            case R.id.top_rated_movies:
                if(checkInternetConnection()) {
                    getMoviesList(getString(R.string.movie_type_top_rated));
                }else{
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setInvisible(progressBar);
                    setVisibility.setVisible(noConnection);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Set RecyclerView grid, columns
    private void setRecyclerView() {

        setVisibility.setInvisible(recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,numberOfColumns());

        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private int numberOfColumns() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Width of the poster
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int columnsNumber = width / widthDivider;
        if (columnsNumber < 2) return 2;

        return columnsNumber;
    }

    // Get movies list in the background
    private void getMoviesList(String listType) {

        GetMoviesData getMoviesData = new GetMoviesData(this);

        getMoviesData.execute(listType);

    }

    // AsyncResponse interface to handle getting back moviesList
    @Override
    public void processFinishData(ArrayList<MovieData> moviesList) {
        if(moviesList != null) {
            this.moviesList = moviesList;
            loadMoviesToRecyclerView();
            setVisibility.setInvisible(progressBar);
            setVisibility.setInvisible(noConnection);
            setVisibility.setVisible(recyclerView);
        }
    }

    // Load movies list to the RecyclerView
    private void loadMoviesToRecyclerView() {

        MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter(moviesList);

        recyclerView.setAdapter(moviesRecyclerAdapter);
    }

    // Save movies list if orientation changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key),moviesList);
    }

    private void checkPermissions() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_PERMISSION
            );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION:{
                if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getMoviesList(getString(R.string.movie_type_popular));
                }else{
                    finish();
                    Toast.makeText(this, R.string.permission_denied_toast_text,Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
