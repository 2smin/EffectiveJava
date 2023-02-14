package Chapter03.Item10.ghlim;

import java.util.Objects;

public class ProteinPowder {

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

        int result = proteinPer;
        result = result + flavor.hashCode();

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
}