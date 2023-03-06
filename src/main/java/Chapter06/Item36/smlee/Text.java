package Chapter06.Item36.smlee;

import java.util.EnumSet;
import java.util.Set;

public class Text {

    public static final byte STYLE_BOLD = 1 << 0;
    public static final byte STYLE_ITALIC = 1 << 1;
    public static final byte STYLE_UNDERLINE = 1 << 2;
    public static final byte STYLE_PADDING = 1 << 3;
    public static final byte STYLE_BLANK = 1 << 4;
    public static final byte STYLE_COLOR = 1 << 5;
    public static final byte STYLE_BACKGROUND = 1 << 6;
    public static final int STYLE_TRIM = 1 << 7;
    public static final int STYLE_EXP = 1 << 8;

    public int style;
    public void applyStyles(int styles){
        this.style = styles;
    }

    public void applyStyles(Set<Text> enumSet){

    }
    public static void main(String[] args) {
        Text text = new Text();
        System.out.println(STYLE_EXP);
        text.applyStyles(STYLE_BOLD|STYLE_ITALIC|STYLE_UNDERLINE|STYLE_PADDING|STYLE_BLANK|STYLE_COLOR|STYLE_BACKGROUND|STYLE_TRIM);
        System.out.println(text.style);
    }
}

