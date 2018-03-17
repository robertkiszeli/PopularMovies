package com.robertkiszelirk.popularmovies.uianddata.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.robertkiszelirk.popularmovies.R;
import com.robertkiszelirk.popularmovies.data.HandleUrls;
import java.util.ArrayList;

/** TrailersRecyclerAdapter is responsible to fill the trailers RecyclerView from the
 *  passed ArrayList<String> id list.
 *  And to handle the click on the images to start the trailer on YouTube.
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.ViewHolder> {

    private final Context context;

    //Passed trailers list
    private final ArrayList<String> trailersList;

    public TrailersRecyclerAdapter(Context context,ArrayList<String> trailersList){
        this.context = context;
        this.trailersList = trailersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.trailer_in_recycler_view,parent,false);

            return new ViewHolder(view);
            }

    @Override
    public void onBindViewHolder(TrailersRecyclerAdapter.ViewHolder holder, int position) {

            Glide.with(holder.itemView.getContext())
            .load(HandleUrls.createYoutubeThumbnailUrl(trailersList.get(position)))
            .into(holder.trailerImage);
            }

    @Override
    public int getItemCount() {

            return trailersList.size();
            }

    class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView trailerImage;

        ViewHolder(final View itemView) {
            super(itemView);

            trailerImage = itemView.findViewById(R.id.trailer_image);
            // Set click listener to the images
            trailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check internet connection
                    if(checkInternetConnection()) {
                        // Start intent to show trailer
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(HandleUrls.createYoutubeTrailerUrl(trailersList.get(getAdapterPosition()))));
                        view.getContext().startActivity(intent);
                    }else{
                        Toast.makeText(context,"No internet connection.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
