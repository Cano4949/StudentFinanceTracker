package com.example.studentfinancetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

import java.util.Calendar;
import java.util.Locale;


public class AddIncomeActivity extends AppCompatActivity {

    private EditText titleInput, amountInput, dateInput;
    private Spinner frequencySpinner;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        // Views finden
        titleInput = findViewById(R.id.editTitle);
        amountInput = findViewById(R.id.editAmount);
        dateInput = findViewById(R.id.editDate);
        frequencySpinner = findViewById(R.id.spinnerFrequency);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Spinner füllen
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);

        // DatePicker öffnen
        dateInput.setOnClickListener(v -> showDatePicker());

        // Button-Klick
        findViewById(R.id.btnSaveIncome).setOnClickListener(v -> saveIncome());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    dateInput.setText(formattedDate);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }




    private void saveIncome() {
        String title = titleInput.getText().toString().trim();
        String amountStr = amountInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();
        int userId = session.getUserId();

        // Validierung
        if (title.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Speichern
        boolean success = dbHelper.addIncome(userId, title, amount, frequency, date);
        if (success) {
            Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show();

            // → MainActivity neuladen
            finish();
        } else {
            Toast.makeText(this, "Error saving income", Toast.LENGTH_SHORT).show();
        }
    }
}
