package Chapter03.item14.smlee;

import java.security.InvalidParameterException;
import java.util.Comparator;

public class Credit implements Comparable<Credit>{

    public static void main(String[] args) {

        Credit credit1 = new Credit("C-");
        Credit credit2 = new Credit("C0");

        System.out.println(credit1.compareTo(credit2));


        System.out.println(LEVEL_COMPARATOR_LAMBDA.compare(credit1,credit2));
    }

    public String level;
    public String subLevel;

    public Credit(String level) {
        this.level = level.split("")[0];
        this.subLevel = level.split("")[1];
    }

    @Override
    public int compareTo(Credit crd) {

        int result = -String.CASE_INSENSITIVE_ORDER.compare(this.level,crd.level);
        if(result == 0){
            result = LEVEL_COMPARATOR.compare(this.subLevel, crd.subLevel);
        }
        return result;
    }

    private static Comparator<String> LEVEL_COMPARATOR = new Comparator<String>() {
        private int indexing(String sublevel) {
            if (sublevel.equals("-")) {
                return 1;
            } else if (sublevel.equals("0")) {
                return 2;
            } else if (sublevel.equals("+")) {
                return 3;
            }
            throw new InvalidParameterException();
        }

        @Override
        public int compare(String o1, String o2) {
            int a = indexing(o1);
            int b = indexing(o2);

            return Integer.compare(a,b);
        }
    };

    private static int indexing(String sublevel) {
        if (sublevel.equals("-")) {
            return 1;
        } else if (sublevel.equals("0")) {
            return 2;
        } else if (sublevel.equals("+")) {
            return 3;
        }
        throw new InvalidParameterException();
    }

    private static Comparator<Credit> LEVEL_COMPARATOR_LAMBDA =
            Comparator.comparing((Credit crd) -> crd.level).reversed()
                    .thenComparing(crd -> Credit.indexing(crd.subLevel));
}

