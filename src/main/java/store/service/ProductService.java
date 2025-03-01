package store.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import store.model.PriceDetails;

public interface ProductService {
    Map<String, PriceDetails> purchaseProducts(Map<String, Integer> products, boolean isMembership);
    List<String> getFormattedProductList();
    void saveAll(String filePath) throws IOException, URISyntaxException;
    int getAvailablePromotionCount(String productName, int currentCount);
    int getInsufficientPromotionCount(String productName, int currentCount);
}
