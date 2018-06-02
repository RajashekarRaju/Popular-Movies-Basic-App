package com.example.rkrjstdio.popularmoviesbasic;

import android.os.Parcel;
import android.os.Parcelable;

// Movie model implementing Parcelable
public class Movie implements Parcelable {

    // Declaring private variables set of string objects
    private final String title;
    private final String popularity;
    private final String voteAverage;
    private final String imageView;
    private final String overview;
    private final String releaseDate;
    private final String language;

    // Main constructor for list of variables
    Movie(String title, //
          String popularity,
          String voteAverage,
          String imageView,
          String overview,
          String releaseDate,
          String language) {

        this.title = title;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.imageView = imageView;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.language = language;
    }

    //constructor used for parcel
    private Movie(Parcel in) {
        title = in.readString();
        popularity = in.readString();
        voteAverage = in.readString();
        imageView = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        language = in.readString();
    }

    // Creator used when un-parceling our parcel (creating the object)
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

    // getters for each object values into adapter class and returns values as strings
    public String getTitle() {
        return title;
    }

    public String getImageView() {
        return imageView;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    //return hashcode of object
    @Override
    public int describeContents() {
        return 0;
    }

    //write object values to parcel for movie data
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(voteAverage);
        dest.writeString(imageView);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(language);
    }
}
