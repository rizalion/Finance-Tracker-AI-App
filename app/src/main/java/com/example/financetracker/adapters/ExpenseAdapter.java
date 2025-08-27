package com.example.financetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.financetracker.R;
import com.example.financetracker.models.Expense;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private Context context;
    private List<Expense> expenses;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, R.layout.expense_item, expenses);
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.expense_item, parent, false);

        TextView tvAmount = rowView.findViewById(R.id.tvAmount);
        TextView tvCategory = rowView.findViewById(R.id.tvCategory);
        TextView tvDate = rowView.findViewById(R.id.tvDate);
        TextView tvDescription = rowView.findViewById(R.id.tvDescription);

        Expense expense = expenses.get(position);
        tvAmount.setText(String.format("$%.2f", expense.getAmount()));
        tvCategory.setText(expense.getCategory());
        tvDate.setText(expense.getDate());
        tvDescription.setText(expense.getDescription());

        return rowView;
    }
}