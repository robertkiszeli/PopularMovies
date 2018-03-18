package com.robertkiszelirk.popularmovies.uianddata.interfaces;

import com.robertkiszelirk.popularmovies.data.modeldata.ReviewData;

import java.util.ArrayList;

/** AsyncResponseForReviewList is responsible to pass the movies list from AsyncTask to MovieDetails */

public interface AsyncResponseForReviewList {
    void processFinishReviewData(ArrayList<ReviewData> reviewData);
}
