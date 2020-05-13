package com.example.popularmovies.url;

import android.os.Parcel;
import android.os.Parcelable;

public class url implements Parcelable {
    private String original_name;
    private String overview;
    private String release_date;
    private String poster_path;
    private String rating;

    public url(){

    }

    public url(url in) {
        this.setOriginal_name(in.getOriginal_name());
        this.setOverview(in.getOverview());
        this.setRelease_date(in.getRelease_date());
        this.setPoster_path(in.getPoster_path());
        this.setRating(in.getRating());

    }


    public url(String original_name, String overview, String release_date, String poster_path, String rating) {
        this.original_name = original_name;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.rating = rating;
    }


    public static final Creator<url> CREATOR = new Creator<url>() {
        @Override
        public url createFromParcel(Parcel in) {
            return new url(in);
        }

        @Override
        public url[] newArray(int size) {
            return new url[size];
        }
    };

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_name);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(rating);
    }

    public url(Parcel in) {
        this.setOriginal_name(in.readString());
        this.setOverview(in.readString());
        this.setRelease_date(in.readString());
        this.setPoster_path(in.readString());
        this.setRating(in.readString());
    }
}
