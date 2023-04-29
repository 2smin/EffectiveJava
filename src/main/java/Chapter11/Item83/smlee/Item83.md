# Item83

# 지연 초기화는 신중히 사용하라

### 지연초기화란?

필드의 초기화 시점을 그 값이 처음 필요할때까지 늦추는 기법

값이 전혀 쓰이지 않으면 초기화도 일어나지 않는다. static 필드와 instance 필드 모두 사용가능하다.

주로 최적화 용도로 쓰이며, 순환문제를 해결하는 효과도 있다.

### 지연 초기화는 필요할때까지 하지 말자.

지연 초기화를 사용하면 클래스 혹은 인스턴스 생성 시의 초기화 비용은 줄어들지만 대신 지연 초기화하는 필드에 접근하는 비용은 커진다. 지연 초기화 하는 필드들 중 초기화가 이루어지는 비율, 비용, 필드 호출 빈도에 따라 지연 초기화가 성능을 느려지게 할 수 있다.

### 지연 초기화가 필요할 때

해당 클래스의 인스턴스 중 그 필드를 사용하는 인스턴스의 비율이 낮은 반면, 필드 초기화 비용이 크다면 지연 초기화가 제 역할을 해준다.

### 대부분의 상황에서는 일반적인 초기화가 지연 초기화 보다 낫다.

### 인스턴스 필드 지연 초기화 예시

```java
private final FieldType field =computeFieldValue();

private static FieldType computeFieldValue(){
    return new FieldType();
}
```

```java
private FieldType field2;

private synchronized  FieldType getField(){
    if(field2 == null){
        field2 =computeFieldValue();
    }

    return field2
}
```

### static field 지연 초기화 예시

일반적으로 static 필드를 지연 초기화 한다면 지연 초기화 홀더 클래스 관용구를 사용한다. (정적 팩토리랑 비슷)

```java
private static class FieldHolder{
    static final FieldTypefield=computeFieldValue();
}

private static FieldType getStaticField() { return  FieldHolder.field; }
```

getStaticField() 가 호출되면 FieldHolder가 처음 읽히면서 FieldHolder 클래스를 초기화 시킨다. VM에서 클래스를 처음 초기화 할때 동기화를 걸고, 그 이후는 동기화를 걸지 않는다. synchronize 를 붙일 필요가 없다.

### 인스턴스 필드를 지연 초기화 할때는 이중 검사 관용구를 사용하자.

필드의 값을 두번 검사하는 방식으로, 한번은 동기화 없이 검사하고 두번째는 동기화 하여 검사한다.

```java
private volatile FieldType field3;

private FieldType getField3(){

    //초기화 된 상황에서 필드를 딱 한번만 읽도록 보장한다. 왜 필요하지?
    FieldType result = field3;
    if(result != null){
        return result;
    }

    synchronized (this){ //필드가 없으므로 초기화 해야한다. sync를 해줘야 함
        if(field3 == null){
            field3 =computeFieldValue();
        }
        return field3;
    }
}
```

result 지역변수를 통해서 초기화 된 상황에서 필드를 딱 한번만 읽도록 보장한다. 만약 없으면??? static 영역 가서 읽는 비용이 증가하나?????

이중검사를 정적 필드에도 적용 가능하지만 굳이 그럴 필요는 없고 그냥 holder 쓰는게 더 낫다.

### 단일 검사

인스턴스 필드를 반복해서 초기화 해도 상관 없는 경우는 이중 검사에서 두번째 검사를 생략한다. 필드는 여전히 volatile로 선언한다.

```java

//중복 초기화가 상관 없는 단일 검사 (중복 초기화니까 sync도 필요 없다)
private volatile FieldType field4;
private FieldType getField4(){
    FieldType result = field4;

    if(result == null){
        field4 = result =computeFieldValue();
    }
    return result;
}
```

모든 스레드가 필드의 값을 다시 계산해도 상관없고 필드의 타입이 long과 double을 제외한 다른 기본타입이라면 단일 검사의 필드 선언에 volatile을 없애도 된다 (byte 수와 관련이 있는듯)

이 검사법을 racy single check 라고 한다. 이 방법은 필드 접근 속도를 높여주지만 초기화가 스레드당 최대 한번 더 이루어질 수 있다. 잘 안쓰이긴 한다.