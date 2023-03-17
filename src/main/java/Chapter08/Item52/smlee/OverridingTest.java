package Chapter08.Item52.smlee;

import java.util.List;

public class OverridingTest {

    static class Coffee{
        String name() {return "coffee";}
    }

    static class Latte extends Coffee{
        @Override
        String name() {
            return "latte";
        }
    }

    static class Americano extends Coffee{
        @Override
        String name() {
            return "ame ame ame ame";
        }
    }

    public static void main(String[] args) {
        List<Coffee> coffeeList = List.of(new Coffee(), new Americano(), new Latte());
        for(Coffee coffee : coffeeList){
            System.out.println(coffee.name());
        }
    }
}
