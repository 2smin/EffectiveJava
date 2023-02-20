package Chapter04.Item24.ghlim;

import java.util.Stack;

public class Calculator {

    public static enum operator { PLUS, MINUS };

    private Stack<String> stack = new Stack<>();

    public innerClass createInnerClass() {
        return new innerClass(1);
    }
    public class innerClass {
        final Integer t;
        public innerClass(Integer t) {
            this.t = t;
        }
        public Stack<String> getStack() {
            // 바깥 참조 가능
            return Calculator.this.stack;
        }


    }
    public Calculator() {

    }
}
