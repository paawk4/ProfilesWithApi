package com.example.profileswithapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private int Id;
    private String Name;
    private String Job;
    private String Email;
    private String Image;

    public Profile(int id, String name, String job, String email, String image) {
        Id = id;
        Name = name;
        Job = job;
        Email = email;
        Image = image;
    }

    protected Profile(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Job = in.readString();
        Email = in.readString();
        Image = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public void setId(int id) {
        this.Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setJob(String job) {
        Job = job;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Job);
        dest.writeString(Email);
        dest.writeString(Image);
    }

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


}
