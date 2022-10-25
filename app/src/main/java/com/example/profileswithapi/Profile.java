package com.example.profileswithapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private int Id;
    private String Name;
    private String Job;
    private String Image;

    protected Profile(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Job = in.readString();
        Image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Job);
        dest.writeString(Image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
