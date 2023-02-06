package Chapter01.Item03;

public class Elvis01 {

    // 싱글턴 첫번째 방법 - public static final 멤버변수
    public static final Elvis01 INSTANCE = new Elvis01();
    private Elvis01() {}


}
