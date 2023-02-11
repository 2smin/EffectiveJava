package Chapter02.Item03;

public class Elvis02 {

    // 싱글턴 두번째 방법 - 팩터리 메서드로 private static final 멤버 변수 호출
    private static final Elvis02 INSTANCE = new Elvis02();
    private Elvis02() {}
    public static Elvis02 getInstance() {return INSTANCE;}

    // 싱글턴임을 보장
    private Object readResolve() {
        return INSTANCE;
    }

}
