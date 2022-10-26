package com.example.profileswithapi;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProfileAdapter profileAdapter;
    private final List<Profile> listProfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvProfiles = findViewById(R.id.lvDatabase);
        profileAdapter = new ProfileAdapter((Context) MainActivity.this, (ArrayList<Profile>) listProfiles);
        lvProfiles.setAdapter(profileAdapter);

        new GetProfiles().execute();
    }

    private class GetProfiles extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ssfb.ngknn.local/NGKNN/ЧетвериковПв/api/Personal_Inf");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
                return result.toString();
            } catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0; i < tempArray.length(); i++){
                    JSONObject profileJson = tempArray.getJSONObject(i);
                    Profile tempProfile = new Profile(
                            profileJson.getInt("Id"),
                            profileJson.getString("Name"),
                            profileJson.getString("Job"),
                            profileJson.getString("Email"),
                            profileJson.getString("Image")
                    );
                    listProfiles.add(tempProfile);
                    profileAdapter.notifyDataSetChanged();
                }
            } catch (Exception ignored){

            }
        }
    }
}