package Chapter05.Item33.hwkim;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AsSubclassUtil {
    static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName) {
        Class<?> annotationType = null; // 비한정적 타입 토큰
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }
}
