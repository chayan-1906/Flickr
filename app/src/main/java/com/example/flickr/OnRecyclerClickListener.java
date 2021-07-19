package com.example.flickr;

import android.view.View;

public interface OnRecyclerClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
