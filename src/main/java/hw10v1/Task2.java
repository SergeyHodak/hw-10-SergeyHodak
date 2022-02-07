package hw10v1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class Task2 {
    public String[] reverseSortByUpperCase(String[] data) {
        Optional<String[]> dats = Optional.of(data); // обертка в опшн
        Stream<String> result = Stream.of(dats.get()) // стрим берущий по одному объекту
                .sorted(Comparator.reverseOrder()) // реверсная сортировка
                .map(String::toUpperCase); // возвести в верхний регистр

        return result.toArray(String[]::new); // превратить стрим в массив строк
    }

    public static void main(String[] args) {
        Task2 test = new Task2(); // экземпляр класса
        String[] names = new String[]{"Ken", "Mari", "Bill", "Mark", "Steve", "Jo", "Le"}; // входной массив
        String[] result = test.reverseSortByUpperCase(names); // вызов метода
        System.out.println(Arrays.toString(result)); // печать в консоль результата работы метода
    }
}