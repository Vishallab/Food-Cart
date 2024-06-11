package com.vishal.foodbooking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzeria.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_CUSTOMER_NAME = "c_name";
    private static final String COLUMN_CUSTOMER_ADDRESS = "c_address";
    private static final String COLUMN_TOTAL_COST = "total_cost";
    private static final String COLUMN_TIME = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CUSTOMER_NAME + " TEXT,"
                + COLUMN_CUSTOMER_ADDRESS + " TEXT,"
                + COLUMN_TOTAL_COST + " REAL,"
                + COLUMN_TIME + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long insertOrder(String customerName, String customerAddress, double totalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME, customerName);
        values.put(COLUMN_CUSTOMER_ADDRESS, customerAddress);
        values.put(COLUMN_TOTAL_COST, totalCost);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        values.put(COLUMN_TIME, timeStamp);
        long orderId = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return orderId;
    }

    public ArrayList<String> getAllOrders() {
        ArrayList<String> orders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + COLUMN_ORDER_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                long orderId = cursor.getLong(0);
                String customerName = cursor.getString(1);
                String customerAddress = cursor.getString(2);
                double totalCost = cursor.getDouble(3);
                String time = cursor.getString(4);

                // Get discount percentage and max discount amount based on total cost
                double discountPercentage = 0.0;
                double maxDiscountAmount = 0.0;
                if (totalCost <= 200) {
                    discountPercentage = 10.0;
                    maxDiscountAmount = 50.0;
                } else if (totalCost <= 500) {
                    discountPercentage = 25.0;
                    maxDiscountAmount = 100.0;
                } else if (totalCost <= 800) {
                    discountPercentage = 40.0;
                    maxDiscountAmount = 200.0;
                } else if (totalCost <= 1200) {
                    discountPercentage = 60.0;
                    maxDiscountAmount = 400.0;
                } else if (totalCost <= 1700) {
                    discountPercentage = 80.0;
                    maxDiscountAmount = 650.0;
                } else if (totalCost > 1700) {
                    discountPercentage = 90.0;
                    maxDiscountAmount = 800.0;
                }

                // Calculate discount amount
                double discountAmount = (totalCost * discountPercentage) / 100;
                if (discountAmount > maxDiscountAmount) {
                    discountAmount = maxDiscountAmount;
                }

                // Calculate the final total after discount
                double finalTotal = totalCost - discountAmount;

                String orderDetails = "\nOrder #000" + orderId + "\n" +
                        "\n" + customerName +
                        "\n" + customerAddress +
                        "\ntotalPrice " + totalCost +
                        "\nDiscount: -" + discountPercentage + "% (Upto Rs. " + maxDiscountAmount + ")" +
                        "\nPayable Amount: Rs. " + String.format("%.2f", finalTotal) + // Payable amount after discount
                        "\nDate/Time: " + time + "\n";
                orders.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

}