package com.vishal.foodbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CART_ACTIVITY = 1;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private FoodAdapter foodAdapter;

    private int foodCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.headerImageView);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.foodbook)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable caching for GIFs to ensure they animate properly
                        .skipMemoryCache(true))  // Skip memory cache
                .into(imageView);


        // Initialize Spinner
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

// Set Spinner listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterFoodList(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterFoodList("All");
            }
        });


        RecyclerView foodRecyclerView = findViewById(R.id.foodRecyclerView);
        foodList = createFoodList();
        filteredFoodList = new ArrayList<>(foodList);
        foodAdapter = new FoodAdapter(filteredFoodList, new FoodAdapter.FoodClickListener() {
            @Override
            public void onPlusClick(int position) {
                addFoodToCart(position);
            }

            @Override
            public void onMinusClick(int position) {
                removeFoodFromCart(position);
            }
        });

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodRecyclerView.setAdapter(foodAdapter);

        Button cartButton = findViewById(R.id.cartButton);
        Button historyButton = findViewById(R.id.historyButton);

        cartButton.setOnClickListener(v -> {
            if (foodCount > 0) {
                ArrayList<Food> selectedFoods = new ArrayList<>();
                for (Food food : foodList) {
                    if (food.getQuantity() > 0) {
                        selectedFoods.add(food);
                    }
                }
                if (selectedFoods.size() > 0) {
                    double total = calculateTotal(selectedFoods);
                    openCartActivity(selectedFoods, total);
                } else {
                    Toast.makeText(MainActivity.this, "No foods added to the cart", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "No foods added to the cart", Toast.LENGTH_SHORT).show();
            }
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private List<Food> createFoodList() {
        List<Food> foods = new ArrayList<>();

//        pizza

        foods.add(new Food("Veg Classix Pizza", "129", R.drawable.clasic, "Pizza"));
        foods.add(new Food("Double Cheese Margrita", "199", R.drawable.doublecheesemargirita, "Pizza"));
        foods.add(new Food("Golden Corn Pizza", "249", R.drawable.goldencorn, "Pizza"));
        foods.add(new Food("Margrita Pizza", "159", R.drawable.margirita, "Pizza"));
        foods.add(new Food("MAxican Green Pizza", "300", R.drawable.mexicangreenwave, "Pizza"));
        foods.add(new Food("Onion Capsicum Pizza", "230", R.drawable.onioncapsicum, "Pizza"));
        foods.add(new Food("Paneer Capsicum Pizza", "280", R.drawable.paneercpsicum, "Pizza"));
        foods.add(new Food("Peppy Paneer Pizza", "300", R.drawable.peppypaneer, "Pizza"));
        foods.add(new Food("Veg Loaded Pizza", "200", R.drawable.vegloaded, "Pizza"));


//        burger

        foods.add(new Food("Crispy Veg Burger", "109", R.drawable.crispyvegburger, "Burger"));
        foods.add(new Food("Double Crispy Veg Burger", "209", R.drawable.doublecrespyveg, "Burger"));
        foods.add(new Food("Hot & Crispy Burger", "179", R.drawable.hotnsicyburger, "Burger"));
        foods.add(new Food("Veg taco", "79", R.drawable.taco, "Burger"));
        foods.add(new Food("Veg Whopper Burger", "179", R.drawable.vegwhopper, "Burger"));


        //chinese
        foods.add(new Food("Chilly mushroom", "339", R.drawable.chilimashrom, "Chinese"));
        foods.add(new Food("Chilly chaap", "365", R.drawable.chillychap, "Chinese"));
        foods.add(new Food("Chilly potato", "315", R.drawable.chilypotato, "Chinese"));
        foods.add(new Food("French Fries", "299", R.drawable.fenchfries, "Chinese"));
        foods.add(new Food("Chilly Paneer Dry", "399", R.drawable.pneerdry, "Chinese"));
        foods.add(new Food("Honey Chilly Potato", "249", R.drawable.honeychill, "Chinese"));
        foods.add(new Food("Veg Manchuriya", "299", R.drawable.vegmanchuriyan, "Chinese"));
        foods.add(new Food("Veg Spring Roll", "130", R.drawable.vegspringrol, "Chinese"));
        foods.add(new Food("Veg Lolipop", "130", R.drawable.veglolipop, "Chinese"));




//chowmin
        foods.add(new Food("Garlic Chowmin", "130", R.drawable.garliccoumon, "Chowmin"));
        foods.add(new Food("Singapuri Chowmin", "800", R.drawable.singapuricown, "Chowmin"));



//pasta
        foods.add(new Food("Mix Sauce Pasta", "1300", R.drawable.mixpasta, "Pasta"));
        foods.add(new Food("Red Sauce Pasta", "800", R.drawable.redpasta, "Pasta"));
        foods.add(new Food("White Sauce Pasta", "800", R.drawable.whitepasta, "Pasta"));



        //MOMO
        foods.add(new Food("Fried Momo", "140", R.drawable.friedmomo, "Momo"));
        foods.add(new Food("Gravey Momo", "190", R.drawable.graveymomo, "Momo"));
        foods.add(new Food("Kurkure Momo", "240", R.drawable.kurkuremomo, "Momo"));
        foods.add(new Food("Paneer Momo", "180", R.drawable.paneermomo, "Momo"));
        foods.add(new Food("Peri Peri Momo", "200", R.drawable.periperimomo, "Momo"));
        foods.add(new Food("steam Momo", "120", R.drawable.steammomo, "Momo"));



        //Drinks
        foods.add(new Food("Vanilla Shake", "189", R.drawable.vanila, "Drinks"));
        foods.add(new Food("Butterscotch Shake", "200", R.drawable.butter, "Drinks"));
        foods.add(new Food("Strawberry Shake", "199", R.drawable.strawberry, "Drinks"));
        foods.add(new Food("Kitkat Shake", "250", R.drawable.kitkat, "Drinks"));



//        Meal Bowl For 1
        foods.add(new Food("Rajma Meal Box", "249", R.drawable.rajma, "Meal Bowl"));
        foods.add(new Food("Paneer Butter Masala Meal Box", "189", R.drawable.butterpanner, "Meal Bowl"));
        foods.add(new Food("Chilly Paneer Meal Bowl", "189", R.drawable.chillipaneer, "Meal Bowl"));
        foods.add(new Food("Manchurian Meal Bowl", "189", R.drawable.manchuriuanmeal, "Meal Bowl"));
        foods.add(new Food("Dal Makhni Meal Box", "189", R.drawable.dalmakhni, "Meal Bowl"));
        foods.add(new Food("Shahi Paneer Meal Box", "189", R.drawable.shahipaneer, "Meal Bowl"));

        return foods;
    }

    private void filterFoodList(String category) {
        if (category.equals("All")) {
            filteredFoodList.clear();
            filteredFoodList.addAll(foodList);
        } else {
            filteredFoodList.clear();
            for (Food food : foodList) {
                if (food.getCategory().equals(category)) {
                    filteredFoodList.add(food);
                }
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    private void addFoodToCart(int position) {
        Food food = filteredFoodList.get(position);
        int quantity = food.getQuantity();
        if (quantity < 10) {
            foodCount++;
            quantity++;
            food.setQuantity(quantity);
            foodAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Maximum food limit reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFoodFromCart(int position) {
        Food food = filteredFoodList.get(position);
        int quantity = food.getQuantity();
        if (quantity > 0) {
            foodCount--;
            quantity--;
            food.setQuantity(quantity);
            foodAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Why?", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateTotal(List<Food> foods) {
        double total = 0;
        for (Food food : foods) {
            int quantity = food.getQuantity();
            double price = Double.parseDouble(food.getPrice().replaceAll("[^\\d.]+", ""));
            total += (quantity * price);
        }
        return total;
    }

    private void openCartActivity(List<Food> selectedFoods, double total) {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        intent.putExtra("selectedFoods", new ArrayList<>(selectedFoods));
        intent.putExtra("total", total);
        startActivityForResult(intent, REQUEST_CART_ACTIVITY);
    }
}
