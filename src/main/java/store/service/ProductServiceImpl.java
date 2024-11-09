package store.service;

import java.time.LocalDate;
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
        int buyQuantity = amount;
        /*
        TODO:프로모션 적용 상품 결제
        products.stream()
                .filter(Product::hasPromotion)
                .findFirst()
                .filter(product -> promotionService.isPromotionActiveForProduct(product, LocalDate.now()))
                .ifPresent(product -> {
                    buyQuantity = product.purchase(buyQuantity)
                });

         */
        products.stream()
                .filter(product -> !product.hasPromotion())
                .findFirst()
                .ifPresent(product -> product.purchase(buyQuantity));
    }
}
