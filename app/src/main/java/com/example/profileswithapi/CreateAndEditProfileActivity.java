package com.example.profileswithapi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAndEditProfileActivity extends AppCompatActivity {
    private TextView nameTV;
    private TextView jobTV;
    private TextView emailTV;
    private Button btnCreateProfile;
    private TextView windowTitle;

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

    private void createProfile() {
        fillHints();

        btnCreateProfile.setOnClickListener(v -> {
            String name = nameTV.getText().toString();
            String job = jobTV.getText().toString();
            String email = emailTV.getText().toString();

        });

    }

    private void fillHints() {
        nameTV.setHint("Введите имя");
        jobTV.setHint("Введите должность");
        emailTV.setHint("Введите почту");
        windowTitle.setText("Добавление профиля");
    }
}
