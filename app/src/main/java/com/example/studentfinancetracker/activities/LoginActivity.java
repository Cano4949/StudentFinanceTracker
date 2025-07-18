package com.example.studentfinancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (dbHelper.checkUser(user, pass)) {
                int userId = dbHelper.getUserId(user);
                session.createLoginSession(userId);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.textRegisterLink).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
