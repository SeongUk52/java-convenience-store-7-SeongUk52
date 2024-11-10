package store.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ProductService {
    void purchaseProducts(String input);
    List<String> getFormattedProductList();
    void saveAll(String filePath) throws IOException, URISyntaxException;
}
