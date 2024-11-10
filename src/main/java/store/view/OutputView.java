package store.view;

import java.util.List;

public class OutputView {
    public void printFormattedProductList(List<String> formattedProductList) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        formattedProductList.forEach(System.out::println);
        System.out.println();
    }

    public void printReceipt(List<String> receipt) {
        receipt.forEach(System.out::println);
    }
}
