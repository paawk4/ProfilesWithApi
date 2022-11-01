package com.example.profileswithapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;
import androidx.core.graphics.BitmapKt;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAndEditProfileActivity extends AppCompatActivity {
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private EditText nameET;
    private EditText jobET;
    private EditText emailET;
    private Button btnCreateProfile;
    private TextView windowTitle;
    private ProgressBar loadingPB;
    private ImageView avatarImageView;
    private Button btnAddImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        initObjects();
        createOrChange(true);
    }

    private void initObjects() {
        nameET = findViewById(R.id.editName);
        jobET = findViewById(R.id.editJob);
        emailET = findViewById(R.id.editEmail);
        btnCreateProfile = findViewById(R.id.btnCreateProfile);
        windowTitle = findViewById(R.id.windowTitle);
        loadingPB = findViewById(R.id.idLoadingPB);
        avatarImageView = findViewById(R.id.ivAvatar);
        btnAddImage = findViewById(R.id.btnAddImage);
    }

    public void createOrChange(Boolean bool) {
        if (bool) {
            createProfile();
        } else {
            changeProfile();
        }
    }

    private void changeProfile() {
    }


    private void GetImageFromDevice(){
        btnAddImage.setOnClickListener(v -> {
            Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFileIntent.setType("*/*");
            chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
            startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();

                    String filePath;
                    try {
                        filePath = FileUtils.getPath(this.getBaseContext(), fileUri);
                        ImageView image = findViewById(R.id.ivAvatar);
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                        ByteArrayOutputStream outStream =

                        image.setImageBitmap(bitmap);

                        byte[] byteArray = outStream.toByteArray();
                        outStream.close();

                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    } catch (Exception e) {
                        Toast.makeText(this.getBaseContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createProfile() {
        fillHints();

        btnCreateProfile.setOnClickListener(v -> {
            String name = nameET.getText().toString();
            String job = jobET.getText().toString();
            String email = emailET.getText().toString();
            String image = avatarImageView.getDrawable().toString();
            postData(name, job, email);
        });

    }

    private void fillHints() {
        nameET.setHint("Введите имя");
        jobET.setHint("Введите должность");
        emailET.setHint("Введите почту");
        windowTitle.setText("Добавление профиля");
    }


    private void postData(String name, String job, String email) {

        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5101/NGKNN/ЧетвериковПв/api/Personal_Inf")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Profile modal = new Profile(name, job, email, image);

        Call<Profile> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                Toast.makeText(CreateAndEditProfileActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                loadingPB.setVisibility(View.GONE);

                nameET.setText("");
                jobET.setText("");
                emailET.setText("");

                Profile responseFromAPI = response.body();

                assert responseFromAPI != null;
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getJob();

                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }

    public void fillHintsChange() {
        nameET.setHint("Измените название");
        jobET.setHint("Измените должность");
        emailET.setHint("Измените почту");
    }
}
