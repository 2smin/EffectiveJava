package Chapter11.Item83.smlee;

public class FieldInitializer {

    //인스턴스 필드 일반적 초기화
    private final FieldType field = computeFieldValue();

    private static FieldType computeFieldValue(){
        return new FieldType();
    }

    //인스턴스 필드 지연 초기화
    private FieldType field2;

    private synchronized  FieldType getField(){
        if(field2 == null){
            field2 = computeFieldValue();
        }

        return field2;
    }

    //static 필드 초기화 (holder 관용구 사용)
    private static class FieldHolder{
        static final FieldType field = computeFieldValue();
    }

    private static FieldType getStaticField() { return  FieldHolder.field; }

    //인스턴스 필드 이중 검사
    private volatile FieldType field3;

    private FieldType getField3(){

        //초기화 된 상황에서 필드를 딱 한번만 읽도록 보장한다. 왜 필요하지?
        FieldType result = field3;
        if(result != null){
            return result;
        }

        synchronized (this){ //필드가 없으므로 초기화 해야한다. sync를 해줘야 함
            if(field3 == null){
                field3 = computeFieldValue();
            }
            return field3;
        }
    }

    //중복 초기화가 상관 없는 단일 검사 (중복 초기화니까 sync도 필요 없다)
    private volatile FieldType field4;
    private FieldType getField4(){
        FieldType result = field4;

        if(result == null){
            field4 = result = computeFieldValue();
        }
        return result;
    }
}
