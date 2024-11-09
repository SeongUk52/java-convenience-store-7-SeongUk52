package store.service;

import store.model.Product;
import store.model.Promotion;
import store.repository.PromotionRepository;

public class PromotionServiceImpl implements PromotionService {
    private PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public boolean isPromotionActiveForProduct(Product product) {
        Promotion promotion = promotionRepository.findByName(product.getPromotion());
        return promotion.isValid();
    }
}
