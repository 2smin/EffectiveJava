# Item85

## 자바 직렬화의 대안을 찾으라

- 자바 직렬화의 장점
    - 분산 객체를 쉽게 만들 수 있다
- 단점
    - 보이지 않는 생성자
    - API와 구현 사이의 모호해진 경계
    - 잠재적인 정확성 문제
    - 성능
    - 보안
    - 유지보수
- 자바 직렬화의 근본적인 문제
    - 공격범위가 너무 넓고 지속적으로 더 넓어져 방어하기 어려움
    - ObjectInputStream의 readObject는 클래스패스 안의 거의 모든 타입의 객체를 만들어 낼 수 있음
    - 그 타입들의 코드 전체가 공격 범위에 들어감
    - 공격에 대비하도록 작성한다 해도 여전히 취약할 수 있음
- 직렬화와 관련된 공격
    - 원격 코드 실행 (remote code execution, RCE)
    - 서비스 거부 (denial-of-service, DOS)
- 간단한 역직렬화 폭탄
    
    
    ```java
    static byte[] bomb() {
    	Set<Object> root = new HashSet<>();
    	Set<Object> s1 = root;
    	Set<Object> s2 = new HashSet<>();
    
    	for (int i = 0; i < 100; i++) {
    		Set<Object> t1 = new HashSet<>();
    		Set<Object> t2 = new HashSet<>();
    		t1.add("foo"); // Make t1 unequal to t2
    		s1.add(t1); s1.add(t2);
    		s2.add(t1); s2.add(t2);
    		s1 = t1;
    		s2 = t2;
    	}
    	return serialize(root); // Method omitted for brevity
    }
    ```
    
    - 201개의 HashSet 인스턴스 및 각각은 3개 이하의 객체 참조를 가짐
    - 스트림의 전체 크기는 5744 바이트이지만 역직렬화는 시간이 무한으로 소요
        - HashSet 인스턴스를 역직렬화하려면 그 원소들의 해시코드를 계산해야 하기 때문
    - 역직렬화가 영원히 계속된다는 것도 문제이지만, 무엇인가 잘못되었다는 신호를 주지 않는 것이 더 큰 문제
- 회피 방법
    - 아무것도 역직렬화하지 않는 것
    - 객체와 바이트시퀀스를 변환해주는 다른 매커니즘 사용
        - cross-platform structured-data representation
    - JSON과 프로토콜 버퍼가 있음
- 레거시 시스템 때문에 직렬화를 사용해야 할 때는?
    - **신뢰할 수 없는 데이터는 절대 역직렬화하지 않는 것**
- 직렬화를 피할 수 없고 역직렬화한 데이터가 안전한지 완전히 확신할 수 없다면
    - 객체 역직렬화 필터링 사용
        - java 9 에 추가 됨 - java.io.ObjectInputFilter
    - 데이터 스트림이 역직렬화되기 전에 필터를 설치하는 기능
    - 클래스 단위로, 특정 클래스를 받아들이거나 거부할 수 있음
    - 기본 수용 모드 : 블랙리스트에 기록된 클래스를 거부
    - 기본 거부 모드 : 화이트리스트에 있는 클래스만 수용
    - 화이트리스트 방식이 추천됨
    - SWAT - 화이트리스트를 자동으로 생성해주는 도구
    - 직렬화 폭탄은 걸러내지 못함