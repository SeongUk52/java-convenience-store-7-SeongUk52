package store;

import store.config.AppConfig;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        appConfig.getOrderController().run();
    }
}
