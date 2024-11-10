package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String requestPurchaseInput() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    public boolean askForFreeItem(String productName, int availablePromotionCount) {
        System.out.printf("%s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n",
                productName, availablePromotionCount);
        return Console.readLine().equalsIgnoreCase("Y");
    }

    public boolean askForRegularPrice(String productName, int amount) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n",
                productName, amount);
        return Console.readLine().equalsIgnoreCase("Y");
    }

    public boolean isMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine().equalsIgnoreCase("Y");
    }
}
