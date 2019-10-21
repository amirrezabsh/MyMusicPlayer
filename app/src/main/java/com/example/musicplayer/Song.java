package com.example.musicplayer;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

public class Song  implements Serializable{
    public String artist;
    private String title;
    private String pathId;
    private String album;

    public Song(String artist, String title,String pathId,String album) {
        this.artist = artist;
        this.title = title;
        this.pathId = pathId;
        this.album = album;
    }


    public String getAlbum() {
        return album;
    }



    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String  getPathId() {
        return pathId;
    }



}
