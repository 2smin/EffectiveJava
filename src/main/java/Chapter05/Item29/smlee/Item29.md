# Item29

# 이왕이면 제네릭 타입으로 만들라

기존 클래스를 제네릭을 만들 수 있으면 제네릭을 사용해서 바꾸자.

### 제네릭 클래스로 만들기

```java
public class Box<E> {

    private E[] items;
    private int size;

    public Box(){
        items = new E[10]; //컴파일 불가 (제네릭은 배열 불가)
    }

    public void push(E e){
        items[size++] = e;
    }
}

```

```java
public class Box<E> {

    private E[] items;
    private int size;

    public Box(){
        items = (E[]) new Object[size]; //push에서 E만 받기 때문에 안전하다. 
    }

    public void push(E e){
        items[size++] = e;
    }
}
```

위 코드는 경고가 발생하긴 하지만, 우리는 push에서 E 타입만 받는다는 것을 알 수있다. field는 private이라 접근 불가능하기때문에 확실히 안전하다. [SuppressWarnings로 경고만 제거하면 깔끔](https://www.notion.so/SuppressWarnings-99d853703e3e409688853ba8d6973128)

```java
public class Box<E> {

    private Object[] items;
    private int size;
    
    public Box(){
        items = new Object[size];
    }

    public void push(E e){
        items[size++] = e;
    }
    
    public E pop(){
        E result = (E) items[--size];
        return result;
    }
}
```

Object로 필드 타입을 변경한 경우, pop의 items를 꺼내올 때 타입 캐스팅 경고가 난다. 하지만 마찬가지로 private 필드, push 에서 E 타입만 받을수 있기 때문에 확실히 안전하다.

여기서도 @SuppressWarnings 로 경고 제거해주자.

첫번째 방식은 가독성이 좋고 형변환을 배열 생성시에만 해주면 되지만, 런타임 타입이 컴파일 타입과 달라 힙 오염을 일으킨다.

두번째 방식은 형변환을 배열을 읽을 때마다 해준다. 하지만 힙 오염을 일으키지 않는다.

### 제네릭 타입 안에서의 배열과 리스트

대다수의 제네릭 타입은 타입 매개변수에 제한이 없다. 다만 기본형 타입 (int, double) 등은 사용할 수 없다.