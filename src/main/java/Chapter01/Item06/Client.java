package Chapter01.Item06;

public class Client {

    public static void main(String[] args) {

        long start1 = System.currentTimeMillis();
        Instance instance1 = Instance.getInstance(6);
        Instance instance2 = Instance.getInstance(7);
        long end1 = System.currentTimeMillis();

        System.out.println(instance1.hashCode());
        System.out.println(instance2.hashCode());
        System.out.println("time : " + (end1-start1));

        long start2 = System.currentTimeMillis();
        Instance instance3 = Instance.caching(5);
        Instance instance4 = Instance.caching(5);
        long end2 = System.currentTimeMillis();

        System.out.println("time : " + (end2-start2));
        System.out.println(instance3.hashCode());
        System.out.println(instance4.hashCode());

    }

}
