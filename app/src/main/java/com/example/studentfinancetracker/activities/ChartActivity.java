package com.example.studentfinancetracker.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import java.util.ArrayList;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        pieChart = findViewById(R.id.pieChart);
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        loadExpenseChart();
    }

    private void loadExpenseChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Beispiel: gruppiere Ausgaben nach Titel (vereinfachte Version)
        Map<String, Double> expenseMap = dbHelper.getExpenseSummaryByTitle(session.getUserId());

        for (String title : expenseMap.keySet()) {
            float amount = expenseMap.get(title).floatValue();
            entries.add(new PieEntry(amount, title));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Ausgabenübersicht");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        Description description = new Description();
        description.setText("Ausgaben nach Titel");
        pieChart.setDescription(description);

        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
