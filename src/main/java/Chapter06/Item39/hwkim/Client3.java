package Chapter06.Item39.hwkim;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Client3 {
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
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }

    @MultiExceptionTest2(IndexOutOfBoundsException.class)
    @MultiExceptionTest2(NullPointerException.class)
    public static void doublyBad() {
        List<String> list = new ArrayList<>();

        // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나
        // NullPointerException을 던질 수 있다.
        list.addAll(5, null);
    }
}
