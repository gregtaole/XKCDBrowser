package fr.esiea.xkcdbrowser;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ComicViewerActivity extends AppCompatActivity {
    final static String TAG = "ComicViewerActivity";

    Comic comic;
    FavManager favManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_viewer);

        favManager = FavManager.getInstance(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_comic_viewer, menu);
        if (favManager.isAlreadyFavorites(comic)) {
            menu.getItem(0).setIcon(getDrawable(R.drawable.ic_menu_unfav));
        } else {
            menu.getItem(0).setIcon(getDrawable(R.drawable.ic_menu_fav));
        }
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
            if (favManager.isAlreadyFavorites(comic)) {
                favManager.removeFavorite(comic);
                item.setIcon(getDrawable(R.drawable.ic_menu_fav));
                Snackbar.make(findViewById(R.id.comic_viewer_coordinator), R.string.snackbar_removed_favorites, Snackbar.LENGTH_SHORT).show();
            }
            else {
                try {
                    favManager.addToFavorites(comic);
                    item.setIcon(getDrawable(R.drawable.ic_menu_unfav));
                    Snackbar.make(findViewById(R.id.comic_viewer_coordinator), R.string.snackbar_favorites, Snackbar.LENGTH_SHORT).show();
                }
                catch (AlreadyFavoriteException e) {
                    Snackbar.make(findViewById(R.id.comic_viewer_coordinator), R.string.snackbar_already_favorite, Snackbar.LENGTH_SHORT).show();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
