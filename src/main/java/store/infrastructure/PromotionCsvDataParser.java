package store.infrastructure;

import java.util.List;
import store.model.Product;
import store.model.Promotion;

public class PromotionCsvDataParser implements DataParser<Promotion> {
    @Override
    public Promotion parseLine(String line) {
        List<String> fields = List.of(line.split(","));
        return new Promotion(
                fields.get(0),
                Integer.parseInt(fields.get(1)),
                Integer.parseInt(fields.get(2)),
                fields.get(3),
                fields.get(4)
        );
    }
}
