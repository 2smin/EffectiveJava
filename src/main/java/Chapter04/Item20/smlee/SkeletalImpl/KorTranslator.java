package Chapter04.Item20.smlee.SkeletalImpl;

public class KorTranslator extends AbstractTranslator{

    @Override
    protected String process(String message) {

        if(message.equals("안녕하세요")){
            return "hi";
        }
        return "translate error";
    }
}
