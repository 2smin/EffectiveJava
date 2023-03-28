package Chapter08.Item60.hwkim;

public class WrongGreedyCandy {
    public static void main(String[] args){
        double funds = 1.00;
        int itemsBought = 0;
        for(double price = 0.10; funds >= price; price += 0.10){
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈 (달러) : " + funds);
    }
}
