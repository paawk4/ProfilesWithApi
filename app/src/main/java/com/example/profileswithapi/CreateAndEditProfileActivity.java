package com.example.profileswithapi;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAndEditProfileActivity extends AppCompatActivity {
    private int SELECT_PICTURE = 200;

    private EditText nameET;
    private EditText jobET;
    private EditText emailET;
    private ImageView avatarIV;
    private Button btnCreateProfile;
    private Button btnUploadImage;
    private Button goBackBtn;
    private Button btnDelete;
    private TextView windowTitle;
    private ProgressBar loadingPB;

    private String stringImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        initObjects();
        createOrChange();
    }

    private void initObjects() {
        nameET = findViewById(R.id.editName);
        jobET = findViewById(R.id.editJob);
        emailET = findViewById(R.id.editEmail);
        btnCreateProfile = findViewById(R.id.btnCreateProfile);
        windowTitle = findViewById(R.id.windowTitle);
        loadingPB = findViewById(R.id.idLoadingPB);
        avatarIV = findViewById(R.id.ivAvatar);
        goBackBtn = findViewById(R.id.btnGoBack);
        btnUploadImage = findViewById(R.id.btnAddImage);
        btnDelete = findViewById(R.id.btnDelete);
    }

    public void createOrChange() {
        fillHints();
        if (MainActivity.createOrChange) {
            createProfile();
        } else {
            changeProfile();
        }
    }

    private void createProfile() {
        windowTitle.setText("Добавление профиля");
        btnDelete.setVisibility(View.GONE);

        btnCreateProfile.setOnClickListener(v -> {
            if (nameET.getText().toString().isEmpty() || jobET.getText().toString().isEmpty()
                    || emailET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите значения", Toast.LENGTH_SHORT).show();
                return;
            }
            postData(nameET.getText().toString(), jobET.getText().toString(),
                    emailET.getText().toString(), stringImage);
            goBackIntent();
        });

        btnUploadImage.setOnClickListener(v -> {
            imageChooser();
        });

        goBackBtn.setOnClickListener(v -> {
            goBackIntent();
        });
    }

    private void postData(String name, String job, String email, String image) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl
                        ("https://ngknn.ru:5001/NGKNN/ЧетвериковПв/Api/Personal_inf/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Profile modal = new Profile(name, job, email, image);

        Call<Profile> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                Toast.makeText(CreateAndEditProfileActivity.this, "Данные добавлены",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(CreateAndEditProfileActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeProfile() {
        windowTitle.setText("Изменение профиля");
        btnDelete.setVisibility(View.VISIBLE);

        nameET.setText(MainActivity.nameText);
        jobET.setText(MainActivity.jobText);
        emailET.setText(MainActivity.emailText);

        btnCreateProfile.setOnClickListener(v -> {
            if (nameET.getText().toString().isEmpty() || jobET.getText().toString().isEmpty()
                    || emailET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите значения", Toast.LENGTH_SHORT).show();
                return;
            }
            putData(nameET.getText().toString(), jobET.getText().toString(), emailET.getText().toString(), stringImage);
            goBackIntent();
        });

        goBackBtn.setOnClickListener(v -> {
            goBackIntent();
        });
        btnUploadImage.setOnClickListener(v -> {
            imageChooser();
        });
    }

    private void putData(String name, String job, String email, String image) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl
                        ("https://ngknn.ru:5001/NGKNN/ЧетвериковПв/Api/Personal_inf/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Profile modal = new Profile(name, job, email, image);

        Call<Profile> call = retrofitAPI.updateData(modal);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                Toast.makeText(CreateAndEditProfileActivity.this, "Data updated to API", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(CreateAndEditProfileActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void goBackIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void fillHints() {
        nameET.setHint("Введите имя");
        jobET.setHint("Введите должность");
        emailET.setHint("Введите почту");
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    avatarIV.setImageURI(selectedImageUri);
                    stringImage = getImageString();
                }
            }
        }
    }

    private String getImageString() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) avatarIV.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
