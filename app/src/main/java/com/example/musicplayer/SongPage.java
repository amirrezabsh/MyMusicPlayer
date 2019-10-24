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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_page);
        currentTime = findViewById(R.id.currentTime);
        bundle = getIntent().getExtras();
        song = (Song) bundle.getSerializable("currentSong");
        songName = findViewById(R.id.songNameInSongPage);
        songName.setText(song.getTitle());
        songsArray = (ArrayList<Song>) bundle.getSerializable("songs Array");
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
        play = findViewById(R.id.playButton);
        play.setImageResource(R.drawable.pause_button);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.setLooping(false);

        imageView = findViewById(R.id.songImage);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        seekBar = findViewById(R.id.seekBar);
        TextView songLength = findViewById(R.id.length);
        currentTime.setText(0 + ":" + 0);
        String length = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        seekBar.setMax(mediaPlayer.getDuration());
        String lengthMin = String.valueOf((Long.parseLong(length) / 60000));
        String lengthSecond = String.valueOf((Long.parseLong(length) % 60000 / 1000));
        length = lengthMin + ":" + lengthSecond;
        songLength.setText(length);
        seekBar.setOnSeekBarChangeListener(this);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime.setText(mediaPlayer.getCurrentPosition()/60000+":"+mediaPlayer.getCurrentPosition()%60000/1000);
                    }
                });
            }
        }, 0,500);
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                int total = mediaPlayer.getDuration() / 1000;
//
//
//                while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
//                    try {
//                        Thread.sleep(1000);
//                        currentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                    } catch (InterruptedException e) {
//                        return;
//                    } catch (Exception e) {
//                        return;
//                    }
//                    final int finalCurrentPosition = currentPosition;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            currentTime.setText(finalCurrentPosition / 60 + ":" + finalCurrentPosition % 60);
//                        }
//                    });
//
//                    seekBar.setProgress(currentPosition);
//                    System.out.println("fuck");
//
//                }
//            }
//        });
//        thread.start();
        metadataRetriever.release();
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
        bundleForNext.putSerializable("currentSong", songsArray.get(bundle.getInt("index") + 1));
        bundleForNext.putInt("index", bundle.getInt("index") + 1);
        bundleForNext.putSerializable("songs Array", songsArray);
        intent.putExtras(bundleForNext);
        startActivity(intent);
        finish();
    }

    public void play(View view)  {
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
}
