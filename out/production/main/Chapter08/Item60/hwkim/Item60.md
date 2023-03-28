# Item 60

# 정확한 답이 필요하다면 float와 double은 피하라

float와 double은 이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 **근사치**로 계산하도록 세심하게 설계되었다. 따라서 정확한 결과가 필요할 때는 사용해서는 안된다.

float와 double 타입은 금융 관련 계산과는 맞지 않는다. 0.1 혹은 10의 음의 거듭제곱수 등을 표현할 수 없기 때문이다. 예를 들어 주머니에 1.03 달러가 있었는데, 그 중 42 센트를 썼다고 해보자 남은돈을 구하기 위해 다음과 같이 어설프게 작성할 수 있다.

```java
public static void main(String[] args){
    System.out.println(1.03 - 0.42);
}
```

안타깝게도 이 코드는 0.6100000000000001를 출력한다. (답 : 0.61)

혹은 1달러가 있었는데 10센트 사탕 9개를 샀다고 해보자

```java
public static void main(String[] args){
    System.out.println(1.00 - 9 * 0.10);
}
```

0.09999999999999998을 출력한다. (답 : 0.1)

결과를 반환하기 전 반올림을 하면 해결할 수 있다고 생각할 수 있으나 이는 잘못된 생각이다.

1달러가 주어져 있고 10센트부터 1달러 까지 10센트씩 가격이 비싼 사탕이 1개씩 있다고 해보자

(10, 20, 30, … , )

10센트 짜리부터 1씩 산다면 사탕을 몇개 살 수 있을까? 이를 구하는 어설픈 코드는 다음과 같다.

```java
public class GreedyCandy {
    public static void main(String[] args){
        double funds = 1.00;
        int itemsBought = 0;
        for(double price = 0.10; funds >= price; price += 0.10){
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈 (달러) : " + funds);
    }
}
```

정답은 4개 구입에 잔돈 0달러이다. 그러나 실제 결과는 3개 구입에 잔돈 0.3999999999로 출력된다.

위와 같은 금융 계산에는 BigDecimal, int 혹은 long을 사용해야 한다.

```java
public class GreedyCandy {
    public static void main(String[] args){
        final BigDecimal TEN_CENTS = new BigDecimal(".10");

        BigDecimal funds = new BigDecimal("1.00");
        int itemsBought = 0;
        for(BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0; price = price.add(TEN_CENTS)){
            funds = funds.subtract(price);
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈 (달러) : " + funds);
    }
}
```

위와 같이 수정하면 올바른 결과가 출력된다. 그러나 기본타입보다 쓰기가 훨씬 불편하고 느리다. 단발성 계산이라면 느리다는 문제는 무시할 수 있지만, 쓰기 불편하다는 점은 못내 아쉬울 것이다. int나 long을 쓸 수도 있다. 그럴 경우 다룰 수 있는 값의 크기가 제한되고 소수점을 직접 관리해야한다.

```java
public class GreedyCandy {
    public static void main(String[] args){
        int itemsBought = 0;
        int funds = 100;
        for(int price = 10; funds >= price; price += 10){
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈 (달러) : " + funds);
    }
}
```