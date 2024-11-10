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
        return availableQuantity;
    }

    public boolean isNameEqualTo(String name) {
        return this.name.equals(name);
    }

    @Override
    public String toString() {
        return name + "," + price + "," + quantity + "," + promotion;
    }

    public boolean hasPromotion() {
        return !Objects.equals(promotion, "null");
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getPromotion() {
        return promotion;
    }

    public String toFormattedString() {
        String formattedPrice = String.format("%,d", price);
        String formattedQuantity = String.format("%,d개", quantity);
        if (quantity == 0) {
            formattedQuantity = "재고 없음";
        }

        if (Objects.equals(promotion, "null") || promotion.isEmpty()) {
            return String.format("- %s %s원 %s", name, formattedPrice, formattedQuantity);
        }
        return String.format("- %s %s원 %s %s", name, formattedPrice, formattedQuantity, promotion);
    }

    public String toOutOfStockString() {
        return String.format("- %s %,d원 재고 없음", name, price);
    }
}
