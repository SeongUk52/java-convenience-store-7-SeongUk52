package store.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.PriceDetails;
import store.service.ProductService;
import store.util.ProductParser;
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
            Map<String, PriceDetails> productDetailsMap = new HashMap<>();
            displayProductList();
            boolean retry = true;
            while (retry) {
                try {
                    Map<String, Integer> products = ProductParser.parse(inputView.requestPurchaseInput());
                    products.forEach((productName, amount) -> {
                        int updatedAmount = handlePromotion(productName, amount);
                        products.put(productName, updatedAmount);
                    });
                    productDetailsMap = productService
                            .purchaseProducts(products, inputView.isMembership());
                    retry = false;
                } catch (IllegalArgumentException e) {
                    outputView.printErrorMessage(e.getMessage());
                }
            }

            List<String> receipt = ReceiptFormatterUtil.formatReceipt(productDetailsMap);
            outputView.printReceipt(receipt);
            isContinue = inputView.isContinue();
        }
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

    private void executeWithRetry(Runnable action) {
        boolean retry = true;
        while (retry) {
            try {
                action.run();
                retry = false;
            } catch (Exception e) {
                System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }
}
