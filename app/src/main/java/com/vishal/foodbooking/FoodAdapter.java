package com.vishal.foodbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final List<Food> foodList;
    private final FoodClickListener foodClickListener;

    public interface FoodClickListener {
        void onPlusClick(int position);
        void onMinusClick(int position);
    }

    public FoodAdapter(List<Food> foodList, FoodClickListener foodClickListener) {
        this.foodList = foodList;
        this.foodClickListener = foodClickListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.foodImageView.setImageResource(food.getImageResId());
        holder.nameTextView.setText(food.getName());
        holder.priceTextView.setText(food.getPrice());
        holder.quantityTextView.setText(String.valueOf(food.getQuantity()));

        holder.plusImageView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                foodClickListener.onPlusClick(adapterPosition);
            }
        });

        holder.minusImageView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                foodClickListener.onMinusClick(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView plusImageView;
        ImageView minusImageView;
        LinearLayout mainLayout;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            plusImageView = itemView.findViewById(R.id.plusImageView);
            minusImageView = itemView.findViewById(R.id.minusImageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            // Animate RecyclerView
            Animation translate_anim = AnimationUtils.loadAnimation(mainLayout.getContext(), R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
