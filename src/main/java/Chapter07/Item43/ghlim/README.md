# Item43

## 람다보다는 메소드 참조를 사용하라

- 간결함
    - 람다가 익명클래스보다 더 큰 이점을 가짐
    - 메소드 참조를 하면 더 간결
    
    ```java
    // 람다
    map.merge(key, 1, (count, incr) -> count + incr);
    
    // 메소드 참조
    map.merge(key, 1, Integer::sum);
    ```
    
    - merge 메소드 :  map key가 있으면 value를 대체하고, 없으면 새로 생성
    
- 람다로 할 수 없는 일은 메소드 참조로도 할 수 없음
    - 람다로 작성할 코드를 새로운 메소드에 담은 후, 람다 대신 그 메소드 참조 이용
- 람다가 더 간결한 경우?
    - 메소드와 람다가 같은 클래스에 있을 때
    
    ```java
    // 예시 클래스에 action이 담김
    service.execute(GoshThisClassNameIsHumongous::action);
    
    service.execute(() -> action());
    ```
    
    - Function.identity()도 x → x 를 사용하면 코드 짧고 명확

- 메소드 참조의 유형
    - 정적 메소드를 가리키는 참조
    - 한정적 인스턴스 메소드 참조 (수신 객체 특정)
        - 정적 참조와 비슷
        - 함수 객체가 받는 인수와 참조되는 메소드가 받는 인수가 똑같음
    - 비한정적 인스턴스 메소드 참조 (수신 객체 특정하지 않음)
        - 함수 객체를 적용하는 시점에 수신 객체를 알려줌
        - 수신 객체 전달용 매개변수가 매개변수 목록의 첫번째로 추가
        - 예시) 스트림 파이프라인에서의 매핑과 필터 함수(Item45)
    - 클래스 참조를 가리키는 메소드 참조
        - 팩터리 객체로 사용됨
    - 배열 생성자를 가리키는 메소드 참조
    
    | 메소드 참조 유형 | 예 | 같은 기능을 하는 람다 |
    | --- | --- | --- |
    | 정적 | Integer::parseInt | str -> Integer.parseInt(str) |
    | 한정적(인스턴스) | Instant.now()::isAfter | Instant then = Instant.now();
    t -> then.isAfter(t) |
    | 비한정적(인스턴스) | String::toLowerCase | str -> str.toLowerCase() |
    | 클래스 생성자 | TreeMap<K,V>::new | () -> new TreeMap<K,V> |
    | 배열 생성자 | int[]::new | len -> new int[len] |