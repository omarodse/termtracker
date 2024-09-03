package com.sld.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.User;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        repository = new Repository(getApplication());

        // Set up the register button click listener
        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show());
                return;
            }

            repository.getUserByUsername(username, user -> {
                if (user != null) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Username already exists. Press LOGIN", Toast.LENGTH_LONG).show());
                } else {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setHashedPassword(hashedPassword);
                    repository.insert(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        // Clear fields
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            });
        });

        // Set up the login button click listener
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show());
                return;
            }

            repository.getUserByUsername(username, user -> {
                if (user != null && BCrypt.checkpw(password, user.getHashedPassword())) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Clear fields
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show());
                }
            });
        });
    }
}