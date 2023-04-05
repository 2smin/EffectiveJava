# Item63

## 문자열 연결은 느리니 주의하라

- 문자열을 연결하는 + 연산자
    - 문자열이 n개라면, 시간은 n^2 만큼 소요
    - 문자열은 불변이기 때문에 양쪽 내용을 모두 복사해야하기 때문
    
- 문자열 연결을 하고 싶으면 StringBuilder를 사용

```java
public String statement() {
	String result = "";
	for (int i = 0; i < numItems(); i++)
	result += lineForItem(i); // String concatenation
	return result;
}
```

```java
public String statement() {
	StringBuilder b = new StringBuilder(numItems() * LINE_WIDTH);
	for (int i = 0; i < numItems(); i++)
	b.append(lineForItem(i));
	return b.toString();
}
```