package Chapter06.Item37.smlee;

import java.util.Set;

public class Plant {

    enum LifeCycle { ANNUAL, PRENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }


}
