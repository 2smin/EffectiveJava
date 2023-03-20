package Chapter04.Item19.smlee;

public class TestClonableExtended extends TestClonable{

    @Override
    public void overridMe() {
        System.out.println("extended overrideMe");
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        TestClonableExtended testClonableExtended = new TestClonableExtended();
        testClonableExtended.clone();
    }
}
