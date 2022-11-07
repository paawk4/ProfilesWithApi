package com.example.profileswithapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillListView();
        btnListeners();
    }


    private void btnListeners() {
        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            createOrChangeIntent();
        });
    }

    private void createOrChangeIntent(){
        Intent intent = new Intent(this, CreateAndEditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intent);;
    }

    private void fillListView() {
        listProfiles.clear();
        ListView lvProfiles = findViewById(R.id.lvDatabase);
        profileAdapter = new ProfileAdapter(MainActivity.this, listProfiles);
        lvProfiles.setAdapter(profileAdapter);
        new GetProfiles().execute();
        lvProfiles.setOnItemClickListener((parent, view, position, id) -> {
            String nameText, jobText, emailText;
            TextView nameTv = findViewById(R.id.Name);
            nameText = nameTv.getText().toString();
            TextView jobTv = findViewById(R.id.Job);
            jobText = jobTv.getText().toString();
            TextView emailTv = findViewById(R.id.Email);
            emailText = emailTv.getText().toString();
//            ImageView avatar = findViewById(R.id.ivAvatar);
//            Bitmap avatarBm = ((BitmapDrawable) avatar.getDrawable()).getBitmap();

            createOrChangeIntent();

            TextView editName = findViewById(R.id.editName);
            editName.setText(nameText);
            TextView editJob = findViewById(R.id.editJob);
            editJob.setText(jobText);
            TextView editEmail = findViewById(R.id.editEmail);
            editEmail.setText(emailText);
//            ImageView ivAvatar = findViewById(R.id.ivAvatar);
//            ivAvatar.setImageBitmap(avatarBm);
        });
    }

    private class GetProfiles extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ЧетвериковПв/Api/Personal_inf");
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
                            profileJson.getString("Name"),
                            profileJson.getString("Job"),
                            profileJson.getString("Email"),
                            profileJson.getString("Image")
                    );
                    listProfiles.add(tempProfile);
                    profileAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored){

            }
        }
    }
}