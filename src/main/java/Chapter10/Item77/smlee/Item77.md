# Item77

# 예외를 무시하지 말라

api 설계자가 예외를 던지는 이유는 해당 메서드를 사용할 때 적절한 조치를 취해달라고 말하는 것이다. try catch로 감싼 후 catch 블록에서 아무것도  안하면 예외가 쉽게 무시된다.

### 예외를 무시하지 마라고!

catch 블록을 비워두면 예외가 존재할 이유가 없어진다. 누구도 예외가 발생했음을 알 수 없기 때문에, 최소한 log 라도 찍자…

### 예외를 무시해야 하는 경우

예를 들어 FileInputStream을 닫을 때 예외를 무시할 수 있다. 입력 전용 스트림이므로 파일의 상태를 변경하지 않았으니 복구 할 것이 없으며, 필요한 정보는 다 읽었으니 남은 작업을 중단할 이유도 없다. 혹시나 같은 예외가 자주 발생한다면 조사해보는것이 좋을테니 로그로 남겨도 좋다.

예외를 무시하기로 했으면 catch 블록 안에 그렇게 결정한 이유를 남기고 예외 변수의 이름도 ignored로 바꾸자

```java
if(args.length == 0){
    throw new InvalidParameterException();
}

Queue<String> testQueue = new LinkedList<>();

try{
    testQueue.offer(args[0]);
}catch (ArrayIndexOutOfBoundsException ignored){
    // unreachable code
}
```

비검사 예외든, 검사 예외든 모두 동일하게 적용된다. 어떤 예외든 빈 catch 블럭으로 지나치면 오류를 내재한 채 동작하게 된다. 그러다가 어느순간 프로세스가 죽어버릴 수 있다. 무시하지 않고 최소한 바깥으로 전파되게 놔두어도 디버깅 정보를 남긴채 중단되도록 할 수 있다.