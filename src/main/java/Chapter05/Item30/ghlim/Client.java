package Chapter05.Item30.ghlim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Chapter05.Item30.ghlim.Rectangle.*;

public class Client {

    public static void main(String[] args) {

        List<Rectangle> rectangles = new ArrayList<>();

        rectangles.add(new Rectangle(1,1));
        rectangles.add(new Rectangle(2,3));
        rectangles.add(new Square(3));

        Rectangle r = max(rectangles);
    }


}
