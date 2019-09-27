package com.example.musicplayer;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Song {
    private String artist;
    private String title;
    private Bitmap image;

    public Song(String artist, String title,Bitmap image) {
        this.artist = artist;
        this.title = title;
        this.image = image;
    }



    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
