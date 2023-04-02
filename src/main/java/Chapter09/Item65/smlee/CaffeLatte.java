package Chapter09.Item65.smlee;

public class CaffeLatte implements Coffee{

    @Override
    public String extractCoffee() {
        return "extract Caffe Latte";
    }

    @Override
    public String addCream(String cream) {
        return "휘핑크림 올려드릴게요";
    }
}
