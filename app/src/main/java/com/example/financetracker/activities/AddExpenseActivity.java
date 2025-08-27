package com.example.financetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.financetracker.R;
import com.example.financetracker.database.DatabaseHelper;
import com.example.financetracker.models.Expense;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private Spinner spCategory;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        try {
            db = new DatabaseHelper(this);

            // Initialize views
            etAmount = findViewById(R.id.etAmount);
            etDescription = findViewById(R.id.etDescription);
            spCategory = findViewById(R.id.spCategory);
            btnSave = findViewById(R.id.btnSave);

            // Set up category spinner
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.expense_categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);

            btnSave.setOnClickListener(v -> saveExpense());
        } catch (Exception e) {
            showError("Setup failed: " + e.getMessage());
        }
    }

    private void saveExpense() {
        try {
            String amountStr = etAmount.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = spCategory.getSelectedItem().toString();

            if (amountStr.isEmpty()) {
                etAmount.setError("Amount is required");
                etAmount.requestFocus();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                etAmount.setError("Amount must be positive");
                etAmount.requestFocus();
                return;
            }

            // Get current day of week (0-6)
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            Expense expense = new Expense(amount, category, description, dayOfWeek);
            db.addExpense(expense);

            Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            etAmount.setError("Invalid number format");
            etAmount.requestFocus();
        } catch (Exception e) {
            showError("Failed to save: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}