package Chapter03.Item12.hwkim;

public class PhoneNumber {
    private String displayString;
    private String phoneType;
    private String uri;

    PhoneNumber(String phoneType, String uri){
        this.phoneType = phoneType;
        this.uri = uri;
        this.displayString = phoneType + " : " + uri;
    }

    @Override
    public String toString(){
        return this.displayString;
    }

    public String getPhoneType(){
        return this.phoneType;
    }

    public String getUri(){
        return this.uri;
    }
}
