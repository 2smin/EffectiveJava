package Chapter09.Item65.smlee;

public class Americano implements Coffee{
    @Override
    public String extractCoffee() {
        return "extract americano";
    }

    @Override
    public String addCream(String cream) {
        return "누가 아메리카노에 크림을 올려먹어";
    }
}
