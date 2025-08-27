package com.example.financetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.financetracker.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finance_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_EXPENSES = "expenses";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DAY_OF_WEEK = "day_of_week";

    // Create table SQL
    private static final String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_AMOUNT + " REAL,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_DAY_OF_WEEK + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Add new expense
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DATE, expense.getDate());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_DAY_OF_WEEK, expense.getDayOfWeek());

        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get all expenses
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setAmount(cursor.getDouble(1));
                expense.setCategory(cursor.getString(2));
                expense.setDate(cursor.getString(3));
                expense.setDescription(cursor.getString(4));
                expense.setDayOfWeek(cursor.getInt(5));

                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // Get expenses by category
    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPENSES,
                new String[]{KEY_ID, KEY_AMOUNT, KEY_CATEGORY, KEY_DATE, KEY_DESCRIPTION, KEY_DAY_OF_WEEK},
                KEY_CATEGORY + "=?",
                new String[]{category}, null, null, KEY_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setAmount(cursor.getDouble(1));
                expense.setCategory(cursor.getString(2));
                expense.setDate(cursor.getString(3));
                expense.setDescription(cursor.getString(4));
                expense.setDayOfWeek(cursor.getInt(5));

                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // Get total expenses
    public double getTotalExpenses() {
        String selectQuery = "SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }


}