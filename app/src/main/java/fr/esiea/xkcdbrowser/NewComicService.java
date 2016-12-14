package fr.esiea.xkcdbrowser;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
        handleActionNew(intent.getStringExtra(MainActivity.EXTRA_URL));
    }

    private void handleActionNew(String url) {
        Log.d(TAG, url);
        Intent intent = new Intent();
        intent.setAction(ACTION_NEW);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
