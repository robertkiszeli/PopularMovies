package com.robertkiszelirk.popularmovies.userinterface;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.HandleUrls;
import com.robertkiszelirk.popularmovies.data.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

/** MovieDetails is responsible to show the passed movie required data */

public class MovieDetails extends AppCompatActivity {

    @BindView(R.id.movie_detail_backdrop) ImageView backdropImage;
    @BindView(R.id.movie_detail_poster) ImageView posterImage;

    @BindView(R.id.movie_detail_title) TextView titleText;
    @BindView(R.id.movie_detail_vote_average) TextView voteText;
    @BindView(R.id.movie_detail_overview) TextView overviewText;
    @BindView(R.id.movie_detail_date) TextView dateText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        ButterKnife.bind(this);


            Bundle data = getIntent().getExtras();
            // If passed data is not null load it
            if (data != null) {
                // Get passed data
                MovieData movieData = data.getParcelable(getString(R.string.passing_movie_parcelable_key));
                if (movieData != null) {
                    Glide.with(this)
                            .load(HandleUrls.createImageUrl(movieData.getBackdropPath()))
                            .into(backdropImage);
                    Glide.with(this)
                            .load(HandleUrls.createImageUrl(movieData.getPosterPath()))
                            .into(posterImage);
                    titleText.setText(movieData.getTitle());
                    voteText.setText(getString(R.string.vote_average_string, Double.toString(movieData.getVoteAverage())));
                    overviewText.setText(movieData.getOverview());
                    dateText.setText(getString(R.string.release_date_string, movieData.getReleaseDate()));
                } else {
                    // If passed data is null show message
                    Toast.makeText(this, getString(R.string.no_passed_movie_in_details), Toast.LENGTH_SHORT).show();
                }
            } else {
                // If passed data is null show message
                Toast.makeText(this, getString(R.string.no_passed_movie_in_details), Toast.LENGTH_SHORT).show();
            }
    }

}
