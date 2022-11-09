package com.example.profileswithapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button btnCreate;
    private ListView lvProfiles;
    public static String nameText;
    public static String jobText;
    public static String emailText;
    public static Boolean createOrChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
    }

    private void initFields() {
        btnCreate = findViewById(R.id.btnCreate);
        lvProfiles = findViewById(R.id.lvDatabase);
    }


    @Override
    protected void onStart() {
        super.onStart();
        fillListView();
        btnListeners();
    }


    private void btnListeners() {
        btnCreate.setOnClickListener(v -> {
            createOrChange = true;
            createOrChangeIntent();
        });
    }

    private void createOrChangeIntent(){
        Intent intent = new Intent(this, CreateAndEditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);;
    }

    private void fillListView() {
        listProfiles.clear();
        profileAdapter = new ProfileAdapter(MainActivity.this, listProfiles);
        lvProfiles.setAdapter(profileAdapter);
        new GetProfiles().execute();
        lvProfiles.setOnItemClickListener((parent, view, position, id) -> {
            createOrChange = false;
            TextView nameTv = view.findViewById(R.id.Name);
            nameText = nameTv.getText().toString();
            TextView jobTv = view.findViewById(R.id.Job);
            jobText = jobTv.getText().toString();
            TextView emailTv = view.findViewById(R.id.Email);
            emailText = emailTv.getText().toString();
            //ImageView avatar = findViewById(R.id.ivAvatar);
            //Bitmap avatarBm = ((BitmapDrawable) avatar.getDrawable()).getBitmap();

            createOrChangeIntent();
            //ImageView ivAvatar = findViewById(R.id.ivAvatar);
            //ivAvatar.setImageBitmap(avatarBm);
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