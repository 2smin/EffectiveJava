# Item78

# 공유 중인 가변 데이터는 동기화 해서 사용하라

### Synchronized

해당 메서드나 블록을 한번에 한 스레드씩 수행하도록 보장한다. 일반적으로는 한 스레드가 변경하는 중인 상태의 값을 다른 스레드가 보지 못하도록 막는다.

1. 한 객체가 일관된 상태를 가지고 생성된다.
2. 이 객체에 접근하는 메서드는 그 객체에 락을 건다.
3. 락을 건 메서드는 객체의 상태를 확인하고 수정한다.

동기화를 제대로 사용하면 어떤 메서드도 이 객체의 상태가 일관되지 않은 순간을 볼 수 없다. (여러 스레드가 경합이 들어와서 맘대로 못바꾼다.)

### 동기화의 중요한 기능

위처럼 스레드간 경합을 방지하는 기능 외에도 중요한 기능이 하나 더 있다. 동기화 없이는 특정 스레드에서 변화시킨 값을 다른 스레드가 확인하지 못할 수 있다. 

*동기화 없이는 직전 스레드가 변경시킨 값이 다음 스레드에 언제 반영될 지 모른다!*

### long 과 double 외의 변수 읽고 쓰기

jvm에서는 데이터를 4byte 단위로 처리하기 때문에, int와 int 보다 작은 데이터 타입들은 하나의 명령어로 처리할 수 있다. 하나의 명령어는 더이상 나눌 수 없는 최소 단위이기 때문에, 명령 사이에 다른 thread가 끼어들 여지가 없다. 이를 원자적 (atomic) 이라고 한다.

```java
private static boolean stepRequested;

public static void main(String[] args) throws InterruptedException{

    Thread backgroundThread = new Thread(() -> {
        int i = 0;
        while(!stepRequested){
            i++;
        }
    });

    backgroundThread.start();

    TimeUnit.SECONDS.sleep(1);

stepRequested= true;
}
```

위 코드를 수행하면 1초 뒤 종료되지 않고, 언제 종료될지 모른다.

### Synchronized 의 cache memory 동기화.

Synchronized는 단순히 다른 스레드가 들어오지 못하게 막는것 뿐만 아니라, 블록 내부에서 호출하는 필드들의 cache memory의 동기화를 수행한다.

위 코드에서는 개별 Thread의 cache 메모리에 stepRequest 변수가 올라와 있는 상태이다. main thread가 값을 변경하여 1차 memory에 값을 변경하지만, background threads는 해당 값을 본인의 cache에서 가져오기 때문에 변경된 값을 알지 못한다. cache 메모리가 1차 메모리와 자동 동기화 되어야 변경된 값을 얻을 수 있다.

```java
private static booleanstepRequested;
private static synchronized void stop(){stepRequested= true;}
private static synchronized boolean getRequested(){returnstepRequested;}

public static void main(String[] args) throws InterruptedException{

    Thread backgroundThread = new Thread(() -> {
        int i = 0;
        while(!getRequested()){
            i++;
        }
    });

    backgroundThread.start();

    TimeUnit.SECONDS.sleep(1);

stop();
}
```

이런 식으로 코드를 작성해야, stepResult에 접근할 때 동기화 된 값을 가져올 수 있다.

### volatile 사용

synchronized보다 속도가 좀 더 빠른 volatile을 사용하면 항상 cache가 아닌 1차메모리에서 값을 읽어오기 때문에 최신의 data를 읽을 수 있다.

```java
private static volatile booleanstopRequest;

public static void main(String[] args) {

    Thread backgroundThread = new Thread(() -> {
        int i = 0;
        while(!stopRequest){
            i++;
        }
    });

stopRequest= true;
}
```

### volatile 사용 주의

volatile은 최신의 데이터만 보장하지만, thread 경합을 방지하지는 않는다.

```java
public static volatile int nextNumber= 1;
public static int incrementNumber(){
    return nextNumber++;
}
```

위 코드는 volatile에 int 형이므로 thread 경합없이 최신의 데이터를 얻어올 수 있을것 같지만 아니다.

nextNumber++은 2개의 명령으로 구성되기 때문에 (읽고, 쓰기) 두 명령 사이에 다른 스레드가 값을 읽어온다면 이전 쓰레드의 쓰기 직전 값에 접근할 수 있다. 이런 오류를 안전 실패라고 한다.

위 경우는 synchronized를 붙임으로써 해결한다. 대신 volatile은 제거해도 된다.

### Atomic 사용

java.util.concurrent에서 제공하는 atomic 패키지는 원자성을 제공한다. 여기서 제공하는 클래스를 사용하면 락 없이도 스레드 안전하게 코딩할 수 있다. 동기화된 최신의 값까지 가져올 수 있다.

```java
private static final AtomicIntegernextNumber= new AtomicInteger();

public static int incrementNumber(){
    returnnextNumber.getAndIncrement();
}
```

### 문제를 피하는 가장 좋은 방법

애초에 가변 데이터를 공유하지 않는게 좋다. 불변 데이터만 공유하거나 아무것도 공유하지 말자. 가변 데이터는 단일 스레드에서만 사용하자. 

한 스레드가 데이터를 다 수정한 후에는 다른 스레드에 공유할 때 해당 객체에서 공유하는 부분만 동기화하자. 그럼 그 객체를 다시 수정할 때 다른 스레드는 동기화 없이 자유롭게 값을 읽어갈 수 있다. 이런 객체를 사실상 불변이라고 하며, 다른 스레드에게 객체를 건네는 행위를 안전 발행이라고 한다.