package Chapter06.Item39.hwkim;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MultiExceptionTest1 {
    Class<? extends Exception>[] value();
}
