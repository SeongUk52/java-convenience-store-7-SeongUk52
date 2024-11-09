package store.service;

import java.util.List;
import store.model.Product;
import store.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final PromotionService promotionService;

    public ProductServiceImpl(ProductRepository productRepository, PromotionService promotionService) {
        this.productRepository = productRepository;
        this.promotionService = promotionService;
    }

    @Override
    public void purchaseProducts(String input) {

    }

    private void purchaseProduct(String name, int amount) {
        List<Product> products = productRepository.findByName(name);

    }
}
