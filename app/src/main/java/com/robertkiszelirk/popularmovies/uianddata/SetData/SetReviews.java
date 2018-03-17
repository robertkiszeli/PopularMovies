package com.robertkiszelirk.popularmovies.uianddata.SetData;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetReviewsData;
import com.robertkiszelirk.popularmovies.data.ModelData.ReviewData;
import com.robertkiszelirk.popularmovies.uianddata.Adapters.ReviewsRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForReviewList;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;

import java.util.ArrayList;

public class SetReviews implements AsyncResponseForReviewList {

    private final Context context;
    private RecyclerView reviewsRecyclerView;

    public SetReviews(Context context){
        this.context = context;
    }

    public void setData(String id,RecyclerView reviewsRecyclerView) {

        this.reviewsRecyclerView = reviewsRecyclerView;

        GetReviewsData getReviewsData = new GetReviewsData(this);

        getReviewsData.execute(id);
    }

    // Load reviews to RecyclerView
    @Override
    public void processFinishReviewData(ArrayList<ReviewData> reviewData) {

        if(reviewData != null){

            SetVisibility setVisibility = new SetVisibility();

            setVisibility.setVisible(reviewsRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

            reviewsRecyclerView.setLayoutManager(layoutManager);

            ReviewsRecyclerAdapter reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewData);

            reviewsRecyclerView.setAdapter(reviewsRecyclerAdapter);

        }

    }
}
