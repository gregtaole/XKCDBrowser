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

    public void addToFavorites(Comic comic) throws AlreadyFavoriteException {
        Log.d(TAG, favorites.toString());

        if(isAlreadyFavorites(comic)) {
            throw new AlreadyFavoriteException();
        }

        favorites.add(comic.getId());
        updateFavorites();

        Log.d(TAG, favorites.toString());
    }

    public void removeFavorite(Comic comic) {
        Log.d(TAG, favorites.toString());

        if(isAlreadyFavorites(comic)) {
            favorites.remove(favorites.indexOf(comic.getId()));
            updateFavorites();
        }

        Log.d(TAG, favorites.toString());
    }

    private void updateFavorites() {
        writeFavorites(); //Write list
        loadFavorites(); //Reload list
    }

    public boolean isAlreadyFavorites(Comic comic) {
        return favorites.contains(comic.getId());
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

    private void writeFavorites() {
        FileOutputStream outputStream;
        String separator = System.getProperty("line.separator");

        try {
            outputStream = context.openFileOutput(favFilename, Context.MODE_PRIVATE);
            for (Integer id : favorites) {
                outputStream.write(String.valueOf(id).getBytes());
                outputStream.write(separator.getBytes());
            }
            outputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
