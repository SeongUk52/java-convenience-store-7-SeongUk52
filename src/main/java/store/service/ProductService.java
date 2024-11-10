package store.service;

import java.util.List;

public interface ProductService {
    void purchaseProducts(String input);
    List<String> getFormattedProductList();
}
