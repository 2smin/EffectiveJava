package Chapter04.Item19.smlee;

public class TestClonable implements Cloneable{

    private String name;
    private int age;

    public void overridMe(){
        System.out.println("test override");
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        overridMe();
        return super.clone();
    }
}
