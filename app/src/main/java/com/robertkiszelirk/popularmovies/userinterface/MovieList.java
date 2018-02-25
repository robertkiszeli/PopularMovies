package com.robertkiszelirk.popularmovies.userinterface;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
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

    SetVisibility setVisibility;

    private ArrayList<MovieData> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        ButterKnife.bind(this);

        setVisibility = new SetVisibility();

        // Check orientation and set column numbers
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRecyclerView(3);
        }else{
            setRecyclerView(2);
        }
        // Check if orientation has changed
        if(savedInstanceState != null){

            moviesList = savedInstanceState.getParcelableArrayList(getString(R.string.on_saved_instance_state_movies_list_key));

            loadMoviesToRecyclerView();

            setVisibility.setVisible(recyclerView);
            setVisibility.setInvisible(progressBar);

        }else{

            getMoviesList(getString(R.string.movie_type_popular));
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
                getMoviesList(getString(R.string.movie_type_popular));
                break;
            case R.id.top_rated_movies:
                getMoviesList(getString(R.string.movie_type_top_rated));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Set RecyclerView grid, columns
    private void setRecyclerView(int columns) {

        setVisibility.setInvisible(recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,columns);

        recyclerView.setLayoutManager(gridLayoutManager);

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
}
