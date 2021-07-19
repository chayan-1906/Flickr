package com.example.flickr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flickr.databinding.ActivityPhotoDetailBinding;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    private TextView textViewPhotoAuthor;
    private ImageView imageViewPhoto;
    private TextView textViewPhotoTitle;
    private TextView textViewPhotoTags;

    Resources resources;
    String photoTitle;
    String photoTags;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_photo_detail );

        activateToolbar ( true );

        Intent intent = getIntent ();
        Photo photo = (Photo) intent.getSerializableExtra ( PHOTO_TRANSFER );
        if (photo!=null) {
            textViewPhotoAuthor = findViewById ( R.id.textViewPhotoAuthor );
            imageViewPhoto = findViewById ( R.id.imageViewPhoto );
            textViewPhotoTitle = findViewById ( R.id.textViewPhotoTitle );
            textViewPhotoTags = findViewById ( R.id.textViewPhotoTags );

            resources = getResources ();
            textViewPhotoAuthor.setText ( photo.getAuthor () );
            Picasso.get ().load ( photo.getLink () ).error ( R.drawable.placeholder ).placeholder ( R.drawable.placeholder ).into ( imageViewPhoto );
            photoTitle = resources.getString ( R.string.photo_title, photo.getTitle () );
            textViewPhotoTitle.setText ( photoTitle );
            photoTags = resources.getString ( R.string.photo_tags, photo.getTags () );
            textViewPhotoTags.setText ( photoTags );
        }
    }

}