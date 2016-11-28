package fr.esiea.xkcdbrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;

public class ComicViewerActivity extends AppCompatActivity {
    final static String TAG = "ComicViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_viewer);
        Log.d(TAG, "Shazbot !");
        Intent intent = getIntent();
        Comic comic = intent.getParcelableExtra(MainActivity.EXTRA_COMIC);
        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.comic);
        imageView.setImageURI(comic.getImageUri());
    }
}
