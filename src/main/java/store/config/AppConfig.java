package store.config;

import store.controller.OrderController;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    private static final AppConfig instance = new AppConfig();
    private final OrderController orderController;

    private AppConfig() {
        ServiceConfig serviceConfig = ServiceConfig.getInstance();

        InputView inputView = new InputView();
        OutputView outputView = new OutputView();

        orderController = new OrderController(
            serviceConfig.getProductService(),
            inputView,
            outputView
        );
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public OrderController getOrderController() {
        return orderController;
    }
}