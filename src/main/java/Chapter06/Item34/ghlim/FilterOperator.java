package Chapter06.Item34.ghlim;

public enum FilterOperator {
    EQUALS,
    NOT_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    CONTAINS(DataType.NUMBER),
    STARTS_WITH(DataType.STRING),
    ENDS_WITH(DataType.STRING),
    NOT_STARTS_WITH(DataType.STRING),
    NOT_ENDS_WITH(DataType.STRING);


    //멤버 필드
    private final DataType dataType;

    FilterOperator(DataType dataType) {
        this.dataType = dataType;
    }

    FilterOperator() {
        this.dataType = DataType.NUMBER;
    }

    void doFilter(String val) {
        dataType.filter(val);
    }

    enum DataType{
        STRING {
            void filter(String val) {
                System.out.println(" LIKE " + val);
            }
        },
        NUMBER {
            @Override
            void filter(String val) {
                System.out.println(" = ");
            }
        };

        abstract void filter(String val);
    }
}
