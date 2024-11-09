package store.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import store.infrastructure.DataParser;
import store.infrastructure.FileUtils;
import store.model.Promotion;

public class FilePromotionRepository implements PromotionRepository {
    private final List<Promotion> promotions;

    public FilePromotionRepository(String filePath, DataParser<Promotion> dataParser) throws IOException, URISyntaxException {
        promotions = FileUtils.loadFromFile(filePath, dataParser);
    }

    @Override
    public Promotion findByName(String name) {
        return promotions.stream()
                .filter(i -> i.isNameEqualTo(name))
                .findFirst()
                .orElseThrow();
    }
}
