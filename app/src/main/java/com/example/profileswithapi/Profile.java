package com.example.profileswithapi;

import android.graphics.Bitmap;

public class Profile {
    String name, job, email;
    Bitmap image;

    public Profile(String name, String job, String email, Bitmap image) {
        this.name = name;
        this.job = job;
        this.email = email;
        this.image = image;
    }
}
