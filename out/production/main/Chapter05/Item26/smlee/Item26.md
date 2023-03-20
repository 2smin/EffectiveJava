# Item26

# 로(raw) 타입은 사용하지 마라

| 매개변수화 타입 | List<String> |
| --- | --- |
| 제네릭 타입 | List<E> |
| 비한정적 와일드카드 타입 | List<?> |
| 로(raw) 타입 | List |
| 한정적 타입 매개변수 | <E extends Number> |
| 한정적 와일드카드 타입 | <? extends Number> |
|  |  |

### raw 타입을 쓰는 경우 발생하는 문제점

리스트에 타입 매개변수를 설정하지 않고 raw 타입으로 작성하면 컴파일 시점에서 오류를 걸러낼 수 없다.

```java
private static ArrayList arrList = new ArrayList();

    public static void main(String[] args) {

        arrList.add("a");
        arrList.add(new Client());

        for(Iterator i = arrList.iterator(); i.hasNext();){
            String str = (String)i.next();
        }
       
    }
```

Client는 String으로 casting이 되지 않으므로, 에러가 발생한다. iterator를 순회해야만 에러를 잡아낼 수 있다.

### raw 타입의 대안 - 임의객체의 매개변수화 타입

List<Object> 처럼 사용하면 모든 타입을 허용하는 의미를 컴파일러에게 전달할 수 있다. 

클래스 간 상하구조는 존재하나, 매개변수화 된 클래스에서의 상하관계는 존재하지 않는다.

ex). Object < Number < Integer

ex). List<Object> = List<Number> = List<Integer>

그렇기 때문에 Object로 매개변수화 타입을 정의해놓으면 컴파일 시점에서 다른 타입이 들어와도 걸러진다!

```java
public static void printList2(List<Object> list){
    for(Object o : list){
        System.out.println("element: " + o);
    }
}

public static void main(String[] args) {
		List<String> strList = new ArrayList<>();
    printList2(strList); //compile 이전에 에러가 발생
}
```

### 매개변수화 타입의 한계

위에서 말했듯이 매개변수화 타입으로  Object를 선언해 놓으면 object를 상속받은 클래스도 들어 올 수 있을것 같지만, 매개변수 클래스에서 상하관계란 존재하지 않는다.

이런 경우 와일드 카드를 사용해서 타입 안전성을 유지할 수 있다.

```java
public static int numElementsInCommon1(Set s1, Set s2){
        int result = 0;
        for(Object o1 : s1){
            if(s2.contains(o1)){
                result++;
            }
        }
        return result;
    }

    public static int numElementsInCommon2(Set<?> s1, Set<?> s2){
        int result = 0;
        for(Object o1 : s1){
            if(s2.contains(o1)){
                result++;
            }
        }
        return result;
    }
		//컴파일 타임에 에러를 잡아 줄 수 있다.
```

### 로 타입을 사용해도 되는 예외 케이스

- class 리터럴에는 raw 타입을 쓰자
    
    ex). List.class (**O**) List<String>.class (**X**)
    
- instanceof
    
    런타임에서는 제네릭 타입 정보가 지워지기 때문에 instanceof는 비한정적 와일드카드 타입 말고는 작동하지 않는다. 또한 raw 타입이든 비한정적 와일드 카드이던 똑같이 동작한다.
    
    ```java
    if(o instanceof Set){
    	Set<?> s = (Set<?>)o;
    }
    ```
    

### 추가) 와일드카드 대체 왜 쓸까?

Collection<?> aaa 이런 경우 어떤 타입이든 넣지 못하고 null만 넣을수 있다.

? 의 의미는 ? extends Object와 같다. 매개변수에 Object를 넣어봤자 하위 클래스 타입을 받지 못한다. 그러므로 매개변수 에서는 ? 가 Object를 대신한다고 할 수있다.