# Item 40

# @Override 애너테이션을 일관되게 사용하라

@Override는 메서드 선언에만 달 수 있으며, 이 애너테이션이 달렸다는 것은 상위 타입의 메서드를 재정의했음을 의미한다. 이 애너테이션을 일관되게 사용하면 여러 가지 악명 높은 버그들을 예방해준다.

다음의 예제를 살펴보자 이 클래스는 바이그램, 즉 영어 알파벳 2개로 구성된 문자열을 표현한다.

```java
public class Bigram {
    private final char first;
    private final char second;
    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }
    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }
    public int hashCode() {
        return 31 * first + second;
    }
    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new Bigram(ch, ch));
        System.out.println(s.size());
    }
}
```

위의 바이그램 프로그램은 main 메서드에서 똑같은 소문자 2개로 구성된 바이그램 26개를 10번 반복해 집합에 추가한 다음, 그 집합의 크기를 출력한다. Set은 중복을 허용하지 않으니 26이 출력될 것 같지만, 실제로는 260이 출력된다.

확실히 Bigram의 작성자는 equals 메서드를 재정의하려 한 것으로 보이고 hashCode도 함께 재정의해야 한다는 사실을 잊지 않았다. 여기서 문제를 찾아 보자

- 문제 답
    
    @Override 애너테이션을 사용하지 않아 equals 메서드를 재정한 것이 아닌 다중정의(Overloading)해버렸다.
    
    Object의 equals를 재정의 하기 위해서는 매개변수 타입을 Object로 해야만 하는데, 그렇게 하지 않은 것이다. 재정의 하지 않은 Object의 equals는 == 연산자와 같이 객체 식별성만을 확인한다.
    
    따라서 같은 소문자를 소유한 10개의 바이그램이 각각 서로 다른 객체로 인식되고 결국 260을 출력한 것이다. 위의 문제를 해결하기 위해서 equals를 다음과 같이 수정해야 한다.
    
    ```java
    @Override public boolean equals(Object o) {
    		if(!(o instanceof Bigram))
    				return false;
    		Bigram b = (Bigram) o;
        return b.first == first && b.second == second;
    }
    ```
    

그러니 상위 클래스의 메서드를 재정의 하려는 모든 메서드에 @Override 애너테이션을 달자. 예외인 경우는 한가지다. 구체 클래스에서 상위 클래스의 추상메서드를 재정의할 때는 굳이 @Override를 달지 않아도 된다. 구체 클래스인데 아직 구현하지 않은 추상 메서드가 남아 있다면 컴파일러가 그 사실을 바로 알려주기 때문이다. 물론 재정의 메서드 모두에 @Override를 일괄로 붙여두어도 상관없다.

@Override는 클래스뿐 아니라 인터페이스의 메서드를 재정의할 때도 사용할 수 있다.

디폴트 메서드를 지원하기 시작하면서, 인터페이스 메서드를 구현한 메서드에도 @Override를 다는 습관을 들이면 시그니처가 올바른지 재차 확신할 수 있다.

구현하려는 인터페이스에 디폴트 메서드가 없음을 안다면 이를 구현한 메서드에서는 @Override를 생략해 코드를 조금 더 깔끔히 유지해도 좋다. 다만, 추상 클래스나 인터페이스에서는 상위 클래스나 상위 인터페이스의 메서드를 재정의하는 모든 메서드에 @Override를 다는 것이 좋다. 상위 클래스가 구체 클래스든 추상 클래스든 마찬가지다.

- ex. Set 인터페이스는 Collection 인터페이스를 확장했지만 새로 추가한 메서드는 없다. 따라서 모든 메서드 선언에 @Override를 달아 실수로 추가한 메서드가 없음을 보장했다.