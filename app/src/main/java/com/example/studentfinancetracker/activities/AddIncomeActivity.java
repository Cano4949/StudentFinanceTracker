package com.example.studentfinancetracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentfinancetracker.R;
import com.example.studentfinancetracker.database.DatabaseHelper;
import com.example.studentfinancetracker.utils.SessionManager;

import java.util.Calendar;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText titleInput, amountInput, dateInput;
    private Spinner frequencySpinner;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        titleInput = findViewById(R.id.editTitle);
        amountInput = findViewById(R.id.editAmount);
        dateInput = findViewById(R.id.editDate);
        frequencySpinner = findViewById(R.id.spinnerFrequency);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        frequencySpinner.setAdapter(adapter);

        dateInput.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.btnSaveIncome).setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            double amount = Double.parseDouble(amountInput.getText().toString());
            String frequency = frequencySpinner.getSelectedItem().toString();
            String date = dateInput.getText().toString();
            int userId = session.getUserId();

            if (dbHelper.addIncome(title, amount, frequency, date, userId)) {
                Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, y, m, d) -> dateInput.setText(y + "-" + (m + 1) + "-" + d),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
