package com.example.flickr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {

    private static final String TAG = "FlickrRecyclerViewAdapter";
    private List<Photo> photoList;
    private final Context context;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // Called by the layout manager when it needs a new item
        Log.d ( TAG, "onCreateViewHolder: new view requested" );
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.browse, parent, false );
        return new FlickrImageViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull FlickrImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row
        if ((photoList==null) || photoList.size ()==0) {
            holder.imageViewThumbnail.setImageResource ( R.drawable.placeholder );
            holder.textViewTitle.setText ( "No photos match your search.\n\nUse the search icon to search for photos" );
        } else {
            Photo photoItem = photoList.get ( position );
            Log.d ( TAG, "onBindViewHolder: " + photoItem.getTitle ( ) + "---> " + position );
            Picasso.get ( ).load ( photoItem.getImage ( ) ).error ( R.drawable.placeholder ).placeholder ( R.drawable.placeholder ).into ( holder.imageViewThumbnail );
            holder.textViewTitle.setText ( photoItem.getTitle ( ) );
        }
    }

    @Override
    public int getItemCount() {
        Log.d ( TAG, "getItemCount: called" );
        return ((photoList != null && photoList.size () != 0) ? photoList.size () : 1);
    }

    void loadNewData(List<Photo> newPhotos) {
        photoList = newPhotos;
        notifyDataSetChanged ();
    }

    public Photo getPhoto(int position) {
        return ((photoList != null && photoList.size () != 0) ? photoList.get ( position ) : null);
    }

    public static class FlickrImageViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "FlickrImageViewHolder";
        private final ImageView imageViewThumbnail;
        private final TextView textViewTitle;

        public FlickrImageViewHolder(@NonNull @NotNull View itemView) {
            super ( itemView );
            Log.d ( TAG, "FlickrImageViewHolder starts" );
            imageViewThumbnail = itemView.findViewById ( R.id.imageViewThumbnail );
            textViewTitle = itemView.findViewById ( R.id.textViewTitle );
        }

    }
}
