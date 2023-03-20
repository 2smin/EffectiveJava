# item 22

# 인터페이스는 타입을 정의하는 용도로만 사용하라

클래스가 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 이야기해주는 것이며, 인터페이스는 오직 이 용도로만 사용해야 한다. 상수 인터페이스는 인터페이스를 잘못 사용한 예이다.

```java
public interface PhysicalConstants{
    static final double AVOGARDROS_NUMBER = 6.022_140_857e23;
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    static final double ELECTRON_MASS = 9.109_383_56e-3;
}
```

클래스 내부에서 사용하는 상수는 인터페이스가 아니라 내부 구현에 해당한다. 따라서 상수 인터페이스를 구현하는 것은 이 내부 구현을 클래스의 API로 노출하는 행위다. 클래스가 어떤 상수 인터페이스를 사용하는지는 클라이언트에게는 아무런 의미가 없으며, 오히려 혼란을 주기도 한다.

**상수 인터페이스를 사용하지 않으면서 상수를 공개하기 위한 도구**

1. 열거 타입
    
    ```java
    public enum TmaxBuilding { TMAX_TOWER, ORI_LAB, ...}
    ```
    
2. 인스턴스화 할 수 없는 유틸리티 클래스에 담아 공개
    
    ```java
    public class PysicalConstants{
        // private 생성자를 이용하여 인스턴스화 방지
        private PysicalConstants(){};
        public static final double AVOGARDROS_NUMBER = 6.022_140_857e23;
        public static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
        public static final double ELECTRON_MASS = 9.109_383_56e-3;
    }
    ```