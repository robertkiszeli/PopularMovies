package com.robertkiszelirk.popularmovies.uianddata;

import android.view.View;

/**
 * Created by kiszeli on 2/18/18.
 */

public class SetVisibility {

    public SetVisibility(){}

    public void setInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }

    public void setVisible(View view){
        view.setVisibility(View.VISIBLE);
    }
}
