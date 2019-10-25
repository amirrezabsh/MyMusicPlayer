package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LoggingMXBean;

public class SongPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private Song song;
    String lengthSecond;
    ImageView imageView;
    byte[] cover;
    Bitmap image;
    SeekBar seekBar;
    public static MediaPlayer mediaPlayer;
    TextView currentTime;
    int seekedProgress;
    Timer timer;
    Bundle bundle;
    ArrayList<Song> songsArray;
    ImageButton play;
    boolean paused = true;
    Thread thread;
    boolean exit = true;
    TextView songName;
    boolean favorited = false;
    boolean repeated = false;
    ImageButton favoriteButton;
    ImageButton repeatButton;
    int index;
    TextView songLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_page);
        currentTime = findViewById(R.id.currentTime);
        favoriteButton = findViewById(R.id.favorite_button);
        repeatButton = findViewById(R.id.repeat_button);
        bundle = getIntent().getExtras();
        songsArray = (ArrayList<Song>) bundle.getSerializable("songs Array");
        index = bundle.getInt("index");
        seekBar = findViewById(R.id.seekBar);
        songLength = findViewById(R.id.length);
        songName = findViewById(R.id.songNameInSongPage);
        play = findViewById(R.id.playButton);
        imageView = findViewById(R.id.songImage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        seekBar.setOnSeekBarChangeListener(this);
        song = songsArray.get(index);
        songName.setText(song.getTitle());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(song.getPathId());
            mediaPlayer.prepare();
        } catch (IOException e) {


        }
        mediaPlayer.start();
        play.setImageResource(R.drawable.pause_button);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.setLooping(false);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(song.getPathId());
        } catch (Exception e) {

        }
        cover = metadataRetriever.getEmbeddedPicture();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        if (cover != null) {
            image = BitmapFactory.decodeByteArray(cover, 0, cover.length, opt);
            imageView.setImageBitmap(image);

        }
        if (cover == null) {
            imageView.setImageResource(R.drawable.music_icon);
        }
        currentTime.setText(0 + ":" + 00);
        String length = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        seekBar.setMax(mediaPlayer.getDuration());
        String lengthMin = String.valueOf((Long.parseLong(length) / 60000));
        if (Long.parseLong(length) % 60000 / 1000 < 10) {
            lengthSecond = 0 + String.valueOf((Long.parseLong(length) % 60000 / 1000));
        } else
            lengthSecond = String.valueOf((Long.parseLong(length) % 60000 / 1000));
        length = lengthMin + ":" + lengthSecond;
        songLength.setText(length);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer.getCurrentPosition() % 60000 / 1000 < 10) {
                            currentTime.setText(mediaPlayer.getCurrentPosition() / 60000 + ":" + 0 + mediaPlayer.getCurrentPosition() % 60000 / 1000);
                        } else
                            currentTime.setText(mediaPlayer.getCurrentPosition() / 60000 + ":" + mediaPlayer.getCurrentPosition() % 60000 / 1000);
                    }
                });
            }
        }, 0, 500);
        metadataRetriever.release();

    }
    public void autoNextMusic (){
        Bundle bundleForNext = new Bundle();
        Intent intent = new Intent(this, SongPage.class);
        if (bundle.getInt("index") + 1 > songsArray.size()) {
            bundleForNext.putSerializable("currentSong", songsArray.get(0));
            bundleForNext.putInt("index", 0);
            bundleForNext.putSerializable("songs Array", songsArray);
            intent.putExtras(bundleForNext);
            startActivity(intent);
            finish();
            return;
        }
        bundleForNext.putSerializable("currentSong", songsArray.get(bundle.getInt("index") + 1));
        bundleForNext.putInt("index", bundle.getInt("index") + 1);
        bundleForNext.putSerializable("songs Array", songsArray);
        intent.putExtras(bundleForNext);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b && mediaPlayer != null) {
            mediaPlayer.seekTo(i);
            seekBar.setProgress(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        currentTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }


    public void nextMusic(View view) {
        Bundle bundleForNext = new Bundle();
        Intent intent = new Intent(this, SongPage.class);
        if (bundle.getInt("index") + 1 > songsArray.size()) {
            bundleForNext.putSerializable("currentSong", songsArray.get(0));
            bundleForNext.putInt("index", 0);
            bundleForNext.putSerializable("songs Array", songsArray);
            intent.putExtras(bundleForNext);
            startActivity(intent);
            finish();
            return;
        }
        bundleForNext.putSerializable("currentSong", songsArray.get(bundle.getInt("index") + 1));
        bundleForNext.putInt("index", bundle.getInt("index") + 1);
        bundleForNext.putSerializable("songs Array", songsArray);
        intent.putExtras(bundleForNext);
        startActivity(intent);
        finish();
    }

    public void play(View view) {
        if (paused) {
            play.setImageResource(R.drawable.play_button);
            mediaPlayer.pause();
            paused = false;
        } else {
            play.setImageResource(R.drawable.pause_button);
            mediaPlayer.start();
            paused = true;
        }
    }

    public void favoriteAction(View view) {
        if (favorited) {
            favoriteButton.setImageResource(R.drawable.favorite_empty);
            favorited = false;
        } else {
            favoriteButton.setImageResource(R.drawable.favorite_full);
            favorited = true;
        }
    }

    public void repeatAction(View view) {
        if (repeated) {
            repeatButton.setImageResource(R.drawable.repeated_icon);
            repeated = false;
        } else {
            repeatButton.setImageResource(R.drawable.repeat_icon);
            repeated = true;
        }
    }

    public void previousMusic(View view) {
        Bundle bundleForNext = new Bundle();
        Intent intent = new Intent(this, SongPage.class);
        if (bundle.getInt("index") - 1 < 0) {
            return;
        }
        bundleForNext.putSerializable("currentSong", songsArray.get(bundle.getInt("index") - 1));
        bundleForNext.putInt("index", bundle.getInt("index") - 1);
        bundleForNext.putSerializable("songs Array", songsArray);
        intent.putExtras(bundleForNext);
        startActivity(intent);
        finish();
    }
}
