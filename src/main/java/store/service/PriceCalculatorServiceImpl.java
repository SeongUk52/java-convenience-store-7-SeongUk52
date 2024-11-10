package store.service;

import store.model.PriceDetails;
import store.model.Promotion;
import store.model.PromotionBenefit;
import store.model.PurchaseSummary;

public class PriceCalculatorServiceImpl implements PriceCalculatorService {
    @Override
    public PriceDetails calculatePrice(PurchaseSummary purchaseSummary, Promotion promotion, boolean isMembership) {
        int membershipDiscount = 0;
        int totalPrice = calculateTotalPrice(purchaseSummary);

        PromotionBenefit promotionBenefit = null;
        int promotionDiscount = 0;

        if (promotion != null) {
            promotionBenefit = promotion.getBenefit(purchaseSummary.promotionConsumption());
            promotionDiscount = calculatePromotionDiscount(purchaseSummary, promotionBenefit);
        }

        if (isMembership) {
            membershipDiscount = calculateMembershipDiscount(purchaseSummary, promotionBenefit);
        }

        int finalPrice = totalPrice - promotionDiscount - membershipDiscount;

        return new PriceDetails(
                totalPrice,
                promotionDiscount,
                membershipDiscount,
                finalPrice,
                (promotionBenefit != null ? promotionBenefit.eligibleItems() : 0),
                purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption()
        );
    }

    private int calculateTotalPrice(PurchaseSummary purchaseSummary) {
        int totalItems = purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption();
        return purchaseSummary.price() * totalItems;
    }

    private int calculatePromotionDiscount(PurchaseSummary purchaseSummary, PromotionBenefit promotionBenefit) {
        return promotionBenefit.eligibleItems() * purchaseSummary.price();
    }

    private int calculateMembershipDiscount(PurchaseSummary purchaseSummary, PromotionBenefit promotionBenefit) {
        int excludedItems = purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption()
                - promotionBenefit.totalItems();
        int discountAmount = (int) (purchaseSummary.price() * excludedItems * 0.3);
        return Math.min(discountAmount, 8_000);
    }
}
