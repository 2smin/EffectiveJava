# Item87

# 커스텀 직렬화 형태를 고려하라

클래스가 serializable을 구현하고 기본 직렬화 형태를 사용한다면 다음 릴리즈 때 버리려 한 현재의 구현에 영원히 발이 묶이게 된다.

### 예시

기존 릴리즈에서 일단 돌아가도록 구현하고, 다음 릴리즈에서 seriazable을 변경시킬 때, 기본 직렬화 형태를 버릴 수 없다. 왜 못버리지???

### 먼저 고민해보고 괜찮다고 판단될때만 기본 직렬화 형태를 사용

일반적으로 직접 설계하더라도 기본 직렬화 형태와 거의 같은 결과가 나올 경우에만 기본 형태를 쓰자.

기본 직렬화 형태는 그 객체가 포함한 데이터들과 접근 가능한 모든 객체를 담아낸다. 연결된 위상까지 기술한다.

### 물리표현과 논리 내용이 같다면 기본 직렬화 사용

다음 코드에서는 기본 직렬화 형태를 사용해도 된다.

```java
public class Name implements Serializable {
		/**
		     * not null
		     *@serial
		*/
		private final String lastName;
		
		/**
		     * not null
		     *@serial
		*/
		private final String middleName;
		
		/**
		     * not null
		     *@serial
		*/
		private final String firstName;

}
```

논리적으로도 Name은 위 코드와 동일하다. 이런 경우는 그냥 기본 직렬화 형태를 써도 된다.

### 물리표현과 논리 내용이 다르다면 직접 구현

```java
public class StringList implements Serializable {

    private int size = 0;
    private Entry head = null;
    private static class Entry implements  Serializable {
        String data;
        Entry next;
        Entry previous;
    }
}
```

논리적으로는 data를 entry로 연결시켜서 문자열을 표현했다. 물리적으로는 문자열을 entry (이중연결 리스트) 로 연결했다. 이 클래스에 기본 직렬화 형태를 사용하면 노드의 양방향 정보까지 모두 기록되고 각 노드의 정보까지 모두 기록된다.

### 발생하는 문제점

1. 공개 api가 현재의 내부 표현 방식에 영구히 묶인다.
    
    private 클래스인 Entry가 공개 api가 되며, 다음 릴리즈에서 방식을 바꾸더라도 여전히 기존 표현 방식을 처리해야한다. (기존 버젼 미지원 하는게 아닌 이상??)
    
2. 너무 많은 공간을 차지한다.
    
    위 예시의 직렬화 형태는 연결 리스트의 모든 엔트리 (노드) 정보까지 다 가지고 있다. 이런 정보는 직렬화 형태에 포함할 가치가 없고, 저장하거나 전송 속도만 느려진다.
    
3. 시간이 너무 많이 걸린다.
    
    직렬화 로직은 객체 그래프의 위상에 대한 정보가 없으니 그래프를 직접 순회해야한다. 위 예시는 다음 참조를 따라가는것만으로는 해결 되긴 한다.
    
4. stack overflow
    
    기본 직렬화 과정은 객체 그래프를 재귀 순환하는데, 이 작업은 중간 크기의 객체 그래프에서도 stack overflow가 발생할 수 있다.
    

### 합리적인 직렬화 형태

```java
public class StringListEnhance implements Serializable {

    private transient int size = 0;
    private transient Entry head = null;
    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    public final void add(String s){}

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size);

        for(Entry e = head; e != null; e = e.next){
            stream.writeObject(e.data);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int numElement = size;
        for(int i=0; i < numElement; i++){
            add((String)stream.readObject());
        }
    }
}
```

transient를 통해서 직렬화에 포함되지 않을 필드를 지정하고, read/write object를 통해 entry로 연결된 data만 쓰고, 읽어온다.

StringList의 필드가 모두 transient라도 defaultWriteObject와 defaultReadObject는 호출해야한다. 그래야 향후 릴리즈에서 transient가 아닌 필드가 추가되어도 코드 수정 없이 읽어올 수 있다. 위 코드는 직렬화 한 후 역직렬화하면 불변식까지 제대로 복원해낸다.

### Transient 주의

기본 직렬화를 사용하면 transient 필드들은 0이나 null 등 기본값으로 초기화된다. read 한 후 원하는 값으로 복원하는 코드를 넣어야 한다.

### Synchronized도 꼭 써주자

직렬화에는 synchronized도 적용해주어야 한다. 안그러면 여러 스레드가 동시에 직렬화하면서 데드락이 걸릴 수 있다.

### 직렬 버젼 UID 명시

어떤 직렬화 형태를 띄든 직렬화 가능 클래스에 모두 직렬화 버젼 uid를 명시해야한다. 이러면 잠재적 호환성 문제도 사라지며 성능도 조금 빨라진다.

```java
private static final long serialVersionUID = 132415L; //임의의 값
```

클래스 일련 번호를 생성해주는 serialver 유틸리티를 사용해도 되며, 아무 값이나 선택해도 된다. 꼭 고유할 필요도 없다.

직렬버젼UID가 없는 클래스를 구버젼 직렬화된 인스턴스와 (uid가 있는?) 호환성을 유지한 채 수정하고 싶으면 구버젼에서 자동 생성된 값을 사용하자. 

기본 버젼 클래스와 호환성을 끊고 싶으면 단순히 직렬 버젼 UID를 바꾸면 된다. 이러면 기존 버젼의 직렬화 인스턴스를 역직렬화 할 때 invalidClassException이 발생한다.