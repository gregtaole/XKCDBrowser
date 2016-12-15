package fr.esiea.xkcdbrowser;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        int lastId = - 1;
        if (previousLastId != 0) {
            try {
                URL lastUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) lastUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    lastId = jsonObject.getInt("num");
                }
            }
            catch (IOException | JSONException e) {
                Log.d("ComicBuilder", e.getMessage());
            }
            Log.d(TAG, "previousLastId" + String.valueOf(previousLastId) + ", lastId" + String.valueOf(lastId));
            if (lastId > previousLastId) {

                Intent intent = new Intent();
                intent.setAction(ACTION_NEW);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
    }
}
