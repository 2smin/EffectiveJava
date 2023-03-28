package Chapter08.Item60.hwkim;

import java.math.BigDecimal;

public class GreedyCandy {
    public static void main(String[] args){
//        final BigDecimal TEN_CENTS = new BigDecimal(".10");
//
//        BigDecimal funds = new BigDecimal("1.00");
//        int itemsBought = 0;
//        for(BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)){
//            funds = funds.subtract(price);
//            itemsBought++;
//        }


        int itemsBought = 0;
        int funds = 100;
        for(int price = 10; funds >= price; price += 10){
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈 (달러) : " + funds);
    }
}
