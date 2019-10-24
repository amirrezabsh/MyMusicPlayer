package com.example.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<Song> myDataset;
    int currentPosition;
    Bitmap image;
    byte [] cover;

    public MyAdapter(ArrayList<Song> myDataset) {
        this.myDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Song data = myDataset.get(position);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(data.getPathId());
        }
        catch (Exception e){

        }
        cover = metadataRetriever.getEmbeddedPicture();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        if (cover != null) {
            image = BitmapFactory.decodeByteArray(cover, 0, cover.length, opt);
            holder.imageView.setImageBitmap(image);

        }
        if (cover == null){
            holder.imageView.setImageResource(R.drawable.music_icon);
        }
        holder.textViewTitle.setText(data.getTitle());
        holder.textViewDetails.setText(data.getArtist());
        holder.textViewAlbum.setText(data.getAlbum());

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewDetails;
        public TextView textViewAlbum;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerImage);
            textViewTitle = itemView.findViewById(R.id.songName);
            textViewDetails = itemView.findViewById(R.id.artistName);
            textViewAlbum = itemView.findViewById(R.id.albumName);
        }
    }
}
