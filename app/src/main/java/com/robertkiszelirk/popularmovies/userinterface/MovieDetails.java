package com.robertkiszelirk.popularmovies.userinterface;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetReviewsData;
import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetTrailersData;
import com.robertkiszelirk.popularmovies.data.HandleUrls;
import com.robertkiszelirk.popularmovies.data.ModelData.MovieData;
import com.robertkiszelirk.popularmovies.data.ModelData.ReviewData;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForReviewList;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForTrailerList;
import com.robertkiszelirk.popularmovies.uianddata.Adapters.ReviewsRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;
import com.robertkiszelirk.popularmovies.uianddata.Adapters.TrailersRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/** MovieDetails is responsible to show the passed movie required data */

public class MovieDetails extends AppCompatActivity implements AsyncResponseForTrailerList,AsyncResponseForReviewList{

    @BindView(R.id.movie_detail_backdrop) ImageView backdropImage;
    @BindView(R.id.movie_detail_poster) ImageView posterImage;

    @BindView(R.id.movie_detail_title) TextView titleText;
    @BindView(R.id.movie_detail_vote_average) TextView voteText;
    @BindView(R.id.movie_detail_overview) TextView overviewText;
    @BindView(R.id.movie_detail_date) TextView dateText;

    @BindView(R.id.movie_trailer) RecyclerView trailers;
    @BindView(R.id.movie_reviews) RecyclerView reviews;

    Unbinder unbinder;

    private SetVisibility setVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        unbinder = ButterKnife.bind(this);

        setVisibility = new SetVisibility();

        if(checkInternetConnection()) {
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

                    // Get trailers id
                    GetTrailersData getTrailersData = new GetTrailersData(this);

                    getTrailersData.execute(String.valueOf(movieData.getId()));

                    // Get reviews data
                    GetReviewsData getReviewsData = new GetReviewsData(this);

                    getReviewsData.execute(String.valueOf(movieData.getId()));

                } else {
                    // If passed data is null show message
                    Toast.makeText(this, getString(R.string.no_passed_movie_in_details), Toast.LENGTH_SHORT).show();
                }
            } else {
                // If passed data is null show message
                Toast.makeText(this, getString(R.string.no_passed_movie_in_details), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,getString(R.string.movie_detail_no_connection_text),Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Load trailers thumbnail to trailers RecyclerView
    @Override
    public void processFinishTrailerData(ArrayList<String> trailerData) {

        if(trailerData != null){

            setVisibility.setVisible(trailers);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            trailers.setLayoutManager(layoutManager);

            TrailersRecyclerAdapter trailersRecyclerAdapter = new TrailersRecyclerAdapter(this,trailerData);

            trailers.setAdapter(trailersRecyclerAdapter);
        }
    }

    // Load reviews to RecyclerView
    @Override
    public void processFinishReviewData(ArrayList<ReviewData> reviewData) {

        if(reviewData != null){

            setVisibility.setVisible(reviews);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            reviews.setLayoutManager(layoutManager);

            ReviewsRecyclerAdapter reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewData);

            reviews.setAdapter(reviewsRecyclerAdapter);

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


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
