package com.example.studentfinancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (dbHelper.registerUser(user, pass)) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.textLoginLink).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));
    }
}
