package Chapter02.item08;

public class Client {

    public static void main(String[] args) {

        //gc를 해주어야만 close를 호출할 수 있다 (gc 해도 언제 해줄지는 모름)
        new Room(11);
//        System.gc();

        //try with resource는 자동적으로 close를 호출해준다 (autoclosable을 구현한 객체만)
        try(Room room2 = new Room(10)){
            System.out.println("room2 : " + room2);
        }catch (Exception e){

        }
    }

}
