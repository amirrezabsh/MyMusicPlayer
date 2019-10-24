package com.example.musicplayer;

import android.app.ActionBar;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity implements  TextWatcher {
    EditText editText;
    TextView textView;
    RecyclerView recyclerView;
    ArrayList<Song> searchSongs = new ArrayList<>();
    ArrayList<Song> songArrayList;
    Bundle b;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        songArrayList = (ArrayList<Song>) getIntent().getSerializableExtra("songs");
        Toolbar toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.search_result);
        editText.addTextChangedListener(this);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        recyclerView = null;
        recyclerView = findViewById(R.id.search_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        searchSongs.clear();
        for (int j = 0; j <songArrayList.size(); j++) {
            System.out.println(j);
            if (Pattern.compile(Pattern.quote(charSequence.toString()), Pattern.CASE_INSENSITIVE).matcher(songArrayList.get(j).getTitle()).find() && charSequence.length()!=0 || Pattern.compile(Pattern.quote(charSequence.toString()), Pattern.CASE_INSENSITIVE).matcher(songArrayList.get(j).getArtist()).find()){
                if (searchSongs.contains(songArrayList.get(j)))
                    continue;
                searchSongs.add(songArrayList.get(j));
            }
            else if (charSequence.length()==0){
                textView.setText("Enter keywords.");
            }

        }
        if (searchSongs.size()==0) {
            textView.setText("No musics found!");
        }
        else {
            textView.setText("");
        }
        adapter = new MyAdapter(searchSongs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
