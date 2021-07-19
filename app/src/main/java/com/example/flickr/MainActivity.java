package com.example.flickr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnDataAvailable, OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    // Android UI...
    private RecyclerView recyclerView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d ( TAG, "onCreate: starts" );
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        activateToolbar ( false );

        recyclerView = findViewById ( R.id.recyclerView );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );

        recyclerView.addOnItemTouchListener ( new RecyclerItemClickListener ( this, recyclerView, this ) );

        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter ( this, new ArrayList<Photo> (  ) );
        recyclerView.setAdapter ( flickrRecyclerViewAdapter );

        GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData ( this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true );
        getFlickrJsonData.execute ( "android,nougat" );

        Log.d ( TAG, "onCreate: ends" );
    }

    @Override
    protected void onResume() {
        Log.d ( TAG, "onResume starts" );
        super.onResume ( );

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences ( getApplicationContext () );
        String queryResult = sharedPreferences.getString ( FLICKR_QUERY, "" );
        if (queryResult.length () > 0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData ( this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true );
            getFlickrJsonData.execute ( queryResult );
        }
        Log.d ( TAG, "onResume ends" );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ( ).inflate ( R.menu.menu_main, menu );
        Log.d ( TAG, "onCreateOptionsMenu: returned" + true );
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ( );

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
        }

        Log.d ( TAG, "onOptionsItemSelected: returned" + true );
        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onDataAvailable(List<Photo> photoList, DownloadStatus downloadStatus) {
        Log.d ( TAG, "onDataAvailable: starts" );
        if (downloadStatus==DownloadStatus.OK) {
            Log.d ( TAG, "onDataAvailable: data is: " + photoList );
            flickrRecyclerViewAdapter.loadNewData ( photoList );
        } else {
            // download or processing failed
            Log.e ( TAG, "onDataAvailable: failed with status: " + downloadStatus );
        }
        Log.d ( TAG, "onDataAvailable: ends" );
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d ( TAG, "onItemClick: starts" );
//        Toast.makeText ( MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT ).show ( );
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d ( TAG, "onItemLongClick: starts" );
//        Toast.makeText ( MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT ).show ( );
        Intent intentPhotoDetail = new Intent ( MainActivity.this, PhotoDetailActivity.class );
        intentPhotoDetail.putExtra ( PHOTO_TRANSFER, flickrRecyclerViewAdapter.getPhoto ( position ) );
        startActivity ( intentPhotoDetail );
    }
}
