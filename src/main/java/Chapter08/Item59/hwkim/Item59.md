# Item 59

# 라이브러리를 익히고 사용하라

무작위 정수 하나를 생성하고 싶다고 해보자. 값의 범위는 0부터 명시한 수 사이다. 아주 흔히 마주치는 문제로, 많은 프로그래머가 다음과 같은 짤막한 메서드를 만들곤 한다.

```java
static Random rnd = new Random();

static int random(int n){
		return Math.abs(rnd.nextInt()) % n;
}
```

괜찮은 듯 보여도 다음과 같은 문제를 내포하고 있다.

- n이 그리 크지 않은 2의 제곱수라면 얼마 지나지 않아 같은 수열이 반복된다.
- n이 2의 제곱수가 아니라면, 몇몇 숫자가 평균적으로 더 자주 반환된다. n 값이 크면 이 현상은 더 두드러진다.
    
    다음 코드는 예시를 위해 책의 저자가 신중히 선택한 범위에서 무작위 수를 백만개 생성한 뒤, 중간값보다 작은게 몇 개인지 출력한다.
    
    ```java
    public class Client {
    
        static Random rnd = new Random();
    
        static int random(int n){
            return Math.abs(rnd.nextInt()) % n;
        }
        public static void main(String[] args){
            int n = 2 * (Integer.MAX_VALUE / 3);
            int low = 0;
            for(int i = 0; i < 1000000; i++){
                if(random(n) < n/2){
                    low++;
                }
            }
            System.out.println(low);
        }
    }
    ```
    
    random 메서드가 이상적으로 동작한다면 약 50만개가 출력되어야 하지만 실제로 돌려보면 666,666에 가까운 값을 얻는다.(test 시 실제로 666,892) 무작위로 생성된 수 중에서 2/3가량이 중간값보다 낮은 쪽으로 쏠린 것이다.
    
- 지정한 범위의 바깥수가 종종 튀어나올 수 있다.
    
    rnd.nextInt()가 반환한 값을 Math.abs를 이용해 음수가 아닌 정수로 매핑하기 때문이다. nextInt()가 Integer.MIN_VALUE를 반환하면 Math.abs도 Integer.MIN_VALUE를 반환하고 나머지 연산자는 음수를 반환해버린다.(n이 2의 제곱수가 아닐경우 시나리오)
    

위와 같은 문제를 해결하기 위해서 의사난수 생성기, 정수론, 2의 보수 계산 등에 조예가 깊어야 한다. 그러나 Random.nextInt(int)가 위와 같은 문제를 모두 해결해 놓았다. 이 분야의 여러 전문가가 잘 동작함을 검증해 주었고 이 라이브러리가 릴리즈 된 후 긴 시간동안 버그가 보고된 적이 없다. 혹시 버그가 있더라도 다음 릴리즈에서 수정될 것이다.

```java
public class Client {

    static Random rnd = new Random();

    static int random(int n){
        return rnd.nextInt(n);
        //return Math.abs(rnd.nextInt()) % n;
    }
    public static void main(String[] args){
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;
        for(int i = 0; i < 1000000; i++){
            if(random(n) < n/2){
                low++;
            }
        }
        System.out.println(low);
    }
}
```

자바 7부터는 ThreadLocalRandom으로 대체하면 Random보다 더 고품질의 무작위 수를 더 빠르게 생성한다.

## 표준 라이브러리의 장점

1. **표준 라이브러리를 사용하면 그 코드를 작성한 전문가의 지식과 여러분보다 앞서 사용한 다른 프로그래머들의 경험을 활용할 수 있다.**
2. 핵심적인 일과 크게 관련이 없는 문제를 해결하느라 시간을 허비하지 않아도 된다. 위의 예시와 같이 무작위 수를 잘 만들어 내기 위해 시간을 허비할 필요없이 라이브러리를 사용하면된다.
3. 따로 노력하지 않아도 성능이 지속해서 개선된다.
4. 기능이 점점 많아지기 때문에 라이브러리를 익히는 것이 좋다.
5. 작성한 코드가 다른사람에게 낯익은 코드가 된다. 자연스럽게 읽고 유지보수하기 쉽고 재활용하기 좋아진다.

위에서 언급한 많은 이점이 있음에도 실상은 많은 프로그래머가 직접 구현한 코드를 사용하고 있다. 이는 아마도 라이브러리에 그런 기능이 있는지 모르기 때문일 것이다. **메이저 릴리즈마다 주목할 만한 수많은 기능이 라이브러리에 추가된다.** 자바는 메이저 릴리즈마다 새로운 기능을 설명하는 웹페이지를 공시하는데, 한번쯤 읽어볼만 하다.

```java
https://www.oracle.com/java/technologies/javase/10-relnote-issues.html#NewFeature
자바 릴리즈 노트 예시
```

자바 프로그래머라면 적어도 java.lang, java.util, java.io와 그 하위 패키지들에는 익숙해져야한다.