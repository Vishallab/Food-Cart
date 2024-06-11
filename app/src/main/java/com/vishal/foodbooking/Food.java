package com.vishal.foodbooking;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private final String name;
    private final String price;
    private final int imageResId;
    private final String category;
    private int quantity;

    public Food(String name, String price, int imageResId, String category) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
        this.quantity = 0;
    }

    protected Food(Parcel in) {
        name = in.readString();
        price = in.readString();
        imageResId = in.readInt();
        category = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(imageResId);
        dest.writeString(category);
        dest.writeInt(quantity);
    }
}
