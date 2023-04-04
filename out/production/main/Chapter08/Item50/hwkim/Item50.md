# Item 50

# 적시에 방어적 복사본을 만들라

자바는 C, C++과 달리 버퍼 오버런, 배열 오버런, 와일드 포인터 같은 메모리 충돌 오류에서 안전하다.

하지만 아무리 안전하더라도 다른 클래스로부터의 침범을 다 막을 수 있는 것은 아니다. 따라서 **클라이언트가 불변식을 깨뜨리려 혈안이 되어 있다고 가정하고 방어적으로 프로그래밍해야한다.**

어떤 객체든 그 객체의 허락없이는 외부에서 내부를 수정하는 일은 불가능하다. 하지만 주의를 기울이지 않으면 자기도 모르게 내부를 수정하도록 허락하는 경우가 생긴다. 다음의 기간을 표현하는 클래스는 한번 값이 정해지면 변하지 않도록 할 생각이었다.

```java
import java.util.Date;

public final class Period {
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        if(start.compareTo(end) > 0){
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
        this.start = start;
        this.end = end;
    }

    public Date start(){
        return start;
    }

    public Date end(){
        return end;
    }
}
```

이 클래스는 불변처럼 보이고 시작이 종료보다 늦을 수 없다(시작이 종료보다 미래일 수 없다)는 불변식이 지켜질 것 같다. 그러나 Date가 가변이라는 사실을 이용하면 어렵지 않게 그 불변식을 깨뜨릴 수 있다.

```java
public class Client {
    public static void main(String[] args){
        Date start = new Date();
        Date end = new Date();
        start.setYear(100);
        //end.setYear(78);
        end.setYear(101);
        Period p = new Period(start, end);
        end.setYear(78);
        System.out.println(p.end());
    }
}
```

처음 p 인스턴스를 생성할 때 end의 년도는 2001년으로 start보다 미래다. 그러나 p 생성 후 end의 년도를 1978년으로 변경했다. 그리고 이렇게 변경하게 되었을 때 생성 시점에서는 불변식을 만족해 Exception이 발생하지만, 1978년으로 변경된 후에는 불변식을 만족하지 못한다.

**해결방안**

1. Java 8 이후로는 쉽게 해결할 수 있다. 해결 방법은 Date 대신 불변인 Instance를 사용하는 것이다. LocalDateTime이나 ZonedDateTime을 사용할 수 있다. Date같이 낡은 API를 새로운 코드를 작성할 때 사용하면 안된다.

1. 생성자에서 받은 가변 매개변수 각각을 방어적으로 복사한 뒤 Period 인스턴스 안에서는 복사본을 사용
    
    ```java
    import java.util.Date;
    
    public class ImmutablePeriod {
        private final Date start;
        private final Date end;
    
        public ImmutablePeriod(Date start, Date end) {
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
    
            if(this.start.compareTo(this.end) > 0){
                throw new IllegalArgumentException(this.start + "가 " + this.end + "보다 늦다.");
            }
        }
    
        public Date start(){
            return new Date(start.getTime());
        }
    
        public Date end(){
            return new Date(end.getTime());
        }
    }
    ```
    
    ```java
    import java.util.Date;
    
    public class Client {
        public static void main(String[] args){
            Date start = new Date();
            Date end = new Date();
            start.setYear(100);
            //end.setYear(78);
            end.setYear(101);
            Period p = new Period(start, end);
            end.setYear(78);
            System.out.println(p.end());
    
            end.setYear(101);
            ImmutablePeriod ip = new ImmutablePeriod(start, end);
            end.setYear(78);
            System.out.println(ip.end());
        }
    }
    ```
    
    이 방법을 이용하면 앞서의 공격은 더 이상 Period에 위협이 되지 않는다. **매개변수의 유효성을 검사하기 전에 방어적 복사본을 만들고, 복사본으로 유효성을 검사한 점에 주목하자.**
    
    멀티스레딩 환경이라면 원본 객체의 유효성을 검사한 후 복사본을 만드는 그 찰나의 취약한 순간에 다른 스레드가 원본 객체를 수정할 위험이 있기 때문에 순서가 이상해 보이더라도 위와 같이 코드를 작성해야 한다.
    
    방어적 복사에 Date의 clone 메서드를 사용하지 않은 점에도 주목해야한다. Date는 final이 아니기 때문에 clone이 Date가 정의한 게 아닐 수 있다. 즉 clone이 악의를 가진 하위 클래스의 인스턴스를 반환할 수도 있다. 따라서 **매개변수가 제 3자에 의해 확장될 수 있는 타입이라면 방어적 복사본을 만들 때 clone을 사용해서는 안된다.**
    

생성자를 수정하면 위와 같은 공격은 막을 수 있지만 Period 인스턴스는 아직도 변경가능하다. 접근자 메서드 start(), end()가 내부의 가변정보를 직접 드러내기 떄문이다. 따라서 ImmutablePeriod의 접근자 메서드 처럼 방어적 복사본을 반들어 복사본을 반환하도록 한다.

메서드든 생성자든 클라이언트가 제공한 객체의 참조를 내부의 자료구조에 보관해야 할 때면 항시 그 객체가 잠재적으로 변경될 수 있는지를 생각해야 한다. 변경 될 수 있다면 그 객체가 클래스에 넘겨진 뒤 임의로 변경되어도 문제없이 동작할지를 따져보아야 한다.

또한 **되도록 불변 객체들을 조합해 객체를 구성해야 방어적 복사를 할 일이 줄어든다.**  Period 예제의 경우, 자바 8이상으로 개발해도 된다면 Instant, LocalDateTime 또는 ZonedDateTime을 사용하는 것이 좋고 이전 버전의 자바를 사용한다면 Date 참조대신 Date.getTime()이 반환하는 long 정수를 사용하는 방법을 쓰는 것이 좋다.

방어적 복사에는 성능 저하가 따르고, 또 항상 쓸 수 있는 것이 아니다. 호출자가 컴포넌트 내부를 수정하지 않으리라 확신하면 방어적 복사를 생략할 수 있지만 이러한 상황이라도 호출자에서 해당 매개변수가 반환값을 수정하지 말아야 함을 명확히 문서화하는게 좋다.