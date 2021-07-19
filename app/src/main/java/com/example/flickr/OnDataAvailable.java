package com.example.flickr;

import java.util.List;

public interface OnDataAvailable {
    void onDataAvailable(List<Photo> photoList, DownloadStatus downloadStatus);
}
