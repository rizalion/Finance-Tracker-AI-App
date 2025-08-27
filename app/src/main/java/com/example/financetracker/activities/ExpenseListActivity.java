package com.example.financetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.financetracker.R;
import com.example.financetracker.adapters.ExpenseAdapter;
import com.example.financetracker.database.DatabaseHelper;
import com.example.financetracker.models.Expense;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private ListView lvExpenses;
    private Spinner spFilter;
    private DatabaseHelper db;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        try {
            db = new DatabaseHelper(this);
            lvExpenses = findViewById(R.id.lvExpenses);
            spFilter = findViewById(R.id.spFilter);

            setupFilterSpinner();
            loadExpenses("All");
        } catch (Exception e) {
            showError("Setup failed: " + e.getMessage());
        }
    }

    private void setupFilterSpinner() {
        try {
            ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this,
                    R.array.days_of_week, android.R.layout.simple_spinner_item);
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFilter.setAdapter(filterAdapter);

            spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedFilter = parent.getItemAtPosition(position).toString();
                    loadExpenses(selectedFilter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            showError("Filter setup failed: " + e.getMessage());
        }
    }

    private void loadExpenses(String filter) {
        try {
            List<Expense> expenses;
            if (filter.equals("All")) {
                expenses = db.getAllExpenses();
            } else {
                expenses = db.getExpensesByCategory(filter);
            }

            adapter = new ExpenseAdapter(this, expenses);
            lvExpenses.setAdapter(adapter);
        } catch (Exception e) {
            showError("Failed to load expenses: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}