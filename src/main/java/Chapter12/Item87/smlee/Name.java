package Chapter12.Item87.smlee;

import java.io.Serializable;

public class Name implements Serializable {

    /**
     * not null
     * @serial
     */
    private final String lastName;

    /**
     * not null
     * @serial
     */
    private final String middleName;

    /**
     * not null
     * @serial
     */
    private final String firstName;

    public Name(String lastName, String middleName, String firstName) {
        this.lastName = lastName;
        this.middleName = middleName;
        this.firstName = firstName;
    }
}
