package fr.esiea.xkcdbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject comicJson = new JSONObject(result);
                int id = comicJson.getInt("num");
                String title = comicJson.getString("title");
                String alt = comicJson.getString("alt");
                int year = comicJson.getInt("year");
                int month = comicJson.getInt("month");
                int day = comicJson.getInt("day");
                String publicationDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
                Uri imageURI = Uri.parse(comicJson.getString("img"));

                newComic = new Comic(id, title, alt, publicationDate, imageURI);
                Log.d("ComicBuilder", newComic.getTitle());
                return newComic;
            }
        }
        catch (JSONException | IOException e) {
            Log.d("ComicBuilder", e.getMessage());
        }

        return null;
    }
}

