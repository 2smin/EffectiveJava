package Chapter03.Item16.smlee;

public class Point {

    private double x;
    private double y;
    public Vector vector;

    public Point(double x, double y) {
        this.minute = 10;
        this.hour =5;
        this.x = x;
        this.y = y;
        vector = new Vector(3,4);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private class Vector{
        public double z;
        public double q;

        public Vector(double z, double q) {
            this.z = z;
            this.q = q;
        }
    }

    public final int hour;
    public final int minute;

    public Point(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }
}
