package com.example.financetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.financetracker.R;
import com.example.financetracker.database.DatabaseHelper;
import com.example.financetracker.ml.ExpensePredictor;
import com.example.financetracker.models.Expense;
import java.util.Calendar;
import java.util.List;

public class PredictionActivity extends AppCompatActivity {

    private Spinner spCategory, spDayOfWeek;
    private Button btnPredict;
    private TextView tvPrediction;
    private DatabaseHelper db;
    private ExpensePredictor predictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        try {
            db = new DatabaseHelper(this);
            predictor = new ExpensePredictor(this);

            spCategory = findViewById(R.id.spCategory);
            spDayOfWeek = findViewById(R.id.spDayOfWeek);
            btnPredict = findViewById(R.id.btnPredict);
            tvPrediction = findViewById(R.id.tvPrediction);

            // Set up spinners
            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                    R.array.expense_categories, android.R.layout.simple_spinner_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(categoryAdapter);

            ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                    R.array.days_of_week, android.R.layout.simple_spinner_item);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDayOfWeek.setAdapter(dayAdapter);

            // Set current day as default
            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            spDayOfWeek.setSelection(currentDay);

            btnPredict.setOnClickListener(v -> predictExpense());
        } catch (Exception e) {
            showError("Setup failed: " + e.getMessage());
        }
    }

    private void predictExpense() {
        try {
            String category = spCategory.getSelectedItem().toString();
            int dayOfWeek = spDayOfWeek.getSelectedItemPosition();

            // Calculate average frequency (expenses per week in this category)
            List<Expense> expenses = db.getExpensesByCategory(category);
            float frequency = expenses.size() / 4.0f;

            // Calculate average past expense in this category
            float pastExpense = 0;
            if (!expenses.isEmpty()) {
                for (Expense expense : expenses) {
                    pastExpense += expense.getAmount();
                }
                pastExpense /= expenses.size();
            } else {
                pastExpense = 50; // Default value
            }

            // Get prediction
            float prediction = predictor.predictExpense(dayOfWeek, category, frequency, pastExpense);

            tvPrediction.setText(String.format("Predicted expense: $%.2f", prediction));
        } catch (Exception e) {
            showError("Prediction failed: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (predictor != null) {
            predictor.close();
        }
    }
}