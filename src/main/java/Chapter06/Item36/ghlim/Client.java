package Chapter06.Item36.ghlim;

import java.util.EnumSet;
import java.util.Set;

public class Client {

    public static void main(String[] args) {
        Text emphasis_text = new Text();

        Text cancel_text = new Text();

        emphasis_text.applyStyles(EnumSet.of(Text.Style.BOLD));

        cancel_text.applyStyles(EnumSet.of(Text.Style.STRIKETHROUGH));
    }
}