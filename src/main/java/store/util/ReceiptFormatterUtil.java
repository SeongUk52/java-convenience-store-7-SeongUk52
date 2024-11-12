package store.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.model.PriceDetails;

public class ReceiptFormatterUtil {
    public static List<String> formatReceipt(Map<String, PriceDetails> productDetailsMap) {
        List<String> receipt = new ArrayList<>();
        receipt.addAll(formatHeader());
        receipt.addAll(formatProductList(productDetailsMap));
        receipt.addAll(formatPromoSection(productDetailsMap));
        receipt.addAll(formatSummary(productDetailsMap));
        return receipt;
    }

    private static List<String> formatHeader() {
        return List.of("==============W 편의점================");
    }

    private static List<String> formatProductList(Map<String, PriceDetails> productDetailsMap) {
        List<String> productList = new ArrayList<>();
        productList.add("상품명\t\t수량\t금액");
        productDetailsMap.forEach((name, details) ->
            productList.add(String.format("%s\t\t%,d\t%,d", name, details.totalCount(), details.totalPrice()))
        );
        return productList;
    }

    private static List<String> formatPromoSection(Map<String, PriceDetails> productDetailsMap) {
        List<String> promoSection = new ArrayList<>();
        promoSection.add("=============증\t정==============");
        productDetailsMap.forEach((name, details) -> {
            if (details.promotionCount() > 0) {
                promoSection.add(String.format("%s\t\t%,d", name, details.promotionCount()));
            }
        });
        return promoSection;
    }

    private static List<String> formatSummary(Map<String, PriceDetails> productDetailsMap) {
        List<String> summary = new ArrayList<>();
        summary.add("====================================");

        summary.add(formatTotalPurchase(productDetailsMap));
        summary.add(formatPromoDiscount(productDetailsMap));
        summary.add(formatMembershipDiscount(productDetailsMap));
        summary.add(formatFinalPrice(productDetailsMap));

        return summary;
    }

    private static String formatTotalPurchase(Map<String, PriceDetails> productDetailsMap) {
        int totalPurchase = productDetailsMap.values().stream().mapToInt(PriceDetails::totalPrice).sum();
        return String.format("총구매액\t\t%,d\t%,d", totalPurchase, totalPurchase);
    }

    private static String formatPromoDiscount(Map<String, PriceDetails> productDetailsMap) {
        int promoDiscount = productDetailsMap.values().stream().mapToInt(PriceDetails::promotionDiscount).sum();
        return String.format("행사할인\t\t\t-%,d", promoDiscount);
    }

    private static String formatMembershipDiscount(Map<String, PriceDetails> productDetailsMap) {
        int membershipDiscount = productDetailsMap.values().stream().mapToInt(PriceDetails::membershipDiscount).sum();
        return String.format("멤버십할인\t\t\t-%,d", membershipDiscount);
    }

    private static String formatFinalPrice(Map<String, PriceDetails> productDetailsMap) {
        int finalPrice = productDetailsMap.values().stream().mapToInt(PriceDetails::finalPrice).sum();
        return String.format("내실돈\t\t\t %,d", finalPrice);
    }
}