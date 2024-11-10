package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import store.model.PriceDetails;
import store.model.Product;
import store.model.Promotion;
import store.model.PurchaseSummary;
import store.repository.FileProductRepository;

public class ProductServiceImpl implements ProductService {
    private final FileProductRepository productRepository;
    private final PromotionService promotionService;
    private final PriceCalculatorService priceCalculatorService;

    public ProductServiceImpl(FileProductRepository productRepository, PromotionService promotionService,
                              PriceCalculatorService priceCalculatorService) {
        this.productRepository = productRepository;
        this.promotionService = promotionService;
        this.priceCalculatorService = priceCalculatorService;
    }

    @Override
    public void purchaseProducts(Map<String, Integer> products) {
        products.forEach(this::purchaseProduct);
    }

    @Override
    public List<String> getFormattedProductList() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .flatMap(entry -> formatProductGroup(entry.getValue()).stream())
                .toList();
    }

    @Override
    public void saveAll(String filePath) throws IOException, URISyntaxException {
        productRepository.saveToFile(filePath);
    }

    @Override
    public int getAvailablePromotionCount(String productName, int currentCount) {
        return productRepository.findByName(productName)
                .stream()
                .filter(Product::hasPromotion)
                .findFirst()
                .map(product -> calculatePromotionCount(product, currentCount))
                .orElse(0);
    }

    private int calculatePromotionCount(Product product, int currentCount) {
        Promotion promotion = promotionService.findPromotion(product.getPromotion());
        if (!isPromotionValid(promotion)) {
            return 0;
        }
        int threshold = promotion.getThreshold();
        if (isEligibleForPromotion(currentCount, threshold, promotion) &&
                hasSufficientStock(product, currentCount, promotion)) {
            return promotion.getGet();
        }
        return 0;
    }

    private boolean isPromotionValid(Promotion promotion) {
        return promotion.isValid(DateTimes.now().toLocalDate());
    }

    private boolean isEligibleForPromotion(int currentCount, int threshold, Promotion promotion) {
        return currentCount % threshold == promotion.getBuy();
    }

    private boolean hasSufficientStock(Product product, int currentCount, Promotion promotion) {
        return product.getQuantity() >= currentCount + promotion.getGet();
    }

    private int calculateRequiredCount(int currentCount, int threshold) {
        return currentCount - (currentCount % threshold) + threshold;
    }

    private boolean isStockInsufficient(Product product, int requiredCount) {
        return product.getQuantity() < requiredCount;
    }

    private List<String> formatProductGroup(List<Product> products) {
        List<String> formattedProducts = products.stream()
                .map(Product::toFormattedString)
                .toList();

        if (hasNoRegularStock(products)) {
            formattedProducts = Stream.concat(
                    formattedProducts.stream(), Stream.of(products.get(0).toOutOfStockString()))
                    .toList();
        }
        return formattedProducts;
    }

    private boolean hasNoRegularStock(List<Product> products) {
        return products.stream().noneMatch(product -> !product.hasPromotion() && product.getQuantity() > 0);
    }

    private void purchaseProduct(String name, int amount) {
        Map<Boolean, List<Product>> partitionedProducts = partitionProductsByPromotion(name);
        int promotionConsumption = calculatePromotionConsumption(partitionedProducts.get(true), amount);
        int regularConsumption = calculateRegularConsumption(
                partitionedProducts.get(false), amount - promotionConsumption);
        int price = getPriceFromName(name);
        PurchaseSummary purchaseSummary = new PurchaseSummary(promotionConsumption, regularConsumption, price);
        Promotion promotion = promotionService.findPromotion(partitionedProducts.get(true).get(0).getPromotion());
        PriceDetails priceDetails = priceCalculatorService.calculatePrice(purchaseSummary, promotion);
    }

    private Map<Boolean, List<Product>> partitionProductsByPromotion(String name) {
        return productRepository.findByName(name).stream()
                .collect(Collectors.partitioningBy(Product::hasPromotion));
    }

    private int getPriceFromName(String name) {
        return productRepository.findByName(name)
                .stream()
                .findFirst()
                .map(Product::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. 다시 입력해 주세요."));
    }

    private int calculatePromotionConsumption(List<Product> promotionProducts, int amount) {
        return promotionProducts.stream()
                .filter(product -> promotionService.isPromotionActiveForProduct(product,
                        LocalDate.from(DateTimes.now())))
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
