package Chapter04.Item23.hwkim.HierarchicalClass;

public class Circle extends Figure{
    final double radius;

    Circle(double radius) { this.radius = radius; }
    @Override
    double area() {
        return Math.PI * (radius * radius);
    }
}
