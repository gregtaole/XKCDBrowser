package fr.esiea.xkcdbrowser;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class NewComicService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_NEW = "fr.esiea.xkcdbrowser.action.NEW";

    final static String TAG = "NewComicService";

    public NewComicService() {
        super("NewComicService");
        Log.d(TAG, "Service started!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        handleActionNew(intent.getStringExtra(MainActivity.EXTRA_URL), intent.getIntExtra(MainActivity.EXTRA_LAST_ID, 0));
    }

    private void handleActionNew(String url, int previousLastId) {
        Log.d(TAG, url);
        Log.d(TAG, String.valueOf(previousLastId));
        int lastId = - 1;
        if (previousLastId != 0) {
            try {
                lastId = new ComicFetcher().execute(Constants.XKCD_LAST_COMIC_URL, this).get().getId();
            } catch (ExecutionException | InterruptedException e) {
                Log.d(TAG, e.getMessage());
            }

            if (lastId > 50/*previousLastId*/) {

                Intent intent = new Intent();
                intent.setAction(ACTION_NEW);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
    }
}
