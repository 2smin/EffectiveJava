package Chapter03.item13.hwkim;

public class Hyunwoo implements Cloneable{
    private final String name = "hyunwoo";
    private int age;
    private float height;

    public Hyunwoo(int age, float height){
        this.age = age;
        this.height = height;
    }

    @Override
    public Hyunwoo clone(){
        try{
            return (Hyunwoo) super.clone();
        }
        catch (CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
}
