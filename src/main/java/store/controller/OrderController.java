package store.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import store.service.ProductService;
import store.util.ProductParser;
import store.view.InputView;
import store.view.OutputView;

public class OrderController {
    private final ProductService productService;
    private final InputView inputView;
    private final OutputView outputView;

    public OrderController(ProductService productService, InputView inputView, OutputView outputView) {
        this.productService = productService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        displayProductList();
        Map<String, Integer> products = ProductParser.parse(inputView.requestPurchaseInput());

        products.forEach((productName, amount) -> {
            int updatedAmount = handlePromotion(productName, amount);
            products.put(productName, updatedAmount);
        });

        productService.purchaseProducts(products, inputView.isMembership());

        try {
            productService.saveAll("src/main/resources/products.md");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayProductList() {
        outputView.printFormattedProductList(productService.getFormattedProductList());
    }

    private int handlePromotion(String productName, int amount) {
        int availablePromotionCount = productService.getAvailablePromotionCount(productName, amount);

        if (availablePromotionCount > 0) {
            boolean addItem = inputView.askForFreeItem(productName, availablePromotionCount);
            if (addItem) {
                return amount + availablePromotionCount;
            }
        }
        int insufficientPromotionCount = productService.getInsufficientPromotionCount(productName, amount);
        if (insufficientPromotionCount > 0) {
            boolean applyRegularPrice = inputView.askForRegularPrice(productName, insufficientPromotionCount);
            if (!applyRegularPrice) {
                return amount - insufficientPromotionCount;
            }
        }
        return amount;
    }
}
