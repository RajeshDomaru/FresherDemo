package com.example.fresher.sqlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fresher.core.ExpensesModel;

import java.util.ArrayList;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String dbName = "expenses_db";

    private static final int dbVersion = 1;

    private static final String expenses_tbl = "expenses_tbl";

    private static final String expenseId = "expenseId";
    private static final String title = "title";
    private static final String date = "date";
    private static final String amount = "amount";
    private static final String paidVia = "paidVia";

    public MySQLiteOpenHelper(@Nullable Context context) {

        super(context, dbName, null, dbVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        String hardCodeQuery = "CREATE TABLE expenses_tbl(expenseId INTEGER PRIMARY KEY, title TEXT, date TEXT, amount REAL, paidVia TEXT)";

        String query = "CREATE TABLE " + expenses_tbl + "(" + expenseId + " INTEGER PRIMARY KEY, " + title + " TEXT, " + date + " TEXT, " + amount + " REAL, " + paidVia + " TEXT)";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + expenses_tbl);

        onCreate(db);

    }

    public long insertExpenses(ExpensesModel expensesModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = getExpensesModel(expensesModel);

        long insertedId = db.insert(expenses_tbl, null, contentValues);

        db.close();

        return insertedId ;

    }

    public boolean updateExpenses(ExpensesModel expensesModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = getExpensesModel(expensesModel);

        int updatedId = db.update(expenses_tbl, contentValues, expenseId + " = ?", new String[]{String.valueOf(expensesModel.getExpenseId())});

        db.close();

        return updatedId != 0;

    }

    private ContentValues getExpensesModel(ExpensesModel expensesModel) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(title, expensesModel.getTitle());

        contentValues.put(date, expensesModel.getDate());

        contentValues.put(amount, expensesModel.getAmount());

        contentValues.put(paidVia, expensesModel.getPaidVia());

        return contentValues;

    }

    public ArrayList<ExpensesModel> getAllExpenses() {

        ArrayList<ExpensesModel> expensesModelArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + expenses_tbl;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                int cursorExpenseId = cursor.getColumnIndex(expenseId); // 0
                int cursorTitle = cursor.getColumnIndex(title); // 1
                int cursorDate = cursor.getColumnIndex(date);   // 2
                int cursorAmount = cursor.getColumnIndex(amount); // 3
                int cursorPaidVia = cursor.getColumnIndex(paidVia); // 4

                ExpensesModel expensesModel = new ExpensesModel();

                expensesModel.setExpenseId(cursor.getInt(cursorExpenseId));
                expensesModel.setTitle(cursor.getString(cursorTitle));
                expensesModel.setDate(cursor.getString(cursorDate));
                expensesModel.setAmount(cursor.getFloat(cursorAmount));
                expensesModel.setPaidVia(cursor.getString(cursorPaidVia));

                expensesModelArrayList.add(expensesModel);

            } while (cursor.moveToNext());

        }

        db.close();

        return expensesModelArrayList;

    }

    public boolean deleteExpenses(int expenseId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int deleteCount = db.delete(expenses_tbl, MySQLiteOpenHelper.expenseId + " = ?", new String[]{String.valueOf(expenseId)});

        db.close();

        return deleteCount != 0;

    }

}