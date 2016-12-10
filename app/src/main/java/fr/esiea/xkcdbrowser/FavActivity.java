package fr.esiea.xkcdbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class FavActivity extends ComicFetcherInterface implements NavigationView.OnNavigationItemSelectedListener, NetworkConnectivityDialogFragment.NetworkConnectivityListener {
    private ArrayList<Comic> favComics = new ArrayList<>();
    private ArrayList<Integer> favIds = new ArrayList<>();
    private RecyclerView comicRecycler;
    private ComicAdapter comicAdapter;
    private DividerItemDecoration comicRecyclerDivider;
    private FavManager favManager;

    final static String TAG = "FavActivity";
    public static final String EXTRA_COMIC = "fr.esiea.xkcdbrowser.COMIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.favorites_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.favorites_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        comicRecycler = (RecyclerView) findViewById(R.id.favorites_recycler_view);
        comicAdapter = new ComicAdapter(favComics);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
        comicRecyclerDivider = new DividerItemDecoration(comicRecycler.getContext(), recyclerLayoutManager.getOrientation());
        comicRecycler.setLayoutManager(recyclerLayoutManager);
        comicRecycler.setItemAnimator(new DefaultItemAnimator());
        comicRecycler.addItemDecoration(comicRecyclerDivider);
        comicRecycler.setAdapter(comicAdapter);
        comicRecycler.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        comicAdapter.setClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Intent intent = new Intent(view.getContext(), ComicViewerActivity.class);
                intent.putExtra(EXTRA_COMIC, favComics.get(position));
                Log.d(TAG, "Click !");
                startActivity(intent);
            }
        });

        favManager = FavManager.getInstance(this);
        loadComics();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.favorites_drawer_layout);
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_fav) {
            startActivity(new Intent(getApplicationContext(), FavActivity.class));
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.favorites_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialogFragment) {
        this.finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
    }

    private void loadComics() {
        favIds = favManager.getFavorites();
        for (Integer id : favIds) {
            new ComicFetcher().execute("http://xkcd.com/" + String.valueOf(id) +"/info.0.json", this);
        }
    }

    @Override
    public ArrayList<Comic> getComics() {
        return favComics;
    }

    @Override
    public ComicAdapter getComicAdapter() {
        return comicAdapter;
    }
}
