package fr.esiea.xkcdbrowser;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewComicService extends IntentService {
    public static final String ACTION_NEW = "fr.esiea.xkcdbrowser.action.NEW";

    final static String TAG = "NewComicService";

    public NewComicService() {
        super("NewComicService");
        Log.d(TAG, "Service started!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleActionNew(intent.getStringExtra(MainActivity.EXTRA_URL));
    }

    private void handleActionNew(String url) {
        Log.d(TAG, url);
        int lastId = - 1;
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(Constants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        int previousLastId = sharedPreferences.getInt(Constants.CURRENT_LAST_ID, 0);

        if (previousLastId != 0) {
            try {
                URL lastUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) lastUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

            if (lastId > previousLastId) {
                Intent intent = new Intent();
                intent.setAction(ACTION_NEW);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
    }
}
