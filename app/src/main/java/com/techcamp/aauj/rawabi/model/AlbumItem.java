package com.techcamp.aauj.rawabi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumItem implements Parcelable{
    private String imgUrl; //main image for the album
    private String title;
    private String description;
    private int id;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AlbumItem() {
    }

    protected AlbumItem(Parcel in) {
        imgUrl = in.readString();
        title = in.readString();
        id = in.readInt();
        description = in.readString();
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel in) {
            return new AlbumItem(in);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(description);
    }
}
