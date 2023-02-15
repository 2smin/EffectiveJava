package Chapter03.item13.hwkim;

public class Client {
    public static void main(String[] args) {
        Hyunwoo kimHyunwoo = new Hyunwoo(28, 171.2F);
        Hyunwoo parkHyunwoo = kimHyunwoo.clone();
        System.out.println("parkHyunWoo Object : "+parkHyunwoo.toString());
        System.out.println("kimHyunWoo Object : "+kimHyunwoo.toString());
    }
}
