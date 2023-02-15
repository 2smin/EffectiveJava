package Chapter04.Item17.smlee;

public class Complex {

    private final double x;
    private final double y;

    private Complex(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    };

    //방어적 복사
    public Complex plus(Complex c){
        return new Complex(x + c.x, y + c.y);
    }

    public static Complex getInstance(double x, double y){
        return new Complex(x,y);
    }

}
