package Chapter08.Item54.smlee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {

    public Client(List<Cheese> cheeseInStock) {
        this.cheeseInStock = cheeseInStock;
    }

    private final List<Cheese> cheeseInStock;

    public List<Cheese> getCheeses() {
        return cheeseInStock.isEmpty() ? null : new ArrayList<>(cheeseInStock);
    }

    public List<Cheese> getCheeseWithEmptyList(){
        return cheeseInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheeseInStock);
    }

    private static final Cheese[] EMPTY_STOCK_ARRAY = new Cheese[0];

    public Cheese[] getCheesesArray(){
        return cheeseInStock.toArray(EMPTY_STOCK_ARRAY);
    }
}
