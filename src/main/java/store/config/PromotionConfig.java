package store.config;

import store.infrastructure.PromotionMarkdownDataParser;
import store.model.Promotion;
import store.repository.FilePromotionRepository;
import store.repository.PromotionRepository;
import store.infrastructure.DataParser;

public class PromotionConfig {
    private static final PromotionConfig instance = new PromotionConfig();
    private final PromotionRepository promotionRepository;

    private PromotionConfig() {
        try {
            DataParser<Promotion> promotionCsvDataParser = new PromotionMarkdownDataParser();
            promotionRepository = new FilePromotionRepository("promotions.md", promotionCsvDataParser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PromotionConfig getInstance() {
        return instance;
    }

    public PromotionRepository getPromotionRepository() {
        return promotionRepository;
    }
}