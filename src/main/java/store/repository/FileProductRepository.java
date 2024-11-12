package store.repository;

import static store.constants.ErrorMessage.INVALID_FILE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.infrastructure.DataParser;
import store.infrastructure.FileUtils;
import store.model.Product;

public class FileProductRepository implements ProductRepository {
    private final List<Product> products;

    public FileProductRepository(String filePath, DataParser<Product> dataParser) throws IOException, URISyntaxException {
        List<Product> fileProducts = FileUtils.loadFromFile(filePath, dataParser);
        validateProductsFile(fileProducts);
        products = fileProducts;
    }

    @Override
    public List<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.isNameEqualTo(name))
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    public void saveToFile(String filePath) throws IOException {
        FileUtils.saveToFile(filePath, products);
    }

    private void validateProductsFile(List<Product> products) {
        Map<String, List<Product>> groupedByName = groupProductsByName(products);
        groupedByName.forEach(this::validatePricesForSameNameProducts);
    }

    private Map<String, List<Product>> groupProductsByName(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName));
    }

    private void validatePricesForSameNameProducts(String name, List<Product> productList) {
        long distinctPrices = productList.stream()
                .map(Product::getPrice)
                .distinct()
                .count();

        if (distinctPrices > 1) {
            throwFileFormatException();
        }
    }

    private void throwFileFormatException() {
        try {
            throw new IOException(INVALID_FILE.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
