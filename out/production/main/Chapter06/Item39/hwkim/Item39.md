# Item 39

# 명명 패턴보다 애너테이션을 사용하라

### 명명 패턴과 애너테이션 개요

전통적으로 도구, 프레임워크가 특별히 다뤄야 할 프로그램 요소에는 딱 구분되는 명명 패턴을 적용해 왔다.

예컨대 테스트 프레임워크인 JUnit은 버전 3까지 테스트 메서드 이름을 test로 시작하게끔 했다. 효과적인 방법이지만 단점도 크다.

- 오타가 나면 안된다.
    - 실수로 tsetSafetyOverride로 지으면 JUnit 3은 이 메서드를 무시하고 지나치기 때문에 개발자는 이 테스트가 통과했다고 오해할 수 있다.
- 올바른 프로그램 요소에서만 사용되리라 보증할 방법이 없다.
    - 클래스 이름을 TestSafetyMechanisms로 지어 JUnit에 던져줄 경우 개발자는 이 클래스에 정의된 테스트 메서드들을 수행해주길 기대하지만 JUnit은 클래스 이름에는 관심이 없다. 이번에도 테스트는 수행되지 않는다.
- 프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.
    - 특정 예외를 던져야만 성공하는 테스트가 있을 때, 예외의 이름을 테스트 메서드 이름에 덧붙이는 방법도 있지만, 보기도 나쁘고 깨지기도 쉽다.
    - 컴파일러는 메서드 이름에 덧붙인 문자열이 예외를 가리키는지 알 도리가 없다. 테스트를 실행하기 전에는 그런 이름의 클래스가 존재하는지 혹은 예외가 맞는지조차 알 수 없다.

애너테이션은 이 모든 문제를 해결해주는 멋진 개념으로 JUnit 4 부터 전면 도입하였다.

### 애너테이션의 동작

Test라는 이름의 애너테이션을 정의한다고 해보자.

자동으로 수행되는 간단한 테스트용 애너테이션으로 예외가 발생하면 해당 테스트를 실패로 처리한다.

```java
import java.lang.annotation.*;

/*
 * 테스트 메서드임을 선언하는 애너테이션이다.
 * 매개변수 없는 정적 메서드 전용이다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test{
}
```

@Retention과 @Target과 같이 애너테이션 선언에 다는 애너테이션을 메타애너테이션이라 한다.

@Retention(RetentionPolicy.RUNTIME) 메타애너테이션은 @Test가 런타임에도 유지되어야 한다는 표시다. 만약 이 메타애너테이션을 생략하면 테스트 도구는 @Test를 인식할 수 없다.

@Target(ElementType.METHOD) 메타애너테이션은 @Test가 반드시 메서드 선언에서만 사용돼야 한다고 알려준다. 따라서 클래스 선언, 필드 선언 등 다른 프로그램 요소에는 달 수 없다.

위의 코드는 “매개변수 없는 정적 메서드 전용”이라는 의도를 주석을 통해 달아놨다. 그러나 이 제약 사항을 컴파일러가 강제할 수 없기 때문에 적절한 애너테이션 처리기를 직접 구현해야한다. 관련 방법은 javax.annotation.processing API문서를 참고해야한다. 만약 적절한 애너테이션 처리기 없이 인스턴스 메서드나 매개변수가 있는 메서드에 이 애너테이션이 달린다면 컴파일은 잘 되나 테스트 도구를 실행할 때 문제가 된다.

**위의 @Test를 실제 적용한 예제** 

```java
public class Sample {
    @Test
    public static void m1() { // 설공해야 한다.
    }

    public static void m2() {
    }

    @Test
    public static void m3() { // 실패해야 한다.
        throw new RuntimeException("실패");
    }

    public static void m4() {
    }

    @Test
    public void m5() { // 잘못 사용한 예: 정적 메서드가 아니다.
    }

    public static void m6() {
    }

    @Test
    public static void m7() { // 실패해야 한다.
        throw new RuntimeException("실패");
    }

    public static void m8() {
    }
}
```

Sample 클래스는 정적 메서드가 7개고, 그중 4개에 @Test를 달았다. m3, m7은 예외를 던지고 m1과 m5는 예외를 던지지 않는다. 그리고 m5의 경우 인스턴스 메서드이므로 @Test를 잘못 사용한 경우이다. 

@Test가 Sample 클래스의 의미에 직접적인 영향을 주지는 않는다. 그저 이 애너테이션에 관심 있는 프로그램에게 추가 정보를 제공할 뿐이다.(@Test 애너테이션에 관심있는 도구에서 특별한 처리를 할 기회를 준다.)

다음의 RunTests를 살펴보자

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(m + "실패: " + exc);
                } catch (Exception exception) {
                    System.out.println("잘못 사용한 @Test: " + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}
```

이 테스트 러너는 명령줄로부터 완전 정규화된 클래스 이름을 받아, 그 클래스에서 @Test 애너테이션이 달린 메서드를 차례로 호출한다. isAnnoationPresent가 실행할 메서드를 찾아주는 메서드다. 테스트 메서드가 예외를 던지면 리플렉션 매커니즘이 InvocationTargetException으로 감싸서 다시 던진다.

그래서 이 프로그램은 InvocationTargetException을 잡아 원래 예외에 담긴 실패정보를 추출해(.getCause()) 출력한다. @Test 애너테이션을 잘못 사용한 경우 InvocationTargetException 이외의 예외가 발생하여 2번째 catch 블록에서 처리된다.

### 특정 예외를 던져야만 성공하는 테스트 애너테이션 지원

```java
import java.lang.annotation.*;

/*
 * 명시한 예외를 던저야만 성공하는 테스트 메서드 애너테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
```

매개변수 타입으로 Class<? extends Throwable>를 사용한다. “Throwable을 상속받은 클래스의 Class 객체”라는 뜻으로 모든 예외와 오류 타입을 다 수용한다. 이를 실제로 적용한 예는 다음과 같다.

**매개변수 하나짜리 애너테이션을 사용한 프로그램**

```java
public class Sample2 {
    @ExceptionTest(ArithmeticException.class)
    public static void m1() {  // 성공해야 한다.
        int i = 0;
        i = i / i;
    }
    @ExceptionTest(ArithmeticException.class)
    public static void m2() {  // 실패해야 한다. (다른 예외 발생)
        int[] a = new int[0];
        int i = a[1];
    }
    @ExceptionTest(ArithmeticException.class)
    public static void m3() { }  // 실패해야 한다. (예외가 발생하지 않음)
}
```

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
        	
            // 매개변수 하나를 받는 애너테이션 처리
            if (m.isAnnotationPresent(ExceptionTest.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (InvocationTargetException wrappedEx) {
                    Throwable exc = wrappedEx.getCause();
                    Class<? extends Throwable> excType =
                            m.getAnnotation(ExceptionTest.class).value();
                    if (excType.isInstance(exc)) {
                        passed++;
                    } else {
                        System.out.printf(
                                "테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n",
                                m, excType.getName(), exc);
                    }
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @ExceptionTest: " + m);
                }
            }
        }

        System.out.printf("성공: %d, 실패: %d%n",
                passed, tests - passed);
    }
}
```

@Test와 차이는 애너테이션 매개변수의 값을 추출하여 테스트 메서드가 올바른 예외를 던지는지 확인하는 데 사용한다. 여기서 더 나아가 여러 예외 중 하나가 발생하면 성공하게 만들어야 하는 경우도 있을 수 있다. 애너테이션 메커님즘에는 이런 쓰임에 아주 유용한 기능이 기본으로 들어 있다.

**배열 매개변수를 받는 애너테이션 타입**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Exception>**[]** value();
}
```

매개변수 타입을 Class 객체의 배열로 수정한 뒤 다음과 같이 애너테이션을 적용하면 아주 유용하다. 단일 원소 배열에 최적화했지만, 앞서의 @ExceptionTest들도 모두 수정 없이 수용한다. 원소가 여럿인 배열을 지정할 때는 다음과 같이 원소들을 중괄호로 감싸고 쉼표로 구분해주면 된다.

```java
// IndexOutOfBoundsException나 NullPointerException이 발생하면 성공
@ExceptionTest({ IndexOutOfBoundsException.class,
                 NullPointerException.class })
public static void doublyBad() {   // 성공해야 한다.
    List<String> list = new ArrayList<>();

    // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나
    // NullPointerException을 던질 수 있다.
    list.addAll(5, null);
}
```

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
        
            // 배열 매개변수를 받는 애너테이션 처리
            if (m.isAnnotationPresent(ExceptionTest.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (Throwable wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    **int oldPassed = passed;
                    Class<? extends Throwable>[] excTypes =
                            m.getAnnotation(ExceptionTest.class).value();
                    for (Class<? extends Throwable> excType : excTypes) {
                        if (excType.isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
										// 하나라도 올바른 예외가 발생했으면 성공
                    if (passed == oldPassed)
                        System.out.printf("테스트 %s 실패: %s %n", m, exc);**
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n",
                passed, tests - passed);
    }
}
```

**반복 가능 애너테이션**

자바 8에서는 여러개의 값을 받는 애너테이션을 @Repeatable 메타애너테이션을 다는 방식으로도 만들 수 있다. @Repeatable 메타애너테이션을 단 애너테이션은 하나의 프로그램 요소에 여러 번 달 수 있다. 단 다음의 항목을 주의해야 한다.

- @Repeatable을 단 애너테이션을 반환하는 **컨테이너 애너테이션**을 하나 더 정의하고, @Repeatable에 이 컨테이너 애너테이션의 **class 객체를 매개변수**로 전달해야 한다.
- 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 한다.
- 컨테이너 애너테이션 타입에는 적절한 보존 정책(@Retention)과 적용 대상(@Target)을 명시해야 한다.

```java
// 반복 가능한 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
**@Repeatable**(**ExceptionTestContainer.class**)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}

// 컨테이너 애너테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    **ExceptionTest[] value();**
}
```

이제 앞서 배열 매개변수를 받는 애너테이션 타입을 반복 가능 애너테이션으로 변경해 보자

```java
@ExceptionTest(IndexOutOfBoundsException.class)
@ExceptionTest(NullPointerException.class)
public static void doublyBad() {
    List<String> list = new ArrayList<>();

    // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나
    // NullPointerException을 던질 수 있다.
    list.addAll(5, null);
}
```

반복 가능 애너테이션은 처리할 때 주의를 요한다. 반복 가능 애너테이션을 여러 개 달면 하나만 달았을 때와 구분하기 위해 해당 '컨테이너' 애너테이션 타입이 적용된다. getAnnotationsByType 메서드는 이 둘을 구분하지 않아서 반복 가능 애너테이션과 그 컨테이너 애너테이션을 모두 가져오지만, isAnnotationPresent 메서드는 둘을 명확히 구분한다. 따라서 반복 가능 애너테이션을 여러번 단 다음 isAnnotationPresent로 반복가능 애너테이션이 달렸는지 검사한다면 “그렇지 않다”라고 알려준다. 그 결과 애너테이션을 여러번 단 메서드들을 모두 무시하고 지나친다.('컨테이너' 애너테이션 타입이 적용됐기 때문)

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {

            // 반복 가능 애너테이션의 경우 ExceptionTestContainer.class로 걸릴 것
						// 컨테이너의 경우도 ExceptionTestContainer.class
						// 반복 가능 애너테이션이 하나만 달린 경우 ExceptionTest.class에서 걸린다.
            if (m.isAnnotationPresent(ExceptionTest.class)
                    || m.isAnnotationPresent(ExceptionTestContainer.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (Throwable wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    int oldPassed = passed;
										// 컨테이너와 1~N개의 반복가능 애너테이션 모두를 받는다.
                    ExceptionTest[] excTests =
                            m.getAnnotationsByType(ExceptionTest.class);
                    for (ExceptionTest excTest : excTests) {
                        if (excTest.value().isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed)
                        System.out.printf("테스트 %s 실패: %s %n", m, exc);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n",
                          passed, tests - passed);
    }
}
```

반복 가능 애너테이션을 사용해 하나의 프로그램 요소에 같은 애너테이션을 여러번 달 때의 코드 가독성을 높일 수 있다. 그러나 애너테이션을 선언하고 처리하는 부분에서 코드의 양이 늘어나며, 처리 코드가 복잡해져 오류가 날 가능성이 커짐을 명심하자.