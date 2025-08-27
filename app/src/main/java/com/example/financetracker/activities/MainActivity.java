package com.example.financetracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.financetracker.R;
import com.example.financetracker.database.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalExpenses;
    private PieChart pieChart;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Initialize database
            db = new DatabaseHelper(this);

            // Initialize views
            tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
            pieChart = findViewById(R.id.pieChart);

            // Setup UI components
            setupPieChart();
            setupButtons();
            updateUI();
        } catch (Exception e) {
            showError("Initialization failed: " + e.getMessage());
        }
    }

    private void setupPieChart() {
        try {
            pieChart.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.setHoleColor(ContextCompat.getColor(this, R.color.black));
            pieChart.setTransparentCircleColor(ContextCompat.getColor(this, R.color.dark_gray));
            pieChart.getLegend().setTextColor(Color.WHITE);

            // Sample data (replace with your actual data)
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(40f, "Food"));
            entries.add(new PieEntry(30f, "Transport"));
            entries.add(new PieEntry(20f, "Entertainment"));
            entries.add(new PieEntry(10f, "Bills"));

            PieDataSet dataSet = new PieDataSet(entries, "Expenses");
            dataSet.setColors(new int[]{
                    Color.parseColor("#FF00FF00"), // Lime Green
                    Color.parseColor("#FF66FF66"), // Light Green
                    Color.parseColor("#FF009900"), // Dark Green
                    Color.parseColor("#FF33FF33")  // Medium Green
            });
            dataSet.setValueTextColor(Color.BLACK);

            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
        } catch (Exception e) {
            showError("Chart setup failed: " + e.getMessage());
        }
    }

    private void setupButtons() {
        try {
            findViewById(R.id.btnAddExpense).setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, AddExpenseActivity.class));
                } catch (Exception e) {
                    showError("Couldn't open Add Expense: " + e.getMessage());
                }
            });

            findViewById(R.id.btnViewExpenses).setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, ExpenseListActivity.class));
                } catch (Exception e) {
                    showError("Couldn't open Expense List: " + e.getMessage());
                }
            });

            findViewById(R.id.btnPredictExpense).setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, PredictionActivity.class));
                } catch (Exception e) {
                    showError("Couldn't open Prediction: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showError("Button setup failed: " + e.getMessage());
        }
    }

    private void updateUI() {
        try {
            double total = db.getTotalExpenses();
            tvTotalExpenses.setText(String.format("$%.2f", total));
        } catch (Exception e) {
            showError("Couldn't update expenses: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}