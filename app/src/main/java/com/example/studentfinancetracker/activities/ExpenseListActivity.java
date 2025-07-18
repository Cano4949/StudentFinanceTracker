package com.example.studentfinancetracker.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.adapters.ExpenseAdapter;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

public class ExpenseListActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        listView = findViewById(R.id.expenseListView);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        Cursor cursor = dbHelper.getAllExpenses(session.getUserId());
        adapter = new ExpenseAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
    }
}
