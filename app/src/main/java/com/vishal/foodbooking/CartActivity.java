package com.vishal.foodbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Window window = CartActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        TextView selectedFoodsTextView = findViewById(R.id.selectedFoodsTextView);
        TextView totalTextView = findViewById(R.id.totalTextView);
        Button confirmOrderButton = findViewById(R.id.confirmOrderButton);

        // Retrieve selected Foods and total from the intent
        Intent intent = getIntent();
        ArrayList<Food> selectedFoods = intent.getParcelableArrayListExtra("selectedFoods");
        double total = intent.getDoubleExtra("total", 0.0);

        dbHelper = new DBHelper(this);

        StringBuilder FoodsText = new StringBuilder();
        long count = 0;
        for (Food food : selectedFoods) {
            FoodsText.append(++count).append(". ").append(food.getName()).append(" (").append(food.getQuantity()).append(")\n");
        }

        String deliveryText;
        double deliveryFee = 0.0;
        double discountPercentage = 0.0;
        double maxDiscountAmount = 0.0;

// Check the total price and determine the discount percentage and maximum discount amount
        if (total <= 200) {
            discountPercentage = 10.0;
            maxDiscountAmount = 50.0;
        } else if (total <= 500) {
            discountPercentage = 25.0;
            maxDiscountAmount = 100.0;
        } else if (total <= 800) {
            discountPercentage = 40.0;
            maxDiscountAmount = 200.0;
        } else if (total <= 1200) {
            discountPercentage = 60.0;
            maxDiscountAmount = 400.0;
        } else if (total <= 1700) {
            discountPercentage = 80.0;
            maxDiscountAmount = 650.0;
        }else if (total > 1700 ) {
            discountPercentage = 90.0;
            maxDiscountAmount = 800.0;
        }

// Calculate discount amount
        double discountAmount = (total * discountPercentage) / 100;
        if (discountAmount > maxDiscountAmount) {
            discountAmount = maxDiscountAmount;
        }

// Calculate the final total after discount and including the delivery fee
        double finalTotal = total - discountAmount;

// Set the delivery fee based on the total
        if (finalTotal > 700) {
            deliveryText = "You got Free Delivery";
            deliveryFee = 0.0;
        } else {
            deliveryText = "Delivery Fee: Rs. 120";
            deliveryFee = 120.0;
        }

// Set the text for selected foods, total price, delivery fee, discount, and final total
        selectedFoodsTextView.setText(FoodsText.toString());
        totalTextView.setText(String.format("Items Price: Rs. %.2f", total));
        totalTextView.append("\n" + deliveryText);
        totalTextView.append("\nDiscount: -" + discountPercentage + "% (Upto Rs. " + maxDiscountAmount + ")");
        totalTextView.append("\nTotal Bill Amount: Rs. " + String.format("%.2f", finalTotal + deliveryFee));

        confirmOrderButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String address = addressEditText.getText().toString();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(CartActivity.this, "Please enter your name and address", Toast.LENGTH_SHORT).show();
            } else {
                long orderId = dbHelper.insertOrder(name, address, total);
                if (orderId != -1) {
                    Toast.makeText(CartActivity.this, "Order confirmed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to insert order", Toast.LENGTH_SHORT).show();
                }

                for (Food food : selectedFoods) {
                    food.setQuantity(0);
                }

                Intent intent1 = new Intent(CartActivity.this, MainActivity.class);
                intent1.putParcelableArrayListExtra("selectedFoods", selectedFoods);
                setResult(RESULT_OK, intent1);

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
