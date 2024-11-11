package store.model;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

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
        int totalItems = buy + get;
        int cycles = promotionConsumption / totalItems;
        return new PromotionBenefit(cycles * totalItems, cycles * get);
    }

    public String getName() {
        return name;
    }

    public int getGet() {
        return get;
    }

    public int getThreshold() {
        return get + buy;
    }
}
