package com.example.studentfinancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.adapters.IncomeAdapter;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

public class IncomeListActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);

        // Initialize views and helpers
        listView = findViewById(R.id.incomeListView);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Get all income entries for the current user
        Cursor cursor = dbHelper.getAllIncomes(session.getUserId());

        // Set up the custom IncomeAdapter with cursor
        IncomeAdapter adapter = new IncomeAdapter(this, cursor);
        listView.setAdapter(adapter);
    }
}
