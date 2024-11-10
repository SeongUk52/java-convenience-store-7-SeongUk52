package store.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import store.model.PriceDetails;
import store.model.Promotion;
import store.model.PurchaseSummary;

class PriceCalculatorServiceImplTest {

    @Test
    void calculatePrice() {
        PriceCalculatorService priceCalculatorService = new PriceCalculatorServiceImpl();
        PriceDetails priceDetails = priceCalculatorService.calculatePrice(
                new PurchaseSummary(7, 3, 1000),
                new Promotion("1+1", 1, 1, "2000-01-01", "3000-01-01")
        );
        assertEquals(new PriceDetails(
                10000,
                3000,
                1200,
                5800
        ), priceDetails);
    }
}