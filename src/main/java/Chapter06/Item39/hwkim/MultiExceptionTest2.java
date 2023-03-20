package Chapter06.Item39.hwkim;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface MultiExceptionTest2 {
    Class<? extends Throwable> value();
}

