# Item 38

# 확장할 수 있는 열거타입이 필요하면 인터페이스를 사용하라

열거 타입은 거의 모든 상황에서 타입 안전 열거 패턴보다 우수하다.

```
타입 안전 열거 패턴 : 클래스를 이용하여 열거 타입과 유사하게 동작하는 객체를 생성하는 패턴으로
private 생성자를 이용한 싱글톤 패턴을 이용.
```

타입 안전 열거 패턴 예제

```java
public class TypesafeOperation {
    private final String type;
    private TypesafeOperation(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
    
    public static final TypesafeOperation PLUS = new TypesafeOperation("+");
    public static final TypesafeOperation MINUS = new TypesafeOperation("-");
    public static final TypesafeOperation TIMES = new TypesafeOperation("*");
    public static final TypesafeOperation DIVIDE = new TypesafeOperation("/");
}
```

단 타입 안전 열거 패턴은 확장할 수 있으나 열거 타입은 그럴 수 없다는 점이다. 사실 대부분의 상황에서 열거 타입을 확장하는 것은 좋지 않은 생각이다.

- 확장한 타입의 원소는 기반 타입의 원소로 취급하지만, 그 반대는 성립하지 않는다면 이상하다.(???)
    - 기반 타입의 원소는 확장한 타입의 원소로 취급하지 않는다.
- 기반 타입과 확장된 타입들의 원소 모두를 순회할 방법도 마땅치 않다.
- 확장성을 높이려면 고려할 요소가 늘어나 설계와 구현이 더 복잡해진다.

그러나 확장할 수 있는 열거 타입이 어울리는 쓰임이 최소한 하나는 있다. 바로 연산 코드다. 연산 코드의 각 원소는 특정 기계가 수행하는 연산을 뜻한다. API가 제공하는 기본 연산 외에 사용자 확장 연산을 추가하도록 해야할 때가 있다.

**열거 타입의 확장 방법**

열거 타입이 임의의 인터페이스를 구현할 수 있다는 것을 이용하여 열거 타입의 사용자 확장 연산을 추가하도록 하는 방법이 있다. 연산 코드용 인터페이스를 정의하고 열거 타입이 이 인터페이스를 구현하게 하는 것이다. 이 때 열거 타입이 그 인터페이스의 표준 구현체 역할을 한다.

```java
public interface Operation {
    double apply(double x, double y);
}

public enum BasicOperation implements Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() { return symbol; }
}
```

열거 타입인 BasicOperation은 확장할 수 없지만 인터페이스인 Operation은 확장할 수 있고, 이 인터페이스를 연산의 타입으로 사용하면 된다. 이렇게 하면 Operation을 구현한 다른 열거 타입을 정의해 기본 타입인 BasicOperation을 대체할 수 있다.

```java
public enum ExtendedOperation implements Operation {
    EXP("^") {
        @Override
        public double apply(double x, double y) { return Math.pow(x, y); }
    },
    REMAINDER("%") {
        @Override
        public double apply(double x, double y) { return x % y; }
    };

    private final String symbol;

    ExtendedOperation(String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String toString() { return symbol; }
}
```

개별 인스턴스 수준에서뿐만 아니라 타입 수준에서도, 확장된 열거 타입을 넘겨 확장된 열거 타입의 원소 모두를 사용하게 할 수도 있다.

**한정적 타입 토큰 이용**

```java
public static void main(String[] args){
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
		test(ExtendedOperation.class, x, y);
}

private static <T extends Enum<T> & Operation> void test(Class<T> opEnumType, double x, double y) {
    for (Operation op : opEnumType.getEnumConstants()) {
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
```

test 메서드에 ExtendedOperation의 class 리터럴을 넘겨 확장된 연산들이 무엇인지 알려준다. 여기서 class 리터럴은 한정적 타입 토큰 역할을 한다. opEnumType 매개 변수의 선언은 Class 객체가 열거 타입인 동시에 Operation의 하위 타입이어야 한다는 뜻이다. 열거 타입이어야 원소를 순회할 수 있고, Operation이어야 원소가 뜻하는 연산을 수행할 수 있기 때문이다.

**한정적 와일드카드 타입을 이용**

Class 객체 대신 한정적 와일드카드 타입인 Collection<? extends Operation>을 넘기는 방식이다.

```java
public static void main(String[] args){
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
		test(ExtendedOperation.class, x, y);
}

private static void test(Collection<? extends Operation> opSet, double x, double y) {
    for (Operation op : opSet) {
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
```

코드가 덜 복잡하고 test 메서드가 더 유연해졌다. 다시 말해 여러 구현 타입의 연산을 조합해 호출할 수 있게 되었다. 반면 특정 연산에서는 EnumSet과 EnumMap을 사용하지 못한다.

**제약사항**

인터페이스를 이용해 확장 가능한 열거 타입을 흉내내는 방식에도 열거 타입끼리 구현을 상속할 수 없다는 문제가 있다. 따라서 아무 상태에도 의존하지 않는 경우에는 디폴트 구현을 이용해서 인터페이스에 추가하는 방법이 있지만 예제와 같이 동일한 역할을 하는 로직이 많이 공유된다면 해당 로직을 도우미 클래스나 정적 도우미 메서드로 분리하는 것이 좋다.(BasicOperation과 ExtendedOperation 모두 연산 기호를 저장하고 찾는다.)