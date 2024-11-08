package store.repository;

import java.util.List;
import store.model.Product;

public interface ProductRepository {
    List<Product> findByName(String name);
}
