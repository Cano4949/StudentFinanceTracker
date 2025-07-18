package com.example.studentfinancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

public class ExpenseListActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        listView = findViewById(R.id.expenseListView);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        Cursor cursor = dbHelper.getAllExpenses(session.getUserId());

        String[] from = {"title", "amount", "frequency", "date"};
        int[] to = {R.id.textTitle, R.id.textAmount, R.id.textFrequency, R.id.textDate};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item_expense, cursor, from, to, 0);

        listView.setAdapter(adapter);
    }
}
