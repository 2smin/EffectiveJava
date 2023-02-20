package Chapter04.Item23.ghlim;


enum FilterType {CUSTOM_FILTER, COLOR_FILTER};
public abstract class Filter {

    final private Integer column;

    protected FilterType filterType;
    protected boolean isFiltering;

    Filter(Integer column) {
        this.column = column;
    }
    abstract void doFilter();

    void clearFilter() {
        isFiltering = false;
    }

}
