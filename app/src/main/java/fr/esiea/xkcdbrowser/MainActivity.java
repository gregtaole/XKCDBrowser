package fr.esiea.xkcdbrowser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ComicFetcherInterface implements NavigationView.OnNavigationItemSelectedListener, NetworkConnectivityDialogFragment.NetworkConnectivityListener {
    private ArrayList<Comic> comics = new ArrayList<>();
    private RecyclerView comicRecycler;
    private ComicAdapter comicAdapter;
    private DividerItemDecoration comicRecyclerDivider;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private Intent newComicIntent;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private int lastId;

    public static final String EXTRA_COMIC = "fr.esiea.xkcdbrowser.COMIC";
    public static final String EXTRA_URL = "fr.esiea.xkcdbrowser.XKCD_LAST_COMIC_URL";
    public static final String EXTRA_LAST_ID  = "fr.esie.xkcdbrowser.LAST_ID";

    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        comicRecycler = (RecyclerView) findViewById(R.id.main_recycler_view);
        comicAdapter = new ComicAdapter(comics);
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
                loadMoreComics();
            }
        });
        comicAdapter.setClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Intent intent = new Intent(view.getContext(), ComicViewerActivity.class);
                intent.putExtra(EXTRA_COMIC, comics.get(position));
                Log.d(TAG, "Click !");
                startActivity(intent);
            }
        });

        if (networkInfo != null && networkInfo.isConnected()) {
            loadComics();
        } else {
            DialogFragment dialogFragment = new NetworkConnectivityDialogFragment();
            dialogFragment.show(this.getSupportFragmentManager(), "NetworkConnectivityDialog");
        }

        this.startNewComicService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            DialogFragment dialogFragment = new NetworkConnectivityDialogFragment();
            dialogFragment.show(this.getSupportFragmentManager(), "NetworkConnectivityDialog");
        }
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_fav) {
            startActivity(new Intent(getApplicationContext(), FavActivity.class));
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    private void loadMoreComics() {
        comicAdapter.setShowLoader(true);
        comicAdapter.notifyDataSetChanged();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadComics();
                comicAdapter.setShowLoader(false);
                comicAdapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    private void loadComics() {
        Log.d(TAG, "ID: " + lastId);
        if (this.comics.size() == 0) {
            try {
                lastId = new ComicFetcher().execute(Constants.XKCD_LAST_COMIC_URL, this).get().getId();
            } catch (InterruptedException | ExecutionException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
        Log.d(TAG, "ID: " + lastId);
        loadNNext(10, lastId);
        lastId -= 10;
    }

    private void loadNNext(int n, int lastId) {
        for (int i = lastId - 1; i > lastId - 1 - n ; i--) {
            if (i <= 0)
                break;
            new ComicFetcher().execute("http://xkcd.com/" + String.valueOf(i) +"/info.0.json", this);
        }
    }

    private void startNewComicService() {
        //Service and alarm
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        newComicIntent = new Intent(this, NewComicService.class);
        newComicIntent.putExtra(EXTRA_URL, Constants.XKCD_LAST_COMIC_URL);
        newComicIntent.putExtra(EXTRA_LAST_ID, lastId);
        alarmIntent = PendingIntent.getService(this, 0, newComicIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                60 * 1000, //AlarmManager.INTERVAL_HOUR * 2,
                alarmIntent);

        IntentFilter intentFilter = new IntentFilter(NewComicService.ACTION_NEW);
        NewComicBR newComicBR = new NewComicBR();
        LocalBroadcastManager.getInstance(this).registerReceiver(newComicBR, intentFilter);
    }

    public ArrayList<Comic> getComics() {
        return comics;
    }

    public ComicAdapter getComicAdapter() {
        return comicAdapter;
    }
}
