package store.service;

import java.time.LocalDate;
import store.model.Product;

public interface PromotionService {
    boolean isPromotionActiveForProduct(Product product, LocalDate currentDate);
}
