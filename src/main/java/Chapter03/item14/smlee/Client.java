package Chapter03.item14.smlee;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Client {

    public static void main(String[] args) {

        Set<BigDecimal> bigDecimals1 = new HashSet<>();
        bigDecimals1.add(new BigDecimal("1.0"));
        bigDecimals1.add(new BigDecimal("1.00"));

        Set<BigDecimal> bigDecimals2 = new TreeSet<>();
        bigDecimals2.add(new BigDecimal("1.0"));
        bigDecimals2.add(new BigDecimal("1.00"));

        System.out.println(bigDecimals1.size());
        System.out.println(bigDecimals2.size());
    }
}
