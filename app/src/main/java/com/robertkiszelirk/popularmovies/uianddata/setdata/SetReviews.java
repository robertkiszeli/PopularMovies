package com.robertkiszelirk.popularmovies.uianddata.setdata;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.robertkiszelirk.popularmovies.data.asynctasks.GetReviewsData;
import com.robertkiszelirk.popularmovies.data.modeldata.ReviewData;
import com.robertkiszelirk.popularmovies.uianddata.adapters.ReviewsRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.interfaces.AsyncResponseForReviewList;

import java.util.ArrayList;

public class SetReviews implements AsyncResponseForReviewList {

    private final Context context;
    private RecyclerView reviewsRecyclerView;
    private Parcelable scrollPosition;

    public SetReviews(Context context){
        this.context = context;
    }

    public void setData(String id,RecyclerView reviewsRecyclerView,Parcelable scrollPosition) {

        this.reviewsRecyclerView = reviewsRecyclerView;

        this.scrollPosition = scrollPosition;

        GetReviewsData getReviewsData = new GetReviewsData(this);

        getReviewsData.execute(id);
    }

    // Load reviews to RecyclerView
    @Override
    public void processFinishReviewData(ArrayList<ReviewData> reviewData) {

        if(reviewData != null){

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

            reviewsRecyclerView.setLayoutManager(layoutManager);

            ReviewsRecyclerAdapter reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewData);

            reviewsRecyclerView.setAdapter(reviewsRecyclerAdapter);

            reviewsRecyclerView.getLayoutManager().onRestoreInstanceState(scrollPosition);
        }

    }
}
