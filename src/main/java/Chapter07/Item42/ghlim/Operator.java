package Chapter07.Item42.ghlim;

import java.util.function.DoubleBinaryOperator;

public enum Operator {
    PLUS ("+", (x, y) -> x + y),
    MINUS ("-", (x, y) -> x - y),
    TIMES ("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);
    private final String symbol;
    // 함수형 인터페이스를 멤버 필드로 가짐
    private final DoubleBinaryOperator op;

    Operator(String symbol, DoubleBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }
    @Override public String toString() { return symbol; }
    public double apply(double x, double y) {
        return op.applyAsDouble(x, y);
    }
}
