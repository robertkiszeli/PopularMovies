package com.robertkiszelirk.popularmovies.uianddata.SetData;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.robertkiszelirk.popularmovies.data.AsyncTasks.GetTrailersData;
import com.robertkiszelirk.popularmovies.uianddata.Adapters.TrailersRecyclerAdapter;
import com.robertkiszelirk.popularmovies.uianddata.Interfaces.AsyncResponseForTrailerList;
import com.robertkiszelirk.popularmovies.uianddata.SetVisibility;

import java.util.ArrayList;

public class SetTrailers implements AsyncResponseForTrailerList {

    private final Context context;
    private RecyclerView trailersRecyclerView;

    public SetTrailers(Context context){
        this.context = context;
    }

    public void setData(String id,RecyclerView trailersRecyclerView){

        this.trailersRecyclerView = trailersRecyclerView;

        GetTrailersData getTrailersData = new GetTrailersData(this);

        getTrailersData.execute(id);

    }

    @Override
    public void processFinishTrailerData(ArrayList<String> trailerData) {

        if(trailerData != null){

            SetVisibility setVisibility = new SetVisibility();

            setVisibility.setVisible(trailersRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            trailersRecyclerView.setLayoutManager(layoutManager);

            TrailersRecyclerAdapter trailersRecyclerAdapter = new TrailersRecyclerAdapter(context,trailerData);

            trailersRecyclerView.setAdapter(trailersRecyclerAdapter);
        }
    }
}
