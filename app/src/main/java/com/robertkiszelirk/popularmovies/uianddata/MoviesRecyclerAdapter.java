package com.robertkiszelirk.popularmovies.uianddata;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.HandleUrls;
import com.robertkiszelirk.popularmovies.data.MovieData;
import com.robertkiszelirk.popularmovies.userinterface.MovieDetails;

import java.util.ArrayList;

/** MoviesRecyclerAdapter is responsible to fill the MovieList RecyclerView from the
 *  passed ArrayList<MovieData> list.
 *  And to handle the click on the images to start the MovieDetails activity.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    //Passed movies list
    private ArrayList<MovieData> moviesList;

    public MoviesRecyclerAdapter(ArrayList<MovieData> moviesList){
        this.moviesList = moviesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_in_grid,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(HandleUrls.createImageUrl(moviesList.get(position).getPosterPath()))
                .into(holder.posterImage);

    }

    @Override
    public int getItemCount() {

        return moviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView posterImage;

        ViewHolder(final View itemView) {
            super(itemView);

            posterImage = itemView.findViewById(R.id.movies_list_movie_poster);
            // Set click listener to the images
            posterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MovieDetails.class);
                    MovieData currentMovieDetails = moviesList.get(getAdapterPosition());
                    // Add actual movie data to intent
                    intent.putExtra(view.getContext().getString(R.string.passing_movie_parcelable_key),currentMovieDetails);
                    // Set animation between the two activity
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(),posterImage,"animated");
                    // Start MovieDetails activity
                    view.getContext().startActivity(intent,options.toBundle());
                }
            });
        }
    }
}
