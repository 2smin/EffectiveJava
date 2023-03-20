# item 23

# 태그 달린 클래스보다는 클래스 계층구조를 활용하라

**태그와 태그 달린 클래스**

태그는 해당 클래스가 어떤 타입인지를 나타내는 정보를 담고있는 멤버변수를 의미한다.

두가지 이상의 의미를 표현할 수 있으며 다음과 같이 현재 표현하는 의미를 태그 값으로 가지는 클래스를 태그 달린 클래스라 한다.

```java
public class Figure {
    enum Shape {RECTANGLE, CIRCLE}

    //태그 필드 - 현재 모양을 나타낸다.
    private Shape shape;

    // 다음 필드들은 모양이 사각형일 때만 사용.
    private double length;
    private double width;

    // 다음 필드들은 모양이 원일 때만 싸용.
    private double radius;

    //원용 생성자
    public Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    //사각형용 생성자
    public Figure(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }

    private double area() {
        switch (shape) {
            case RECTANGLE:
                // 가로 * 세로 = 사각형 크기
                return length * width;
            case CIRCLE:
                // PI * (r ^ 2)
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
} 
```

위와 같은 코드의 문제점

- 열거 타입선언, 태그 필드, switch 문 등 쓸데없는 코드가 많다.
- 여러 구현이 한 클래스에 혼합되어있어 가독성이 나쁘다.
- 다른 의미를 위한 코드도 언제나 함께하기 때문에 메모리도 많이 사용한다.
- final로 선언하려면 해당 의미에 쓰이지 않는 필드들까지 생성자에서 초기화 해야 한다.
- 생성자가 태그 필드를 설정하고 해당 의미에 쓰이는 데이터 필드들을 초기화하는데  컴파일러가 도울 수 있는게 별로 없다.
- 엉뚱한 필드를 초기화해도 런타임에야 문제가 드러난다.
- 또 다른 의미를 추가하려면 코드를 수정해야한다.
    - 새로운 의미를 추가할 때 마다 switch문을 찾아 새로운 의미를 처리하는 코드를 추가해야한다
        - 하나라도 빠뜨릴 경우 런타입에 문제가 발생한다.
- 인스턴스의 타입만으로는 현재 나타내는 의미를 알 수 없다.

**태그 달린 클래스 to 클래스 계층 구조**

위의 코드를 클래스 계층 구조로 바꾸는 과정은 다음과 같다.

1. 계층 구조의 루트가 될 추상 클래스를 정의하고, 태그 값에 따라 동작이 달라지는 메서드들을 루트 클래스의 추상 메서드로 선언한다.
    
    ```java
    abstract class Figure{
        abstract double area();
    }
    ```
    
2. 루트 클래스를 확장한 클래스를 의미별로 하나씩 정의한다.
    
    ```java
    abstract class Figure{
        abstract double area();
    }
    
    class Circle extends Figure {
        final double radius;
    
        Circle(double radius) { this.radius = radius; }
        @Override
        double area() {
            return Math.PI * (radius * radius);
        }
    }
    
    class Rectangle extends Figure {
        final double length;
        final double width;
    
        Rectangle(double length, double width){
            this.length = length;
            this.width = width;
        }
        @Override
        double area() { return length * width; }
    }
    ```
    

클래스 계층 구조로 변경하는 것만으로 위에 나열된 모든 단점이 사라졌다. 또한 정사각형 이라는 새로운 의미를 추가할 때의 확장성도 간단하다.

```java
class Square extends Rectangle { 
    Square(double side) {
        super(side,side);
    }
}
```