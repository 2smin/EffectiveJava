package Chapter01.Item02;

public class CocaCola {

    NutritionFacts nutritionFacts;

    String name;

    // 필수 매개변수인 servingSize, servings는 Builder 매개변수로 두기
    // 선택 매개변수는 이어서 set 해줌 (setter들은 계속 builder 반환 - fluent API / method chaining)
    // 마지막 build() 호출 시, 완성된 NutritionFacts 인스턴스 반환
    private CocaCola() {
        this.name = "CocaCola";
        this.nutritionFacts = new NutritionFacts.Builder(240,8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();

    }

}
