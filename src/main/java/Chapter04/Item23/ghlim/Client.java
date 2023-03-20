package Chapter04.Item23.ghlim;

public class Client {


    public static void main(String[] args) {

        Filter[] filters = new Filter[2];

        filters[0] = new CustomFilter("equals","abc",0);
        filters[1] = new ColorFilter("#FF0000",1);

        filters[0].doFilter();
        filters[1].doFilter();
    }
}
