package store.model;

import java.util.Objects;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public int purchase(int quantityToBuy) {
        int availableQuantity = Math.min(quantityToBuy, quantity);
        quantity -= availableQuantity;
        return quantityToBuy - availableQuantity;
    }

    public boolean isNameEqualTo(String name) {
        return this.name.equals(name);
    }

    @Override
    public String toString() {
        return name + "," + price + "," + quantity + "," + null;
    }

    public String getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return !Objects.equals(promotion, "null");
    }
}
