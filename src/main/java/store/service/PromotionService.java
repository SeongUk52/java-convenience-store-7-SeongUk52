package store.service;

import store.model.Product;

public interface PromotionService {
    boolean isPromotionActiveForProduct(Product product);
}
