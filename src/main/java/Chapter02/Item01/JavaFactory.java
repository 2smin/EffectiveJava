package Chapter02.Item01;

public class JavaFactory {

    private JavaFactory javaFactory;

    private JavaFactory JavaFactory(){
        this.javaFactory = new JavaFactory();
        return this.javaFactory;
    }

    public JavaFactory getJavaFactory(boolean flag){
        if(this.javaFactory != null) {
            return this.javaFactory;
        }

        if(flag){
            return this.javaFactory;
        }else{
            return new ExtendedFactory();
        }
    }

    public JavaFactory getExtendedJavaFactory(boolean flag){
        if(this.javaFactory != null) {
            return this.javaFactory;
        }

        if(flag){
            return this.javaFactory;
        }else{
            return new ExtendedFactory();
        }
    }

}
