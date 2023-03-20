package Chapter04.Item23.ghlim;

public class ColorFilter extends Filter{

    private final FilterType filterType = FilterType.COLOR_FILTER;

    private final String color;

    @Override
    void doFilter() {
        isFiltering = true;
        System.out.println("Color Filter : " + color);
    }

    ColorFilter(String color, Integer column) {
        super(column);
        this.color = color;
    }
}
