package fr.esiea.xkcdbrowser;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public abstract class ComicFetcherInterface extends AppCompatActivity {
    public abstract ArrayList<Comic> getComics();
    public abstract ComicAdapter getComicAdapter();
}
