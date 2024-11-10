package store.service;

import store.model.PriceDetails;
import store.model.Promotion;
import store.model.PurchaseSummary;

public interface PriceCalculatorService {
    PriceDetails calculatePrice(PurchaseSummary purchaseSummary, Promotion promotion);
}
