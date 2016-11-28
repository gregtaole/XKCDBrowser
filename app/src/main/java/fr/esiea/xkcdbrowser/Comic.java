package fr.esiea.xkcdbrowser;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Comic implements Parcelable {
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

    private Comic(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.alt = in.readString();
        this.publicationDate = in.readString();
        this.imageUri = Uri.parse(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.title);
        out.writeString(this.alt);
        out.writeString(this.publicationDate);
        out.writeString(this.imageUri.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Comic> CREATOR = new Parcelable.Creator<Comic>() {
        @Override
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        @Override
        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

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
