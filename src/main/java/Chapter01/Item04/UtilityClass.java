package Chapter01.Item04;

public class UtilityClass {

    // utility class의 생성자는 private으로 만들어 인스턴스, 상속 불가능하게
    private UtilityClass() {
        throw new AssertionError();
    }
}
