package fr.esiea.xkcdbrowser;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ComicViewerActivity extends AppCompatActivity {
    final static String TAG = "ComicViewerActivity";
    final static String favFilename = "favorites.txt";

    Comic comic;
    ArrayList<Integer> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comic_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.comic_viewer_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        comic = intent.getParcelableExtra(MainActivity.EXTRA_COMIC);

        TextView titleView = (TextView) findViewById(R.id.comic_viewer_title);
        titleView.setText(comic.getTitle());

        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.comic_viewer_comic);
        imageView.setImageURI(comic.getImageUri());

        TextView altView = (TextView) findViewById(R.id.comic_viewer_alt);
        altView.setText(comic.getAlt());

        favorites = new ArrayList<Integer>();
        getFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_comic_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_favorite) {
            addToFavorites();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites() {
        if (favorites.contains(comic.getId())) {
            Snackbar.make(findViewById(R.id.comic_viewer_coordinator), R.string.snackbar_already_favorite, Snackbar.LENGTH_SHORT).show();
        }
        else {
            FileOutputStream outputStream;
            String separator = System.getProperty("line.separator");

            try {
                outputStream = openFileOutput(favFilename, Context.MODE_PRIVATE);
                outputStream.write(String.valueOf(comic.getId()).getBytes());
                outputStream.write(separator.getBytes());
                outputStream.close();
            }
            catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
            favorites.add(comic.getId());
            Snackbar.make(findViewById(R.id.comic_viewer_coordinator), R.string.snackbar_favorites, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getFavorites() {
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = openFileInput(favFilename);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                favorites.add(Integer.valueOf(line));
            }
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

    }
}
