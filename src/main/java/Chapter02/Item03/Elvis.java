package Chapter02.Item03;

public class Elvis {

//    Elvis01 elvis01 = new Elvis01(); // 오류 발생

    Elvis01 elvis01 = Elvis01.INSTANCE; //static으로 선언된 멤버 변수 접근하여 인스턴스 가져옴

//    Elvis02 elvis02 = new Elvis02(); // 오류 발생

    Elvis02 elvis02 = Elvis02.getInstance(); // getInstance라는 static 메소드를 통해 인스턴스 가져옴

//    Elvis03 elvis03 = new Elvis03();  // 오류 발생 - enum이라 생성 불가

    Elvis03 elvis03 = Elvis03.INSTANCE;
}
