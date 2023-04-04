# Item54

# Null이 아닌 빈 컬렉션이나 배열을 반환하라.

배열을 반환하는 메서드에서 특정 상황에 따라 null을 반환하는 경우가 있을 수 있다.

이런 경우는 null을 반환하는것보다는 빈 collection이나 배열을 반환하는것이 났다.

```java
private final List<Cheese> cheeseInStock;

public List<Cheese> getCheeses() {
    return cheeseInStock.isEmpty() ? null : new ArrayList<>(cheeseInStock);
}
```

위 코드의 경우 null 을 반환하게 되면 해당 메서드를 사용하는 client는 null에 대한 처리를 추가로 해주어야 한다. 방어로직을 넣지 않으면 오류가 발생할 수 있다.

### 리소스 낭비 아닌가요?

이런 빈 배열 할당은 성능저하의 주범이 되는 경우는 거의 없다. 성능 차이가 있다하더라도 신경 쓸 수준이 되지는 않는다.

빈 컬렉션과 배열은 굳이 새로 할당하지 않아도 된다. 매번 같은 빈 컬렉션을 반환하면 그만이다.

Collections.emptyList 는 항상 동일한 싱글턴 빈 객체를 반환한다. Collections.emptySet, emptyMap을 사용해서 원하는 케이스에 붙여넣으면 된다.

```java
public List<Cheese> getCheeseWithEmptyList(){
    return cheeseInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheeseInStock);
}
```

배열을 사용하는 경우도 동일하게 0짜리 배열을 반환하면 된다. 미리 0짜리 배열을 선언해두고 반환하면 된다. 0인 배열은 모두 불변이다.

```java
private static final Cheese[]EMPTY_STOCK_ARRAY= new Cheese[0];

public Cheese[] getCheesesArray(){
    return cheeseInStock.toArray(EMPTY_STOCK_ARRAY);
}
```

- toArray에 주어지는 배열을 미리할당하는것은 성능이 떨어지는 연구결과도 있다. 단순히 성능을 개선할 목적이면 미리 배열을 할당하지 말자. (…?)