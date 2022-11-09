package com.example.profileswithapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProfileAdapter profileAdapter;
    private final List<Profile> listProfiles = new ArrayList<>();
    private Button btnCreate;
    private Button btnRefresh;
    private Button btnSearch;
    private ListView lvProfiles;
    public static String nameText;
    public static String jobText;
    public static String emailText;
    public static Boolean createOrChange;
    public static int currentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
    }

    private void initFields() {
        btnCreate = findViewById(R.id.btnCreate);
        lvProfiles = findViewById(R.id.lvDatabase);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnSearch = findViewById(R.id.btnSearch);
    }

    @Override
    protected void onStart() {
        super.onStart();

        fillListView();
        btnListeners();
        SpinnerInit();
    }

    private void btnListeners() {
        btnCreate.setOnClickListener(v -> {
            createOrChange = true;
            createOrChangeIntent();
        });
        btnRefresh.setOnClickListener(v -> {
            fillListView();
        });
        btnSearch.setOnClickListener(v -> {
            Search();
        });
    }

    private void createOrChangeIntent(){
        Intent intent = new Intent(this, CreateAndEditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);;
    }

    private void fillListView() {

        profileAdapter = new ProfileAdapter(MainActivity.this, listProfiles);
        lvProfiles.setAdapter(profileAdapter);
        new GetProfiles().execute();
        lvProfiles.setOnItemClickListener((parent, view, position, id) -> {
            createOrChange = false;
            currentId = (int)id;
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

    public void SpinnerInit(){
        try {
            Spinner spinner = findViewById(R.id.spinnerSort);
            spinner.setOnItemSelectedListener(this);

            List<String> categories = new ArrayList<>();
            categories.add("By name a-z");
            categories.add("By position a-z");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);

        }
        catch (Exception ignored){
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            ListView listViewDB = findViewById(R.id.lvDatabase);
            String item = parent.getItemAtPosition(position).toString();
            if(item.equals("By name a-z")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(listProfiles, Comparator.comparing(o -> o.getName().toLowerCase(Locale.ROOT)));
                }
                profileAdapter = new ProfileAdapter(MainActivity.this, listProfiles);
                listViewDB.setAdapter(profileAdapter);
            }
            else if(item.equals("By position a-z")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(listProfiles, Comparator.comparing(o -> o.getJob().toLowerCase(Locale.ROOT)));
                }
                profileAdapter = new ProfileAdapter(MainActivity.this, listProfiles);
                listViewDB.setAdapter(profileAdapter);
            }
        }
        catch (Exception ex){
            Toast.makeText(parent.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    public void Search() {
        try {
            EditText searchBar = findViewById(R.id.searchBar);
            TextView tw = findViewById(R.id.textView2);
            if(searchBar.isFocused()){
                searchBar.setVisibility(View.GONE);
                tw.setVisibility(View.VISIBLE);
            }else{
                searchBar.setVisibility(View.VISIBLE);
                tw.setVisibility(View.GONE);

                searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        profileAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
        }
        catch (Exception ex){
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
        }
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
                listProfiles.clear();
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
                    profileAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored){

            }
        }

    }
}