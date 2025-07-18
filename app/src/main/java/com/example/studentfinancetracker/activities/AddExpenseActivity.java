package com.example.studentfinancetracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

import java.util.Calendar;
import java.util.Locale;


public class AddExpenseActivity extends AppCompatActivity {

    private EditText titleInput, amountInput, dateInput;
    private Spinner frequencySpinner;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Initialisieren
        titleInput = findViewById(R.id.editTitle);
        amountInput = findViewById(R.id.editAmount);
        dateInput = findViewById(R.id.editDate);
        frequencySpinner = findViewById(R.id.spinnerFrequency);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Spinner-Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);

        // Datumsauswahl
        dateInput.setOnClickListener(v -> showDatePicker());

        // Speichern-Button
        findViewById(R.id.btnSaveExpense).setOnClickListener(v -> saveExpense());
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



    private void saveExpense() {
        String title = titleInput.getText().toString().trim();
        String amountStr = amountInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String frequency = frequencySpinner.getSelectedItem().toString();
        int userId = session.getUserId();

        // Eingaben validieren
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

        boolean success = dbHelper.addExpense(userId, title, amount, frequency, date);

        if (success) {
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();

            // Optional: MainActivity neu starten, damit Dashboard aktualisiert wird
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show();
        }
    }

}
