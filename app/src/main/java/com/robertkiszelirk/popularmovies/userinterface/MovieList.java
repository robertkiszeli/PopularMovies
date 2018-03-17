package com.robertkiszelirk.popularmovies.userinterface;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetFavoritesData;
import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetMoviesData;
import com.robertkiszelirk.popularmovies.data.ModelData.MovieData;
import com.robertkiszelirk.popularmovies.uianddata.HandleFavorites;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForFavoriteMoviesList;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForMoviesList;
import com.robertkiszelirk.popularmovies.uianddata.Adapters.MoviesRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/** MoviesList is responsible to show movies list and to select the
 *  popular or top rated ones.
 */

public class MovieList extends AppCompatActivity implements AsyncResponseForMoviesList,AsyncResponseForFavoriteMoviesList {

    @BindView(R.id.movies_list_recycler_view) RecyclerView recyclerView;

    @BindView(R.id.movies_list_progress_bar) ProgressBar progressBar;

    @BindView(R.id.movies_list_no_connection_text_view) TextView noConnection;

    private SetVisibility setVisibility;

    private ArrayList<MovieData> moviesList;

    private String currentList;

    private int numberOfFavoriteMovies = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        ButterKnife.bind(this);

        setVisibility = new SetVisibility();

        currentList = getString(R.string.movie_type_popular);

        if(checkInternetConnection()) {
            //checkPermissions();

            // Set RecyclerView column numbers
            setRecyclerView();

            // Check if orientation has changed
            if (savedInstanceState != null) {

                if("favorite".equals(savedInstanceState.getString("currentList"))){

                    moviesList = savedInstanceState.getParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key));

                    numberOfFavoriteMovies = savedInstanceState.getInt("numberOfFavoriteMovies",0);

                    HandleFavorites handleFavorites = new HandleFavorites(this);

                    ArrayList<String> favoriteMoviesIdList = handleFavorites.getFavoriteMoviesId();

                    // Check if favorite movies database changed
                    if(numberOfFavoriteMovies != favoriteMoviesIdList.size()) {
                        numberOfFavoriteMovies = favoriteMoviesIdList.size();

                        setVisibility.setInvisible(recyclerView);
                        setVisibility.setVisible(progressBar);
                        setRecyclerView();
                        getMoviesList(getString(R.string.movie_type_favorite));
                    }else{
                        if (moviesList != null) {
                            loadMoviesToRecyclerView();
                            setVisibility.setVisible(recyclerView);
                            setVisibility.setInvisible(progressBar);
                        } else {
                            getMoviesList(getString(R.string.movie_type_popular));
                            setVisibility.setVisible(recyclerView);
                        }
                    }
                }else {
                    moviesList = savedInstanceState.getParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key));
                    if (moviesList != null) {
                        loadMoviesToRecyclerView();
                        setVisibility.setVisible(recyclerView);
                        setVisibility.setInvisible(progressBar);
                    } else {
                        getMoviesList(getString(R.string.movie_type_popular));
                        setVisibility.setVisible(recyclerView);
                    }
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
                if (checkInternetConnection()) {
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setVisible(progressBar);
                    setRecyclerView();
                    getMoviesList(getString(R.string.movie_type_popular));
                } else {
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setInvisible(progressBar);
                    setVisibility.setVisible(noConnection);
                }
                break;
            case R.id.top_rated_movies:
                if (checkInternetConnection()) {
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setVisible(progressBar);
                    setRecyclerView();
                    getMoviesList(getString(R.string.movie_type_top_rated));
                } else {
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setInvisible(progressBar);
                    setVisibility.setVisible(noConnection);
                }
                break;
            case R.id.favorite_movies:
                if (checkInternetConnection()) {
                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setVisible(progressBar);
                    setRecyclerView();
                    getMoviesList(getString(R.string.movie_type_favorite));
                    currentList = getString(R.string.movie_type_favorite);
                } else {
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

        currentList = listType;

        if(!listType.equals(getString(R.string.movie_type_favorite))) {

            GetMoviesData getMoviesData = new GetMoviesData(this);

            getMoviesData.execute(listType);

        }else{

            HandleFavorites handleFavorites = new HandleFavorites(this);

            ArrayList<String> favoriteMoviesIdList = handleFavorites.getFavoriteMoviesId();

            numberOfFavoriteMovies = favoriteMoviesIdList.size();

            GetFavoritesData getFavoritesData = new GetFavoritesData(this);

            getFavoritesData.execute(favoriteMoviesIdList);

        }
    }

    // AsyncResponseForMoviesList interface to handle getting back moviesList
    @Override
    public void processFinishMovieData(ArrayList<MovieData> moviesList) {
        if(moviesList != null) {
            this.moviesList = moviesList;
            loadMoviesToRecyclerView();
            setVisibility.setInvisible(progressBar);
            setVisibility.setInvisible(noConnection);
            setVisibility.setVisible(recyclerView);
        }
    }

    // AsyncResponseForFavoriteMoviesList interface to handle getting back moviesList
    @Override
    public void processFinishFavoriteMoviesData(ArrayList<MovieData> moviesList) {
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

        MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter(this,moviesList);

        recyclerView.setAdapter(moviesRecyclerAdapter);
    }

    // Save movies list if orientation changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentList",currentList);
        outState.putInt("numberOfFavoriteMovies",numberOfFavoriteMovies);
        outState.putParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key),moviesList);
    }

    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {

        // Check if favorite movies database size changed
        if(currentList != null){
            if ((currentList.equals(getString(R.string.movie_type_favorite)))) {

                HandleFavorites handleFavorites = new HandleFavorites(this);

                ArrayList<String> favoriteMoviesIdList = handleFavorites.getFavoriteMoviesId();

                if (numberOfFavoriteMovies != favoriteMoviesIdList.size()) {
                    numberOfFavoriteMovies = favoriteMoviesIdList.size();

                    setVisibility.setInvisible(recyclerView);
                    setVisibility.setVisible(progressBar);
                    setRecyclerView();
                    getMoviesList(getString(R.string.movie_type_favorite));
                    currentList = getString(R.string.movie_type_favorite);
                }
            }
        }

        super.onResume();
    }
}
