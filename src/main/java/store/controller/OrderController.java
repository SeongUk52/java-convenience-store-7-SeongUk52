package store.controller;

import store.service.ProductService;
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
        productService.purchaseProducts(inputView.requestPurchaseInput());
    }

    private void displayProductList() {
        outputView.printFormattedProductList(productService.getFormattedProductList());
    }
}
