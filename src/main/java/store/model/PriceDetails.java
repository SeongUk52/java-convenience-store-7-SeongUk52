package store.model;

public record PriceDetails(
        int totalPrice,
        int promotionDiscount,
        int membershipDiscount,
        int finalPrice,
        int promotionCount,
        int totalCount
) {}
