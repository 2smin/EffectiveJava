package Chapter04.Item20.smlee.SkeletalImpl;

public abstract class AbstractTranslator implements Translator{

    //번역기를 만든다고 할때
    //ㄴkeletal이 되어야 하는건 번역로직
    //인터페이스가 되어야 하는건 기타 일반 기능

    private String message;
    private String returnMessage;

    @Override
    public void translate(String message) {
        insertMessage(message);
        returnMessage = process(message);
        returnMessage();
    }

    protected abstract String process(String message);

    @Override
    public void insertMessage(String message) {
        this.message = message;
    }

    @Override
    public void returnMessage() {
        System.out.println(this.returnMessage);
    }
}
