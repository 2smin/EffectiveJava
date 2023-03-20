# Item36

### 비트 필드 대신 EnumSet을 사용하라

- 비트 필드 예시

```java
public class Text {
	// 텍스트의 스타일을 비트로 표현
	public static final int STYLE_BOLD = 1 << 0; // 1
	public static final int STYLE_ITALIC = 1 << 1; // 2
	public static final int STYLE_UNDERLINE = 1 << 2; // 4
	public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8

	public void applyStyles(int styles) { ... }
}

// 사용 예 
text.applyStyles(STYLE_BOLD | STYLE_ITALIC);
```

- 단점
    - 정수 열거 상수의 단점을 그대로 지님
    - 비트필드 값이 그대로 출력되면 해석하기 훨씬 어려움
    - 비트필드에 있는 원소 순회 까다로움
    - 최대 몇 비트가 필요한지 API 작성시 미리 예측하여 적절한 타입(int, long)을 선택해야 함
    
- EnumSet
    - 열거 타입 상수의 값으로 구성된 집합을 효과적으로 표현
    - Set 인터페이스 구현됨
    - 타입 안전 및 다른 Set 구현체와 함께 사용 가능
    - 내부는 비트 벡터로 구현되어 있음
    - 원소 총 64개까지 가능 (비트 필드의 경우 long 타입)

```java
public class Text {
	public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }
	// 어떤 Set을 넘겨도 되지만, EnumSet이 가장 좋음
	public void applyStyles(Set<Style> styles) { ... }
}

// 사용 예시
text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
```

- applyStyles()가 Set<Style>을 받은 이유
    - 모든 클라이언트가 EnumSet을 넘기겠지만, 이왕이면 인터페이스로 받는게 일반적으로 좋은 습관임(item64)