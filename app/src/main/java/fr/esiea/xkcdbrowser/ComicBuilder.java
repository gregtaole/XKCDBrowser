package fr.esiea.xkcdbrowser;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ComicBuilder {
    private static volatile ComicBuilder instance = null;

    private ComicBuilder() {
        super();
    }

    public static ComicBuilder getInstance() {
        if(ComicBuilder.instance == null) {
            synchronized (ComicBuilder.class) {
                if (ComicBuilder.instance == null) {
                    ComicBuilder.instance = new ComicBuilder();
                }
            }
        }
        return ComicBuilder.instance;
    }

    public Comic buildComic (String comicURL) {
        Comic newComic;

        try {
            URL url = new URL(comicURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                newComic = new Comic(sb.toString());

                return newComic;
            }
        }
        catch (IOException e) {
            Log.d("ComicBuilder", e.getMessage());
        }

        return null;
    }
}