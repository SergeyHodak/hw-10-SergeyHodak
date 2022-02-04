package hw10v1;

import java.util.Arrays;
import java.util.stream.Stream;

public class Task2 {
    public String[] reverseSortByUpperCase(String[] data) {
        Stream<String> result = Stream.of(data)
                .sorted((s1, anotherString) -> anotherString.compareTo(s1)) // реверсная сортировка
                .map(String::toUpperCase); // возвести в верхний регистр

        return result.toArray(String[]::new); // превратить стрим в массив строк
    }

    public static void main(String[] args) {
        Task2 test = new Task2();
        String[] names = new String[]{"Ken", "Mari", "Bill", "Mark", "Steve", "Jo", "Le"};
        String[] result = test.reverseSortByUpperCase(names);
        System.out.println(Arrays.toString(result));
    }
}