# Item38

# 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라.

타입 안전 열거패턴은 확장할 수 있으나 열거 타입은 그렇지 않다. 애초에 열거 타입을 확장할 수 있다는건 좋지 않은 생각이다.

### 열거타입에서의 연산코드

하지만 연산코드의 경우 열거타입에서는 확장이 필요할 수 있다. (열거타입에서의 메서드)

열거타입이 인터페이스를 확장할 수 있기때문에, 해당 인터페이스에 추상 메서드를 만들어 놓고 enum에서 확장하여 구현하면 된다.

```java
public interface Operation {
    double apply(double x, double y);
}
```

```java
public enum BasicOperation implements Operation{

PLUS("+"){
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },

MINUS("-"){
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    };

    private final String symbol;

    BasicOperation(String symbol){
        this.symbol = symbol;
    }

}
```

```java
public enum ExtendedOperation implements Operation{

EXP("^"){
        @Override
        public double apply(double x, double y) {
            return Math.pow(x,y);
        }
    };

    private final String symbol;

    ExtendedOperation(String symbol) {
        this.symbol = symbol;
    }
}
```

### 확장 enum 사용 예시

```java
public class Client {

    public static void main(String[] args) {
        System.out.println(BasicOperation.MINUS.apply(3,2));

				test(BasicOperation.class,4,2);
				test(ExtendedOperation.class,4,2);

				test(Arrays.asList(ExtendedOperation.values()),5,3);

        Operation[] opList = new Operation[]{BasicOperation.MINUS, ExtendedOperation.EXP};
        test(Arrays.asList(opList),4,6);
    }

    //enum class의 모든 원소 순환하며 실행
    private static <T extends Enum<T> & Operation> void test(Class<T> enumType, double x, double y){

        for(Operation op : enumType.getEnumConstants()){
            System.out.println(op.apply(x,y));
        }
    }

		private static void test(Collection<? extends Operation> enumList, double x, double y){
        for(Operation op : enumList){
            System.out.println(op.apply(x,y));
        }
    }
}

```

### 확장 열거타입의 문제점

확장 가능한 열거타입에도 문제점이 있다. 바로 구현체인 열거타입끼리는 구현을 상속할 수 없다는것이다. 코드 중복량이 적은 경우에는 큰 문제가 되진 않지만, 공유하는 기능이 많아진다면 문제가 된다.

이 경우는 별도의 도우미 클래스를 두거나 정적 도우미 메서드로 분리하는것이 바람직하다.