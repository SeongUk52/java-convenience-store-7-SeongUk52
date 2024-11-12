package store.service;

import store.model.PriceDetails;
import store.model.Promotion;
import store.model.PromotionBenefit;
import store.model.PurchaseSummary;

public class PriceCalculatorServiceImpl implements PriceCalculatorService {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8_000;

    @Override
    public PriceDetails calculatePrice(PurchaseSummary purchaseSummary, Promotion promotion, boolean isMembership) {
        int totalPrice = calculateTotalPrice(purchaseSummary);
        int promotionDiscount = calculatePromotionDiscount(purchaseSummary, promotion);
        int membershipDiscount = calculateMembershipDiscount(purchaseSummary, promotion, isMembership);
        int finalPrice = totalPrice - promotionDiscount - membershipDiscount;
        return new PriceDetails(
                totalPrice, promotionDiscount, membershipDiscount, finalPrice,
                calculateEligibleItems(promotion, purchaseSummary), calculateTotalItems(purchaseSummary)
        );
    }

    private int calculateTotalPrice(PurchaseSummary purchaseSummary) {
        int totalItems = calculateTotalItems(purchaseSummary);
        return purchaseSummary.price() * totalItems;
    }

    private int calculateTotalItems(PurchaseSummary purchaseSummary) {
        return purchaseSummary.regularConsumption() + purchaseSummary.promotionConsumption();
    }

    private int calculatePromotionDiscount(PurchaseSummary purchaseSummary, Promotion promotion) {
        if (promotion == null) {
            return 0;
        }
        PromotionBenefit promotionBenefit = promotion.getBenefit(purchaseSummary.promotionConsumption());
        return promotionBenefit.eligibleItems() * purchaseSummary.price();
    }

    private int calculateMembershipDiscount(PurchaseSummary purchaseSummary, Promotion promotion, boolean isMembership) {
        if (!isMembership) {
            return 0;
        }
        int excludedItems = calculateExcludedItems(purchaseSummary, promotion);
        int discountAmount = (int) (purchaseSummary.price() * excludedItems * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discountAmount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private int calculateExcludedItems(PurchaseSummary purchaseSummary, Promotion promotion) {
        if (promotion == null) {
            return calculateTotalItems(purchaseSummary);
        }
        PromotionBenefit promotionBenefit = promotion.getBenefit(purchaseSummary.promotionConsumption());
        return calculateTotalItems(purchaseSummary) - promotionBenefit.totalItems();
    }

    private int calculateEligibleItems(Promotion promotion, PurchaseSummary purchaseSummary) {
        if (promotion == null) {
            return 0;
        }
        PromotionBenefit promotionBenefit = promotion.getBenefit(purchaseSummary.promotionConsumption());
        return promotionBenefit.eligibleItems();
    }
}