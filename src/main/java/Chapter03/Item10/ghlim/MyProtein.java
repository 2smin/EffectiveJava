package Chapter03.Item10.ghlim;

import java.awt.*;

public class MyProtein {

    // 상속 대신 컴포지션 활용
    private ProteinPowder proteinPowder;

    private String name;
    private String type;
    private Color color;

    public MyProtein(ProteinPowder proteinPowder, String name, String type, Color color) {
        this.proteinPowder = proteinPowder;
        this.name = name;
        this.type = type;
        this.color = color;
    }

    // 복사 생성자
    public MyProtein(MyProtein that) {
        proteinPowder = that.proteinPowder;
        name = that.name;
        type = that.type;
        color = that.color;
    }

    // 복사 팩토리
    public static MyProtein newInstance(MyProtein that) {
        return new MyProtein(that.proteinPowder, that.name, that.type, that.color);
    }

    //MP의 protein을 반환 (protein의 정보만 주기 위해)
    public ProteinPowder asProteinPowder() {
        return proteinPowder;
    }

    @Override
    public boolean equals(Object obj) {
        // type check
        if(!(obj instanceof MyProtein)) return false;

        MyProtein mp = (MyProtein) obj;

        return mp.proteinPowder.equals(proteinPowder)
                && mp.name.equals(name)
                && mp.type.equals(type)
                && mp.color.equals(color);
    }

    @Override
    public int hashCode() {
        int result = proteinPowder.hashCode();

        result = result + 31 * name.hashCode();
        result = result + 31 * type.hashCode();
        result = result + 31 * color.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MyProtein{" +
                "proteinPowder=" + proteinPowder +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", color=#" + Integer.toHexString(color.getRGB()) +
                '}';
    }
}
