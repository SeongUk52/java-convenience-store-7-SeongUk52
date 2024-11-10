package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
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

    @Override
    public List<String> getFormattedProductList() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .flatMap(entry -> formatProductGroup(entry.getValue()).stream())
                .toList();
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

    private PurchaseSummary purchaseProduct(String name, int amount) {
        System.out.println(name + amount);
        Map<Boolean, List<Product>> partitionedProducts = partitionProductsByPromotion(name);
        int promotionConsumption = calculatePromotionConsumption(partitionedProducts.get(true), amount);
        int regularConsumption = calculateRegularConsumption(
                partitionedProducts.get(false), amount - promotionConsumption);
        int price = getPriceFromName(name);

        return new PurchaseSummary(promotionConsumption, regularConsumption, price);
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
