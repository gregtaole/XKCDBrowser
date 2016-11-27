package fr.esiea.xkcdbrowser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Comic> comics = new ArrayList<>();
    private RecyclerView comicRecycler;
    private ComicAdapter comicAdapter;
    private DividerItemDecoration comicRecyclerDivider;
    private int lastId;

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        comicRecycler = (RecyclerView) findViewById(R.id.main_recycler_view);
        comicAdapter = new ComicAdapter(comics);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
        comicRecyclerDivider = new DividerItemDecoration(comicRecycler.getContext(), recyclerLayoutManager.getOrientation());
        comicRecycler.setLayoutManager(recyclerLayoutManager);
        comicRecycler.setItemAnimator(new DefaultItemAnimator());
        comicRecycler.addItemDecoration(comicRecyclerDivider);
        comicRecycler.setAdapter(comicAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadComics();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browse) {
            // Handle the camera action
        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadComics() {
        if (this.comics.size() == 0) {
            try {
                lastId = new ComicFetcher().execute("http://xkcd.com/info.0.json").get().getId();
            } catch (InterruptedException | ExecutionException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
        loadNNext(10, lastId);
        lastId -= 10;
    }

    private void loadNNext(int n, int lastId) {
        for (int i = lastId - 1; i > lastId - 1 - n ; i--) {
            if (i <= 0)
                break;
            new ComicFetcher().execute("http://xkcd.com/" + String.valueOf(i) +"/info.0.json");
        }
    }

    private class ComicFetcher extends AsyncTask<String, Integer, Comic> {
        @Override
        protected Comic doInBackground(String... urls) {
            ComicBuilder builder = ComicBuilder.getInstance();
            Comic newComic = builder.buildComic(urls[0]);
            Log.d("MainActivity", newComic.getTitle());
            if (newComic != null) {
                return newComic;
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Comic result) {
            comics.add(result);
            comicAdapter.notifyDataSetChanged();
        }
    }
}
