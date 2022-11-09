package com.example.profileswithapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    List<Profile> profiles;

    private List<Profile> mOriginalValues;

    public ProfileAdapter(Context mContext, List<Profile> profileList) {
        this.mContext = mContext;
        this.profiles = profileList;
        this.mOriginalValues = profileList;
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
    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                profiles = (ArrayList<Profile>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Profile> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(profiles);
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Profile(mOriginalValues.get(i).getId(),
                                    mOriginalValues.get(i).getName(),
                                    mOriginalValues.get(i).getJob(),
                                    mOriginalValues.get(i).getEmail(),
                                    mOriginalValues.get(i).getImage()));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
    }
}
