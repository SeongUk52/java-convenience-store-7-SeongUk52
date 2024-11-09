package store.model;

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

    public boolean isNameEqualTo(String name) {
        return this.name.equals(name);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String promotionString = "null";
        if (promotion != null) {
            promotionString = promotion;
        }
        return name + "," + price + "," + quantity + "," + promotionString;
    }
}
