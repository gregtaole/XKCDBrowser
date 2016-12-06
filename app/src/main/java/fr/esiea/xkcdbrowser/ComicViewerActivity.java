package fr.esiea.xkcdbrowser;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ComicViewerActivity extends AppCompatActivity {
    final static String TAG = "ComicViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comic_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.comic_viewer_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Comic comic = intent.getParcelableExtra(MainActivity.EXTRA_COMIC);

        TextView titleView = (TextView) findViewById(R.id.comic_viewer_title);
        titleView.setText(comic.getTitle());

        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.comic_viewer_comic);
        imageView.setImageURI(comic.getImageUri());

        TextView altView = (TextView) findViewById(R.id.comic_viewer_alt);
        altView.setText(comic.getAlt());
    }
}
