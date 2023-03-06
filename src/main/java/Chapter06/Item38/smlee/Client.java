package Chapter06.Item38.smlee;

import java.util.Arrays;
import java.util.Collection;

public class Client {

    public static void main(String[] args) {
        System.out.println(BasicOperation.MINUS.apply(3,2));

        test(BasicOperation.class,4,2);
        test(ExtendedOperation.class,4,2);


        test(Arrays.asList(ExtendedOperation.values()),5,3);

        Operation[] opList = new Operation[]{BasicOperation.MINUS, ExtendedOperation.EXP};
        test(Arrays.asList(opList),4,6);
    }

    //enum class의 모든 원소 순환하며 실행
    private static <T extends Enum<T> & Operation> void test(Class<T> enumType, double x, double y){

        for(Operation op : enumType.getEnumConstants()){
            System.out.println(op.apply(x,y));
        }
    }

    private static void test(Collection<? extends Operation> enumList, double x, double y){
        for(Operation op : enumList){
            System.out.println(op.apply(x,y));
        }
    }
}
