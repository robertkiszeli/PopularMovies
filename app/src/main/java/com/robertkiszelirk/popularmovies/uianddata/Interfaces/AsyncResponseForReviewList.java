package com.robertkiszelirk.popularmovies.uianddata.Interfaces;

import com.robertkiszelirk.popularmovies.data.ModelData.ReviewData;

import java.util.ArrayList;

/** AsyncResponseForReviewList is responsible to pass the movies list from AsyncTask to MovieDetails */

public interface AsyncResponseForReviewList {
    void processFinishReviewData(ArrayList<ReviewData> reviewData);
}
