package fr.esiea.xkcdbrowser;

import android.net.Uri;

public class Comic {
    private int id;
    private String title;
    private String alt;
    private String publicationDate;
    private Uri imageUri;

    public Comic(int id, String title, String alt, String publicationDate, Uri imageUri) {
        this.id = id;
        this.title = title;
        this.alt = alt;
        this.publicationDate = publicationDate;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlt() {
        return alt;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
    public Uri getImageUri() {
        return imageUri;
    }
}
