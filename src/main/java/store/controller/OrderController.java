package store.controller;

import static store.constants.ErrorMessage.INVALID_FORMAT;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.model.PriceDetails;
import store.service.ProductService;
import store.util.ProductParser;
import store.util.ProductValidator;
import store.util.ReceiptFormatterUtil;
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
        boolean isContinue = true;
        while (isContinue) {
            displayProductList();
            Map<String, Integer> products = getValidProducts();
            if (products == null) {
                continue;
            }
            processPurchase(products);
            isContinue = inputView.isContinue();
        }
        saveProducts();
    }

    private void processPurchase(Map<String, Integer> products) {
        try {
            Map<String, PriceDetails> productDetailsMap = productService.purchaseProducts(products, inputView.isMembership());
            List<String> receipt = ReceiptFormatterUtil.formatReceipt(productDetailsMap);
            outputView.printReceipt(receipt);
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
        }
    }

    private void saveProducts() {
        try {
            productService.saveAll("src/main/resources/products.md");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayProductList() {
        outputView.printFormattedProductList(productService.getFormattedProductList());
    }

    private Map<String, Integer> getValidProducts() {
        Map<String, Integer> products = null;
        boolean retry = true;
        while (retry) {
            try {
                products = ProductParser.parse(inputView.requestPurchaseInput());
                checkForDuplicates(products);
                validateProducts(products);
                products = applyPromotions(products);
                retry = false;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
        return products;
    }

    private void checkForDuplicates(Map<String, Integer> products) {
        Set<String> duplicateKeys = findDuplicateKeys(products.keySet());
        if (!duplicateKeys.isEmpty()) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private void validateProducts(Map<String, Integer> products) {
        products.forEach(ProductValidator::validateProduct);
    }

    private Set<String> findDuplicateKeys(Set<String> keys) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String key : keys) {
            if (!seen.add(key)) {
                duplicates.add(key);
            }
        }
        return duplicates;
    }

    private Map<String, Integer> applyPromotions(Map<String, Integer> products) {
        Map<String, Integer> updatedProducts = new HashMap<>(products);
        updatedProducts.forEach((productName, amount) -> {
            int updatedAmount = handlePromotion(productName, amount);
            updatedProducts.put(productName, updatedAmount);
        });
        return updatedProducts;
    }

    private int handlePromotion(String productName, int amount) {
        int updatedAmount = amount;

        updatedAmount = applyFreeItemPromotion(productName, updatedAmount);
        if (updatedAmount != amount) return updatedAmount;

        updatedAmount = applyRegularPricePromotion(productName, updatedAmount);
        return updatedAmount;
    }

    private int applyFreeItemPromotion(String productName, int amount) {
        int availablePromotionCount = productService.getAvailablePromotionCount(productName, amount);
        if (availablePromotionCount > 0 && inputView.askForFreeItem(productName, availablePromotionCount)) {
            return amount + availablePromotionCount;
        }
        return amount;
    }

    private int applyRegularPricePromotion(String productName, int amount) {
        int insufficientPromotionCount = productService.getInsufficientPromotionCount(productName, amount);
        if (insufficientPromotionCount > 0 && !inputView.askForRegularPrice(productName, insufficientPromotionCount)) {
            return amount - insufficientPromotionCount;
        }
        return amount;
    }
}