package Chapter03.Item10.ghlim;

import java.util.Objects;

public class ProteinPowder implements Cloneable{

    private int proteinPer;
    private String flavor;

    public ProteinPowder(int proteinPer, String flavor) {
        this.proteinPer = proteinPer;
        this.flavor = flavor;

    }

    // auto generate code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // 리스코프 치환 원칙 위배?
        if (o == null || getClass() != o.getClass()) return false;
        ProteinPowder that = (ProteinPowder) o;
        return proteinPer == that.proteinPer && flavor.equals(that.flavor);
    }

    // auto generate code
    @Override
    public int hashCode() {

        int result = Integer.hashCode(proteinPer);
        result = result + 31 * flavor.hashCode();

        return result;
//        return Objects.hash(proteinPer, flavor);
    }

    @Override
    public String toString() {
        String str = "ProteinPowder{"
                + "proteinPer = " + proteinPer + ", "
                + "flavor = " + flavor + "}";

        return str;
    }

    @Override
    public ProteinPowder clone() throws CloneNotSupportedException{
        try {
            return (ProteinPowder) super.clone();
        } catch (CloneNotSupportedException e) {
         throw new AssertionError();
        }
    }
}
