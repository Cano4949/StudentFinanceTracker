package com.example.studentfinancetracker.activities;

import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textIncome, textExpense, textSaldo, textNextExpense;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textIncome = findViewById(R.id.textTotalIncome);
        textExpense = findViewById(R.id.textTotalExpenses);
        textSaldo = findViewById(R.id.textSaldo);
        textNextExpense = findViewById(R.id.textNextExpense);

        Button btnAddIncome = findViewById(R.id.btnAddIncome);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnLogout = findViewById(R.id.btnLogout);

        Button btnViewIncomes = findViewById(R.id.btnViewIncomes);
        Button btnViewExpenses = findViewById(R.id.btnViewExpenses);


        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        int userId = session.getUserId();
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Load dashboard data
        double income = dbHelper.getTotalMonthlyIncome(userId, currentMonth);
        double expenses = dbHelper.getTotalMonthlyExpenses(userId, currentMonth);
        double saldo = income - expenses;
        String nextExpense = dbHelper.getNextExpenseTitle(userId, today);

        textIncome.setText("Income: $" + income);
        textExpense.setText("Expenses: $" + expenses);
        textSaldo.setText("Saldo: $" + saldo);
        textNextExpense.setText("Next Expense: " + nextExpense);

        btnAddIncome.setOnClickListener(v ->
                startActivity(new Intent(this, AddIncomeActivity.class)));

        btnAddExpense.setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class)));

        btnViewIncomes.setOnClickListener(v ->
                startActivity(new Intent(this, IncomeListActivity.class)));

        btnViewExpenses.setOnClickListener(v ->
                startActivity(new Intent(this, ExpenseListActivity.class)));

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
