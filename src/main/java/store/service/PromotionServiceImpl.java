package store.service;

import java.time.LocalDate;
import store.model.Product;
import store.model.Promotion;
import store.repository.PromotionRepository;

public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public boolean isPromotionActiveForProduct(Product product, LocalDate currentDate) {
        Promotion promotion = promotionRepository.findByName(product.getPromotion());
        return (promotion != null && promotion.isValid(currentDate));
    }

    @Override
    public Promotion findPromotion(String input) {
        return promotionRepository.findByName(input);
    }
}
