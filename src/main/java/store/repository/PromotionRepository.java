package store.repository;

import store.model.Promotion;

public interface PromotionRepository {
    Promotion findByName(String name);
}
