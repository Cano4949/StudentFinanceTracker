package com.example.studentfinancetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TextView textDate, textIncome, textExpense, textSaldo;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Session check
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Views
        textDate = findViewById(R.id.textDate);
        textIncome = findViewById(R.id.textTotalIncome);
        textExpense = findViewById(R.id.textTotalExpenses);
        textSaldo = findViewById(R.id.textSaldo);
        pieChart = findViewById(R.id.pieChart);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAddIncome = findViewById(R.id.btnAddIncome);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnViewIncomes = findViewById(R.id.btnViewIncomes);
        Button btnViewExpenses = findViewById(R.id.btnViewExpenses);



        // Event handlers
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnAddIncome.setOnClickListener(v ->
                startActivity(new Intent(this, AddIncomeActivity.class)));

        btnAddExpense.setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class)));

        btnViewIncomes.setOnClickListener(v ->
                startActivity(new Intent(this, IncomeListActivity.class)));

        btnViewExpenses.setOnClickListener(v ->
                startActivity(new Intent(this, ExpenseListActivity.class)));

        handleRecurringExpenses();
        refreshDashboard();
    }

    private void handleRecurringExpenses() {
        int userId = session.getUserId();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String thisMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        String lastMonthRun = session.getLastRecurringUpdate("lastMonthlyRun");
        if (!thisMonth.equals(lastMonthRun)) {
            dbHelper.generateRecurringEntries(userId, today);
            session.setLastRecurringUpdate("lastMonthlyRun", thisMonth);
        }
    }

    private void refreshDashboard() {
        int userId = session.getUserId();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());

        double income = dbHelper.getTotalMonthlyIncome(userId, currentMonth);
        double expenses = dbHelper.getTotalMonthlyExpenses(userId, currentMonth);
        double saldo = income - expenses;

        DecimalFormat df = new DecimalFormat("#0.00");
        textDate.setText("Today: " + today);
        textIncome.setText("Income: $" + df.format(income));
        textExpense.setText("Expenses: $" + df.format(expenses));
        textSaldo.setText("Saldo: $" + df.format(saldo));

        updatePieChart(income, expenses);
    }

    private void updatePieChart(double income, double expenses) {
        if (pieChart == null) return;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) expenses, "Spent"));
        entries.add(new PieEntry((float) (income - expenses), "Remaining"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{R.color.red, R.color.green}, this);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("This Month");
        pieChart.setCenterTextSize(14f);
        pieChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }
}
