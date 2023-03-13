package Chapter06.Item41.ghlim;

import java.io.Serializable;

public class DTO implements Serializable {
    private final int a;
    private final String b;

    public DTO(int a, String b) {
        this.a = a;
        this.b = b;
    }

}
