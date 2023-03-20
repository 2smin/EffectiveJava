# Item27

# 비검사 경고를 제거하라

제네릭을 사용하게 되면 경고 수를 많이 줄일 수 있다. 최대한 경고를 제거하자.

경고를 제거할수록 타입 안전성을 보장할 수 있다. 런타임에 ClassCastException이 발생할 일이 없고, 의도된 대로 동작 가능하다.

@

## 

### 경고를 제거할 수 없다면?

경고를 제거 할 수 없지만, 타입이 안전하다고 확신한다면 **@SupressWarnings(”unchecked”)** 를 달아서 경고를 숨길 수 있다.

안전하다고 검증된 비검사 경고를 그대로 두면, 진짜 문제를 알리는 경고가 나와도 지나치기 쉬우니 꼭 제거하도록 하자.

### **@SupressWarnings**

개별 지역변수 ~ 클래스 전체에 적용 가능한 어노테이션이다. 다만 최대한 좁은 범위에 적용하는것이 적절하다.

```java
@SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
```

위와 같이 단 경우 메서드 전체에서 경고가 제거된다. 하지만 만약 return 문에서만 경고를 제거 하고 싶은 경우, 변수를 새로 선언해주고 해당 변수에 어노테이션을 달자.

```java

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
						@SuppressWarnings("unchecked") 
            T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
            return result;
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
```

위처럼 범위를 좁힐 수 있다.

**@SupressWarnings** 을 사용할때는 경고를 무시해도 안전한 이유를 주석으로 꼭 남기자!!