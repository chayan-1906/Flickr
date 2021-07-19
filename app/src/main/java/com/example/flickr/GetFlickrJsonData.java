package com.example.flickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements OnDownloadComplete {

    private static final String TAG = "GetFlickrJsonData";

    private final OnDataAvailable onDataAvailableCallback;
    private boolean runningOnSameThread = false;

    private List<Photo> photoList = null;
    private final String baseURl;
    private final String language;
    private final boolean matchAll;

    public GetFlickrJsonData(OnDataAvailable onDataAvailableCallback, String baseURl, String language, boolean matchAll) {
        Log.d ( TAG, "GetFlickrJsonData called" );
        this.onDataAvailableCallback = onDataAvailableCallback;
        this.baseURl = baseURl;
        this.language = language;
        this.matchAll = matchAll;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {
        if (downloadStatus==DownloadStatus.OK) {
            photoList = new ArrayList<> (  );
            try {
                JSONObject jsonObject = new JSONObject ( data );
                JSONArray items = jsonObject.getJSONArray ( "items" );
                for (int i = 0; i < items.length (); i++) {
                    JSONObject jsonPhoto = items.getJSONObject ( i );
                    String title = jsonPhoto.getString ( "title" );
                    String author = jsonPhoto.getString ( "author" );
                    String authorId = jsonPhoto.getString ( "author_id" );
                    String tags = jsonPhoto.getString ( "tags" );

                    JSONObject jsonMedia = jsonPhoto.getJSONObject ( "media" );
                    String photoUrl = jsonMedia.getString ( "m" );

                    String link = photoUrl.replaceFirst ( "_m.", "_b." );

                    Photo photo = new Photo ( title, author, authorId, link, tags, photoUrl );
                    photoList.add ( photo );
                    
                    Log.d ( TAG, "onDownloadComplete: " + photo.toString () );
                }
            } catch (JSONException e) {
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                e.printStackTrace ( );
                Log.e ( TAG, "onDownloadComplete: Error processing Json data " + e.getMessage () );
            }
        }
        if (runningOnSameThread && onDataAvailableCallback!=null) {
            // now inform the caller that processing is done - possibly returning null if there was error
            onDataAvailableCallback.onDataAvailable ( photoList, downloadStatus );
        }
        Log.d ( TAG, "onDownloadComplete ends" );
    }

    void executeOnSameThread(String searchCriteria) {
        Log.d ( TAG, "executeOnSameThread starts" );
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, language, matchAll);

        GetRawData getRawData = new GetRawData ( this );
        getRawData.execute ( destinationUri );
        Log.d ( TAG, "executeOnSameThread ends" );
    }

    private String createUri(String searchCriteria, String language, boolean matchAll) {
        Log.d ( TAG, "createUri starts" );

        return Uri.parse ( baseURl ).buildUpon ()
                .appendQueryParameter ( "tags", searchCriteria )
                .appendQueryParameter ( "tagmode", matchAll ? "ALL" : "ANY" )
                .appendQueryParameter ( "lang", language )
                .appendQueryParameter ( "format", "json" )
                .appendQueryParameter ( "nojsoncallback", "1" )
                .build ().toString ();
    }

    @Override
    protected void onPostExecute(List<Photo> photoList) {
        Log.d ( TAG, "onPostExecute starts" );
        if (onDataAvailableCallback!=null) {
            onDataAvailableCallback.onDataAvailable ( photoList, DownloadStatus.OK );
        }
        Log.d ( TAG, "onPostExecute ends" );
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d ( TAG, "doInBackground starts" );
        String destinationUri = createUri ( params[ 0 ], language, matchAll );
        GetRawData getRawData = new GetRawData ( this );
        getRawData.runInSameThread ( destinationUri );
        Log.d ( TAG, "doInBackground ends" );
        return photoList;
    }
}
