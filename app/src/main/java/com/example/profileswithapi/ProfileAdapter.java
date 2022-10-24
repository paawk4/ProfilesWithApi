package com.example.profileswithapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter implements Filterable {
    LayoutInflater inflater;
    private ArrayList<Profile> mOriginalValues;
    private ArrayList<Profile> mDisplayedValues;

    public ProfileAdapter(Context context, ArrayList<Profile> profiles) {
        this.mOriginalValues = profiles;
        this.mDisplayedValues = profiles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        RelativeLayout rlContainer;
        TextView tvName, tvJob, tvEmail;
        ImageView ivAvatar;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.sample_item, null);
            holder.rlContainer = convertView.findViewById(R.id.rlContainer);
            holder.tvName = convertView.findViewById(R.id.Name);
            holder.tvJob = convertView.findViewById(R.id.Job);
            holder.tvEmail = convertView.findViewById(R.id.Email);
            holder.ivAvatar = convertView.findViewById(R.id.list_itemAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(mDisplayedValues.get(i).name);
        holder.tvJob.setText(mDisplayedValues.get(i).job);
        holder.tvEmail.setText(mDisplayedValues.get(i).email);
        holder.ivAvatar.setImageBitmap(mDisplayedValues.get(i).image);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<Profile>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Profile> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(mDisplayedValues);
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Profile(mOriginalValues.get(i).name, mOriginalValues.get(i).job, mOriginalValues.get(i).email, mOriginalValues.get(i).image));
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
