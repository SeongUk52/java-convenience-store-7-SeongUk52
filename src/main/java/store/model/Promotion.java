package store.model;

import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buy, int get, String startDate, String endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public boolean isNameEqualTo(String name) {
        return this.name.equals(name);
    }

    public boolean isValid(LocalDate currentDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    public PromotionBenefit getBenefit(int promotionConsumption) {
        int cycles = promotionConsumption / (buy + get);
        return new PromotionBenefit(cycles * (buy + get),cycles * get);
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public int getThreshold() {
        return get + buy;
    }
}
