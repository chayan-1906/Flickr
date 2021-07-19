package com.example.flickr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {
    IDLE,
    PROCESSING,
    NOT_INITIALIZED,
    FAILED_OR_EMPTY,
    OK
}

class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private final OnDownloadComplete onDownloadComplete;

    private DownloadStatus downloadStatus;

    public GetRawData(OnDownloadComplete onDownloadComplete) {
        this.downloadStatus = DownloadStatus.IDLE;
        this.onDownloadComplete = onDownloadComplete;
    }

    void runInSameThread(String s) {
        Log.d ( TAG, "runInSameThread starts" );
        if (onDownloadComplete!=null) {
            onDownloadComplete.onDownloadComplete ( doInBackground ( s ), downloadStatus );
        }
        Log.d ( TAG, "runInSameThread ends" );
    }

    @Override
    protected void onPostExecute(String s) {
        if (onDownloadComplete!=null) {
            onDownloadComplete.onDownloadComplete ( s, downloadStatus );
        }
        Log.d ( TAG, "onPostExecute: ends" );
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        if (strings==null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }
        try {
            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL ( strings[0] );

            httpURLConnection = (HttpURLConnection) url.openConnection ();
            httpURLConnection.setRequestMethod ( "GET" );
            httpURLConnection.connect ();
            int response = httpURLConnection.getResponseCode ();
            Log.d ( TAG, "doInBackground: The response code was: " + response );

            StringBuilder result = new StringBuilder (  );
            bufferedReader = new BufferedReader ( new InputStreamReader ( httpURLConnection.getInputStream () ) );

            for (String line = bufferedReader.readLine ( ); line != null; line = bufferedReader.readLine ( ))
                result.append ( line ).append ( "\n" );

            downloadStatus = DownloadStatus.OK;
            return result.toString ();

        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace ();
            Log.e ( TAG, "doInBackground: Invalid URL: " + malformedURLException.getMessage ()  );
        } catch (IOException ioException) {
            ioException.printStackTrace ();
            Log.e ( TAG, "doInBackground: IOException: " + ioException.getMessage () );
        } catch (SecurityException securityException) {
            securityException.printStackTrace ();
            Log.e ( TAG, "doInBackground: Security Exception. Needs permission?" + securityException.getMessage () );
        } finally {
            if (httpURLConnection!=null) {
                httpURLConnection.disconnect ();
            }
            if (bufferedReader!=null) {
                try {
                    bufferedReader.close ();
                } catch (IOException ioException) {
                    ioException.printStackTrace ();
                    Log.e ( TAG, "doInBackground: Error closing stream" + ioException.getMessage () );
                }
            }
        }
        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
