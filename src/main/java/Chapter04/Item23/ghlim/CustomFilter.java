package Chapter04.Item23.ghlim;

public class CustomFilter extends Filter{

    private final FilterType filterType = FilterType.CUSTOM_FILTER;

    final private String operator;

    final private String value;

    @Override
    void doFilter() {
        isFiltering = true;
        System.out.println("Custom Filter : " + operator + " " + value);
    }

    CustomFilter(String operator, String value, Integer column) {
        super(column);
        this.operator = operator;
        this.value = value;
    }


}
