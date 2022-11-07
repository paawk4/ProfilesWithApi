package com.example.profileswithapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAndEditProfileActivity extends AppCompatActivity {
    private TextView nameTV;
    private TextView jobTV;
    private TextView emailTV;
    private ImageView avatarIV;
    private Button btnCreateProfile;
    private Button goBackBtn;
    private TextView windowTitle;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        initObjects();
        createOrChange(true);
    }

    private void initObjects() {
        nameTV = findViewById(R.id.editName);
        jobTV = findViewById(R.id.editJob);
        emailTV = findViewById(R.id.editEmail);
        btnCreateProfile = findViewById(R.id.btnCreateProfile);
        windowTitle = findViewById(R.id.windowTitle);
        loadingPB = findViewById(R.id.idLoadingPB);
        avatarIV = findViewById(R.id.ivAvatar);
        goBackBtn = findViewById(R.id.btnGoBack);
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

    private void goBackIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void createProfile() {
        fillHints();

        btnCreateProfile.setOnClickListener(v -> {
            if (nameTV.getText().toString().isEmpty() && jobTV.getText().toString().isEmpty() && emailTV.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите значения", Toast.LENGTH_SHORT).show();
                return;
            }
            postData(nameTV.getText().toString(), jobTV.getText().toString(), emailTV.getText().toString(), null);
            goBackIntent();
        });

        goBackBtn.setOnClickListener(v -> {
            goBackIntent();
        });
    }

    private void postData(String name, String job, String email, String image) {
        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ЧетвериковПв/Api/Personal_inf/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);

        Profile modal = new Profile(name, job, email, image);

        Call<Profile> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                Toast.makeText(CreateAndEditProfileActivity.this, "Данные добавлены", Toast.LENGTH_SHORT).show();

                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(CreateAndEditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillHints() {
        nameTV.setHint("Введите имя");
        jobTV.setHint("Введите должность");
        emailTV.setHint("Введите почту");
        windowTitle.setText("Добавление профиля");
    }
}
