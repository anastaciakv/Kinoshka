package de.proximity.kinoshka.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final String ITEM_KEY = "MOVIE_ITEM";
    public int voteCount;
    public int id;
    public boolean video;
    public double voteAverage;
    public String title;
    public double popularity;
    public String posterPath;
    public String originalLanguage;
    public String originalTitle;
    public String backdropPath;
    public boolean adult;
    public String overview;
    public String releaseDate;

    public Movie() {
    }

    private Movie(Parcel in) {
        voteCount = in.readInt();
        id = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(voteCount);
        parcel.writeInt(id);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeDouble(voteAverage);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(backdropPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    public interface SortMode {
        int mostPopular = 1;
        int topRated = 2;
    }
}
