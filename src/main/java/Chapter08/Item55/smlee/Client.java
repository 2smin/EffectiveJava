package Chapter08.Item55.smlee;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Stream;

public class Client {

    public static <E extends Comparable<E>> E max1(Collection<E> c) {
        if(c.isEmpty()){
            throw new IllegalArgumentException("empty collections");
        }

        E result = null;
        for(E e : c){
            if(result == null || e.compareTo(result) > 0){
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }

    public static <E extends Comparable<E>> Optional<E> max2(Collection<E> c){
        if(c.isEmpty()){
            return Optional.empty();
        }

        E result = null;
        for(E e : c){
            if(result == null || e.compareTo(result) > 0){
                result = Objects.requireNonNull(e);
            }
        }
        return Optional.of(result);
    }

    public static <E extends Comparable<E>> Optional<E> max3(Collection<E> c){
        return c.stream().max(Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();

        //기본값 설정
        String maxWord1 = max3(words).orElse("no word");
        //예외 던지기 (예외 안던지는게 좋다면서?)
        String maxWord2 = max3(words).orElseThrow(InvalidParameterException::new);
        //항상 값이 채워져있다고 가정 (없으면 예외 발생)
        String maxWor3 = max3(words).get();
    }

    public static void printParentProcess(){
        Optional<ProcessHandle> parentProcess = ProcessHandle.current().parent();
        //기본 출력 코드
        System.out.println("parent PID: " +
                (parentProcess.isPresent() ? String.valueOf((parentProcess.get().pid())) : "N/A"));
        //Optional의 map 사용
        System.out.println(
                parentProcess.map(h -> String.valueOf(h.pid())).orElse("N/A")
        );



    }

    public static void streamOfOptional(Collection<Optional> collections){
        Stream<Optional> optionalStream = collections.stream();
        optionalStream.filter(Optional::isPresent).map(Optional::get);
        optionalStream.flatMap(Optional::stream);
    }
}
