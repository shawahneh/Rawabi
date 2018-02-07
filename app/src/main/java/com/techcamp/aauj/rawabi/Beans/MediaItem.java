package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alaam on 2/3/2018.
 */

public class MediaItem implements Parcelable {
    private String imageUrl;

    protected MediaItem(Parcel in) {
        imageUrl = in.readString();
    }

    public static final Creator<MediaItem> CREATOR = new Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel in) {
            return new MediaItem(in);
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };

    public MediaItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
    }
}
