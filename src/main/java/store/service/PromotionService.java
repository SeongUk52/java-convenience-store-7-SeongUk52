package store.service;

import java.time.LocalDate;
import store.model.Product;
import store.model.Promotion;

public interface PromotionService {
    boolean isPromotionActiveForProduct(Product product, LocalDate currentDate);
    Promotion findPromotion(String input);
}
