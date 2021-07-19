package com.example.flickr;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    
    private static final String TAG = "RecyclerItemClickListener";

    private final OnRecyclerClickListener onRecyclerClickListener;
    private final GestureDetectorCompat gestureDetectorCompat;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
        this.gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView != null && onRecyclerClickListener != null) {
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    onRecyclerClickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView != null && onRecyclerClickListener != null) {
                    Log.d(TAG, "onLongPress: calling listener.onItemLongClick");
                    onRecyclerClickListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
        Log.d ( TAG, "onInterceptTouchEvent: starts" );
        if (gestureDetectorCompat!=null) {
            boolean result = gestureDetectorCompat.onTouchEvent ( e );
            Log.d ( TAG, "onInterceptTouchEvent: returned" + result );
            return result;
        } else {
            Log.d ( TAG, "onInterceptTouchEvent: returned false" );
            return false;
        }
    }
}
