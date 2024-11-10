package store.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface ProductService {

    void purchaseProducts(Map<String, Integer> products);

    List<String> getFormattedProductList();
    void saveAll(String filePath) throws IOException, URISyntaxException;


    int getAvailablePromotionCount(String productName, int currentCount);
}
