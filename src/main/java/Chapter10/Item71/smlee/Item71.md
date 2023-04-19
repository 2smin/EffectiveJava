# Item71

# 필요 없는 검사 예외 사용은 피하라

검사예외는 발생한 문제를 프로그래머가 처리할 수 있다. 물론 과하게 사용하면 해당 메서드를 사용하는 쪽에서 catch 블록을 두고 처리하던 던지던 해야한다. 코드가 좀 지저분해질수 있다.

api를 제대로 사용해도 발생할 수 있는 문제이거나, 개발자가 적절한 조치를 취할수 있다면 이정도는 괜츈하다. 하지만 둘 다 아니라면 그냥 비검사 예외를 사용하자.

### 검사예외의 부담

단 하나의 검사 예외만 던질때가 가장 부담스럽다. 이미 다른 검사 예외가 있는 경우에 또 검사 예외가 추가되는 거라면 catch 블록 하나만 추가하면 되지만, 단 하나라면 try catch를 새로 만들어야 한다.

### 검사 예외 회피하기

- 옵셔널 반환
    
    적절한 결과 타입을 담은 옵셔널을 반환하면 된다. 검사 예외를 던지는 대신 빈 옵셔널을 던져도 된다.
    
    이 방식은 예외가 발생한 이유를 알려주는 부가 정보를 담을 수 없다.
    
- 예외를 던지는 메서드를 2개로 쪼개서 비검사 예외로 바꾼다.
    
    ```java
    public static void main(String[] args) {
    
        int code = 0;
        if(isPermitted(code)){
    action();
        }else{
            //exception handling
        }
    }
    
    public static boolean isPermitted(int code){
    
        if(code > 10){
            return true;
        }else{
            return false;
        }
    }
    
    public static void action(){
        //do something
    }
    ```
    
    만약 이 메서드가 무조건 성공한다는걸 알거나, 실패 시 thread를 중단하길 원하면 isPermitted를 타지 않고 바로 action을 때려도 된다.
    
    ### 주의할점
    
    isPermitted 메서드는 상태 검사 메서드에 해당한다. 여러 스레드가 동시에 접근하거나 외부 요인에 의해 상태가 변화된다면 위처럼 쓰면 안된다. isPermitted와 action 사이에서 객체 상태가 변할 수 있기때문이다.
    
    또한 두 메서드에서 중복 작업을 수행한다면 위 코드는 적절하지 않을 수 있다.