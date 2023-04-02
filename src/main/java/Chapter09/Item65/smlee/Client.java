package Chapter09.Item65.smlee;

import java.lang.reflect.InvocationTargetException;

public class Client {

    public static void main(String[] args) {

        Class<? extends Coffee> instance = null;
        Coffee coffeeInstance = null;
        try{
            instance = (Class<? extends Coffee>) Class.forName("Chapter09.Item65.smlee.CaffeLatte");
            coffeeInstance = instance.getDeclaredConstructor().newInstance();
        }catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException |
                InvocationTargetException e){
            e.printStackTrace();
        }

        System.out.println(coffeeInstance.extractCoffee());
        System.out.println(coffeeInstance.addCream("whiteCream"));

    }
}
