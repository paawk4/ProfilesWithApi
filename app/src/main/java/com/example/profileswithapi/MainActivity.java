package com.example.profileswithapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

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
    private List<Profile> listProfiles = new ArrayList<>();

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
                URL url = new URL("https://ngknn.ru:5101/NGKNN/%D0%A7%D0%B5%D1%82%D0%B2%D0%B5%D1%80%D0%B8%D0%BA%D0%BE%D0%B2%D0%9F%D0%B2/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

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
                            profileJson.getString("Id"),
                            profileJson.getString("Name"),
                            profileJson.getString("Job"),
                            profileJson.getString("Image")
                    );
                }
            } catch (Exception ex){

            }
        }
    }
}