package fr.esiea.xkcdbrowser;

import java.net.URI;

public class Comic {
    private int id;
    private String title;
    private String alt;
    private String publicationDate;
    private URI imageURI;

    public Comic(int id, String title, String alt, String publicationDate, URI imageURI) {
        this.id = id;
        this.title = title;
        this.alt = alt;
        this.publicationDate = publicationDate;
        this.imageURI = imageURI;
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
    public URI getImageURI() {
        return imageURI;
    }
}
