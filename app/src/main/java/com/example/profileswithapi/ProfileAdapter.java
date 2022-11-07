package com.example.profileswithapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {
    private final Context mContext;
    List<Profile> profiles;

    public ProfileAdapter(Context mContext, List<Profile> profileList) {
        this.mContext = mContext;
        this.profiles = profileList;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Object getItem(int i) {
        return profiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return profiles.get(i).getId();
    }

    private Bitmap getUserImage(String encodeImage) {
        if (encodeImage != null && !encodeImage.equals("null")) {
            byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.sample_item, null);

        TextView name = v.findViewById(R.id.Name);
        TextView job = v.findViewById(R.id.Job);
        TextView email = v.findViewById(R.id.Email);
        ImageView image = v.findViewById(R.id.Avatar);

        Profile profile = profiles.get(i);

        name.setText(profile.getName());
        job.setText(profile.getJob());
        email.setText(profile.getEmail());
        image.setImageBitmap(getUserImage(profile.getImage()));

        return v;
    }

}
