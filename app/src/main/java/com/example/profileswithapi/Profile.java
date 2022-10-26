package com.example.profileswithapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private int Id;
    private final String Name;
    private final String Job;
    private final String Email;
    private final String Image;

    public String getEmail() {
        return Email;
    }

    public int getId() {
        return Id;
    }
    public String getName() {
        return Name;
    }

    public String getJob() {
        return Job;
    }

    public String getImage() {
        return Image;
    }
    public void setId(int id) {
        Id = id;
    }
    protected Profile(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Job = in.readString();
        Email = in.readString();
        Image = in.readString();
    }

    public Profile(int id, String name, String job, String email, String image) {
        this.Id = id;
        this.Name = name;
        this.Job = job;
        this.Email = email;
        this.Image = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Job);
        dest.writeString(Email);
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
}
