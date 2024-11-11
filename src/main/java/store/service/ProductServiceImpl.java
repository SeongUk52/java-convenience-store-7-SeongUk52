package store.service;

import static store.constants.ErrorMessage.EXCEEDS_STOCK;
import static store.constants.ErrorMessage.NON_EXISTENT_PRODUCT;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    public Map<String, PriceDetails> purchaseProducts(Map<String, Integer> products, boolean isMembership) {
        return products.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> purchaseProduct(entry.getKey(), entry.getValue(), isMembership)
                ));
    }

    @Override
    public List<String> getFormattedProductList() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.toList()))
                .entrySet()
                .stream()
                .flatMap(entry -> formatProductGroup(entry.getValue()).stream())
                .toList();
    }

    @Override
    public void saveAll(String filePath) throws IOException {
        productRepository.saveToFile(filePath);
    }

    @Override
    public int getAvailablePromotionCount(String productName, int currentCount) {
        validateSufficientTotalStock(productName, currentCount);
        return productRepository.findByName(productName)
                .stream()
                .filter(Product::hasPromotion)
                .findFirst()
                .map(product -> calculatePromotionCount(product, currentCount))
                .orElse(0);
    }

    @Override
    public int getInsufficientPromotionCount(String productName, int currentCount) {
        validateSufficientTotalStock(productName, currentCount);
        if (noPromotionAvailableForProduct(productName)) {
            return 0;
        }
        return calculateInsufficientPromotionCount(productName, currentCount);
    }

    private boolean noPromotionAvailableForProduct(String productName) {
        return productRepository.findByName(productName)
                .stream()
                .noneMatch(Product::hasPromotion);
    }

    private int calculateInsufficientPromotionCount(String productName, int currentCount) {
        int sufficientPromotionCount = productRepository.findByName(productName)
                .stream()
                .filter(Product::hasPromotion)
                .findFirst()
                .map(product -> calculateSufficientPromotionCount(product, currentCount))
                .orElse(0);
        return currentCount - sufficientPromotionCount;
    }

    private int calculatePromotionCount(Product product, int currentCount) {
        Promotion promotion = promotionService.findPromotion(product.getPromotion());
        if (promotion == null || !isPromotionValid(promotion)) {
            return 0;
        }

        return getPromotionCountIfEligible(product, currentCount, promotion);
    }

    private int getPromotionCountIfEligible(Product product, int currentCount, Promotion promotion) {
        if (currentCount >= promotion.getThreshold() && hasSufficientStock(product, currentCount, promotion)) {
            return promotion.getGet();
        }
        return 0;
    }

    private boolean isPromotionValid(Promotion promotion) {
        return promotion.isValid(DateTimes.now().toLocalDate());
    }

    private boolean hasSufficientStock(Product product, int currentCount, Promotion promotion) {
        return product.getQuantity() >= currentCount + promotion.getGet();
    }

    private int calculateSufficientPromotionCount(Product product, int currentCount) {
        Promotion promotion = promotionService.findPromotion(product.getPromotion());
        if (product.getQuantity() >= currentCount) {
            return currentCount;
        }
        return product.getQuantity() - (product.getQuantity() % promotion.getThreshold());
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

    private PriceDetails purchaseProduct(String name, int amount, boolean isMembership) {
        Map<Boolean, List<Product>> partitionedProducts = partitionProductsByPromotion(name);
        int promotionConsumption = calculatePromotionConsumption(partitionedProducts.get(true), amount);
        int regularConsumption = calculateRegularConsumption(partitionedProducts.get(false),
                amount - promotionConsumption);
        int price = getPriceFromName(name);
        PurchaseSummary purchaseSummary = new PurchaseSummary(promotionConsumption, regularConsumption, price);
        Promotion promotion = getPromotionFromProducts(partitionedProducts);
        return priceCalculatorService.calculatePrice(purchaseSummary, promotion, isMembership);
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
                .orElseThrow(() -> new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage()));
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

    private void validateSufficientTotalStock(String productName, int amount) {
        int totalStock = calculateTotalStockForProduct(productName);
        if (totalStock == 0) {
            throw new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage());
        }
        if (amount > totalStock) {
            throw new IllegalArgumentException(EXCEEDS_STOCK.getMessage());
        }
    }

    private int calculateTotalStockForProduct(String productName) {
        return productRepository.findByName(productName)
                .stream()
                .map(Product::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Promotion getPromotionFromProducts(Map<Boolean, List<Product>> partitionedProducts) {
        if (partitionedProducts.get(true).isEmpty()) {
            return null;
        } else {
            return promotionService.findPromotion(partitionedProducts.get(true).get(0).getPromotion());
        }
    }
}