# Item28

# 배열보다는 리스트를 사용하라

### 배열과 제네릭의 차이

- 배열은 공변, 제네릭은 불공변이다.

  ex). String[] < Object[]

  ex). List<String> = List<Object>

  그런데 사실 문제가 있는건 배열쪽이다. 제네릭은 아무 잘못이 없다..

    ```java
    //컴파일은 통과, 런타임 실패
    Object[] objectsArray = new Long[1];
    objectsArray[0] = "wer";
    
    //컴파일 실패
    List<Object> o1 = new ArrayList<Long>();
    ```

  어차피 안돌아갈 코드면 컴파일 단계에서 에러를 뱉는게 훨씬 났다.


- 배열은 실체화 된다.

  배열은 런타임에도 자신이 담기로 한 원소의 타입을 인지한다. 하지만 제네릭은 런타임에서 타입 정보가 사라진다.


### 제네릭으로 배열을 만들 수는 없다

```java
new List<E>[], new List<String>[]
```

위 같이 제네릭으로 배열을 쓸 수는 없다. 컴파일러가 자동 형변환 코드를 만들어주기 때문에 런타임에서 ClassCastException이 발생할 수 있다. 이는 제네릭이 타입 시스템의 취지와 어긋난다.

```java
List<String>[] stringList = new List<String>[1];
List<Integer> intList = List.of(42);
Object[] objects = stringList;
objects[0] = intList;
```

위같이 제네릭을 배열로 사용하면, (컴파일 에러가 안난다는 가정하에) stringList에 intList가 담기게 된다. 원소를 꺼내는 순간 String으로 형변환하게 되고, ClassCastException이 발생한다.

### 실체화 불가 타입

E, List<E> 같은 타입을 실체화 불가 타입이라고 하는데, 실체화 되지 않아서 런타임보다 컴파일타임에 타입 정보를 적게 가진다….? (위랑 말이 다르자나)

### 성능 대신 타입 안정성

배열로 형변환 할 때 경고가 뜨는 경우 보통은 E[] 대신 List<E> 를 사용하면 해결된다. 성능이 살짝 나빠질 순 있짐만 타입 안전성은 좋아진다.

### List를 쓰면 경고도 제거하고 안전성도 확보한다.

```java
public class Chooser<T> {

    private T[] choiceArray; //형변환 직접 해야하니까 경고가 뜬다

    public Chooser(Collection<T> choices){
        choiceArray = (T[])choices.toArray();
    }

    public static void main(String[] args) {
        Chooser chooser = new Chooser(new ArrayList<String >());
    }
}
```

```java
public class Chooser<T> {

    private List<T> choiceList; //경고 제거 (컴파일러가 알아서 형변환 해주기 때문)

    public Chooser(Collection<T> choices){
        choiceList = new ArrayList<>(choices);
    }

    public static void main(String[] args) {
        Chooser chooser = new Chooser(new ArrayList<String >());
    
```

코드양이 늘고 살짝 느리지만 안전성은 확보된다!!