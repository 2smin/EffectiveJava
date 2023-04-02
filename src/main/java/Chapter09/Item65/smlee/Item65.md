# Item65

# 리플렉션보다는 인터페이스를 사용하라.

리플렉션 기능을 이용하면 프로그램에서 임의의 클래스에 접근할 수 있다.

Class 객체가 주어지면 해당 클래스의 생성자, 메서드., 필드 인스턴스를 가져올 수 있고, 해당 인스턴스들로부터 클래스의 멤버 이름, 필드 타입, 메서드 시그니처 등을 가져올 수 있다.

각 인스턴스를 이용해 연결된 실제 생성자, 메서드. 필드를 조작할 수 있다. 또한 리플렉션을 이용하면 컴파일 당시에 존재하지 않던 클래스도 이용할 수 있다.

### 리플렉션의 단점

- 컴파일 타임 타입 검사가 주는 이점을 누릴 수 없다.
    - 타입 검사나 예외 검사를 컴파일 시점에 할 수 없기 때문에, 리플렉션을 이용하여 존재하지 않거나 접근할 수 없는 메서드를 호출하면 런타임 오류가 발생한다.
- 코드가 지저분해고 장황해진다.
- 성능이 떨어진다.
    - 리플렉션을 통한 메서드 호출은 일반 메서드 호출보다 훨씬 느리다.

### 리플렉션은 아주 제한된 형태로만 사용하자.

컴파일 타임에 이용할 수 없는 클래스를 사용하는 경우가 있지만, 그런 경우에도 컴파일 타임에 적절한 인터페이스나 상위 클래스를 이용할 수 있다. 

리플렉션은 인스턴스 생성에만 쓰고 만들어진 인스턴스는 인터페이스나 상위 클래스로 참조하자.

```java
public static void main(String[] args) {

        Class<? extends Coffee> instance = null;
        Coffee coffeeInstance = null;
        try{
            instance = (Class<? extends Coffee>) Class.forName("Chapter09.Item65.smlee.CaffeLatte");
            coffeeInstance = instance.getDeclaredConstructor().newInstance();
        }catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException |
                InvocationTargetException e){
            e.printStackTrace();
        }

        System.out.println(coffeeInstance.extractCoffee());
        System.out.println(coffeeInstance.addCream("whiteCream"));

    }
```

위처럼 reflection을 통해서 instance를 생성하되, 인터페이스 혹은 상위 클래스를 참조하게 만들어서 method 호출은 빠르게 실행할 수 있다.

### 위 예제에서의 단점

위처럼 제한적으로 리플렉션을 사용해도, 어느정도 단점이 존재한다.

- 총 6개의 예외를 던진다.
    - instance를 생성하는 과정에서 예외가 발생할 수 있다. 리플렉션을 쓰지 않으면 컴파일 타임에 잡아낼 수 있는 예외다.
- 코드가 길다.
    - 리플렉션이 아니면 1줄로 끝날 코드가 엄청 길어진다.

### 리플렉션의 활용도

리플렉션은 런타임에 존재하지 않을 수도 있는 다른 클래스, 메서드, 필드와의 의존성을 관리할 때 적합하다. 이 기법은 여러개 존재하는 외부 패키지를 다룰 때 유용하다.

가장 오래된 버젼만 지원하도록 컴파일 한 후, 이후 버전의 클래스와 메서드 등은 리플렉션으로 접근하는 방식이다.