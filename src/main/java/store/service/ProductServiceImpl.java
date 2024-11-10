package store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.model.Product;
import store.model.PurchaseSummary;
import store.repository.ProductRepository;
import store.util.ProductParser;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final PromotionService promotionService;

    public ProductServiceImpl(ProductRepository productRepository, PromotionService promotionService) {
        this.productRepository = productRepository;
        this.promotionService = promotionService;
    }

    @Override
    public void purchaseProducts(String input) {
        Map<String, Integer> products = ProductParser.parse(input);
        products.forEach(this::purchaseProduct);
    }

    private PurchaseSummary purchaseProduct(String name, int amount) {
        Map<Boolean, List<Product>> partitionedProducts = partitionProductsByPromotion(name);
        int promotionConsumption = calculatePromotionConsumption(partitionedProducts.get(true), amount);
        int regularConsumption = calculateRegularConsumption(
                partitionedProducts.get(false), amount - promotionConsumption);

        return new PurchaseSummary(promotionConsumption, regularConsumption);
    }

    private Map<Boolean, List<Product>> partitionProductsByPromotion(String name) {
        return productRepository.findByName(name).stream()
                .collect(Collectors.partitioningBy(Product::hasPromotion));
    }

    private int calculatePromotionConsumption(List<Product> promotionProducts, int amount) {
        return promotionProducts.stream()
                .filter(product -> promotionService.isPromotionActiveForProduct(product, LocalDate.now()))
                .findFirst()
                .map(product -> product.purchase(amount))
                .orElse(0);
    }

    private int calculateRegularConsumption(List<Product> regularProducts, int remainingAmount) {
        return regularProducts.stream()
                .findFirst()
                .map(product -> product.purchase(remainingAmount))
                .orElse(0);
    }
}
