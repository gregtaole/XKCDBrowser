package fr.esiea.xkcdbrowser;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FavManager extends Activity {
    final static String TAG = "FavManager";
    final static String favFilename = "favorites.txt";

    private ArrayList<Integer> favorites;
    private Context context;

    private static FavManager INSTANCE = null;

    public static FavManager getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new FavManager(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private FavManager(Context context) {
        this.context = context;
        favorites = new ArrayList<>();
        loadFavorites();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addToFavorites(Comic comic) throws AlreadyFavoriteException {
        Log.d(TAG, favorites.toString());

        if(isAlreadyFavorites(comic.getId())) {
            throw new AlreadyFavoriteException();
        }

        FileOutputStream outputStream;
        String separator = System.getProperty("line.separator");

        try {
            outputStream = context.openFileOutput(favFilename, Context.MODE_APPEND);
            outputStream.write(String.valueOf(comic.getId()).getBytes());
            outputStream.write(separator.getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        loadFavorites(); //Reload list
        Log.d(TAG, favorites.toString());
    }

    public boolean isAlreadyFavorites(Integer comicId) {
        return favorites.contains(comicId);
    }

    public ArrayList<Integer> getFavorites() {
        return favorites;
    }

    private void loadFavorites() {
        //Reset favorites list
        favorites.clear();

        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = context.openFileInput(favFilename);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                favorites.add(Integer.valueOf(line));
            }
            inputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
