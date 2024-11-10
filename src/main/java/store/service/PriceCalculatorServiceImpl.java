package store.service;

import store.model.PriceDetails;
import store.model.Promotion;
import store.model.PurchaseSummary;

public class PriceCalculatorServiceImpl implements PriceCalculatorService {
    @Override
    public PriceDetails calculatePrice(PurchaseSummary purchaseSummary, Promotion promotion) {
        int totalPrice = calculateTotalPrice(purchaseSummary);
        int promotionDiscount = calculatePromotionDiscount(purchaseSummary, promotion);
        int membershipDiscount = 0;
        int finalPrice = totalPrice - promotionDiscount - membershipDiscount;
        return new PriceDetails(
                totalPrice,
                promotionDiscount,
                membershipDiscount,
                finalPrice
        );
    }

    private int calculateTotalPrice(PurchaseSummary purchaseSummary) {
        return purchaseSummary.price() *
                (purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption());
    }

    private int calculatePromotionDiscount(PurchaseSummary purchaseSummary, Promotion promotion) {
        return promotion.getBenefitCount(purchaseSummary.promotionConsumption()) * purchaseSummary.price();
    }

    private int calculateMembershipDiscount(PurchaseSummary purchaseSummary) {
        return 0;
    }

    private int calculateExcludingPromotionPrice(PurchaseSummary purchaseSummary, Promotion promotion) {
        int excludedItems = getExcludedItems(purchaseSummary, promotion);
        return purchaseSummary.price() * excludedItems;
    }

    private int getExcludedItems(PurchaseSummary purchaseSummary, Promotion promotion) {
        int totalItems = purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption();
        int discountedItems = promotion.getBenefitCount(purchaseSummary.promotionConsumption());
        return totalItems - discountedItems;
    }
}
