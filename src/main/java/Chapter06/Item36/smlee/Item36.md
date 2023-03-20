# Item36

# 비트 필드 대신 EnumSet을 사용하라

메모리가 부족하던 시절에는 자바에서 비트 필드 열거 상수를 사용하여 효율을 극대화 시켰다.

예를들어 boolean 은 0, 1 두가지 케이스만 있으므로 1 bit로 표현이 가능하지만, 자바에서는  2byte를 사용하여 표현하게 된다.

메모리 절약 측면에서 상수를 bit 값으로 표현 후 해당 필드들을 효율적으로 표현 가능했다.

```java
public class Text {

    public static final int STYLE_BOLD = 1 << 0;
    public static final int STYLE_ITALIC = 1 << 1;
    public static final int STYLE_UNDERLINE = 1 << 2;

    public int style;
    public void applyStyles(int styles){
        this.style = styles;
    }
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(STYLE_BOLD | STYLE_UNDERLINE);
        System.out.println(text.style);
    }
}
```

위의 경우 세가지 field의 모든 case를 단 3bit로 표현 가능하다.

ex). 000(2) ~ 111(2)

psvm에서 넣은 값은 bit 연산으로 101(2)가 들어갔다.

### 비트필드의 단점

- 비트필드값이 그대로 출력되면 해석하기가 어렵다.
- 비트필드 하나에 녹아있는 모든 원소를 순회하기도 어렵다(?)
- 최대 몇비트가 필요한지를 api 작성 시 미리 예측하여야 한다. (위의 예시는 3개의 필드가 있으므로 int로 커버 가능)
    
    ```java
    public class Text {
    
        public static final byte STYLE_BOLD = 1 << 0;
        public static final byte STYLE_ITALIC = 1 << 1;
        public static final byte STYLE_UNDERLINE = 1 << 2;
        public static final byte STYLE_PADDING = 1 << 3;
        public static final byte STYLE_BLANK = 1 << 4;
        public static final byte STYLE_COLOR = 1 << 5;
        public static final byte STYLE_BACKGROUND = 1 << 6;
        public static final byte STYLE_TRIM = 1 << 7; //bit 부족으로 연산 불가
    }
    ```
    

### EnumSet을 사용하자

```java
public void applyStyles(Set<Text> enumSet){} //EnumSet을 받을 수 있도록 Set인터페이스 지정
```

enumSet 내부는 비트 벡터로 구현되어있기 때문에, 연산이 빠르고 타입 안전하다.

내부적으로 set 원소가 64개 이하면 long 변수 하나로 표현하며, 그 이상인 경우 더 큰 타입을 사용한다.

```java
public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
    Enum<?>[] universe =getUniverse(elementType);
    if (universe == null)
        throw new ClassCastException(elementType + " not an enum");

    if (universe.length <= 64)
        return new RegularEnumSet<>(elementType, universe);
    else
        return new JumboEnumSet<>(elementType, universe);
}

```