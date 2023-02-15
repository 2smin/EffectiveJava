package Chapter04.Item17.smlee;

import java.math.BigInteger;

public class Client {

    public static void main(String[] args) {

        BigInteger bigInteger = new BigInteger( "5");

        BigInteger negateBigInteger = bigInteger.negate();

        System.out.println(bigInteger);
        System.out.println(negateBigInteger);


    }

    public static BigInteger getSafeInstance(BigInteger o){
        return o.getClass() == BigInteger.class ? o : new BigInteger(o.toByteArray());
    }
}
