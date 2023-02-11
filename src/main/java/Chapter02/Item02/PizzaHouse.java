package Chapter02.Item02;

import static Chapter02.Item02.NyPizza.Size.*;
import static Chapter02.Item02.Pizza.Topping.*;

public class PizzaHouse {

    NyPizza nypizza = new NyPizza.Builder(SMALL)
            .addTopping(SAUSAGE).addTopping(ONION)
            .build();

    Calzone calzone = new Calzone.Builder()
            .addTopping(HAM).sauceInside()
            .build();
}
