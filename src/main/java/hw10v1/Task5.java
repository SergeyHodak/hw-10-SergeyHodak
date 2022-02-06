package hw10v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task5 {
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Optional<Stream<T>> first1 = Optional.ofNullable(first); // обертка в опшн
        Optional<Stream<T>> second2 = Optional.ofNullable(second); // обертка в опшн

        List<T> streamFirst = first1.get().collect(Collectors.toList()); // стрим в лист
        List<T> streamSecond = second2.get().collect(Collectors.toList()); // стрим в лист

        int minSize = Math.min(streamFirst.size(), streamSecond.size()); // минимальное количество элементов

        List<T> resultList = new ArrayList<>(); // лист на возврат

        for (int i = 0; i < minSize; i++) { // пробежка по минимальному листу
            resultList.add(streamFirst.get(i)); // добавить на вывод элемент с первого стрима
            resultList.add(streamSecond.get(i)); // добавить на вывод элемент с второго стрима
        }

        return resultList.stream(); // превратить лист в стрим
    }

    public static void main(String[] args) {
        Stream<String> first = Stream.of("Audi", "Tesla", "Life", "Volkswagen", "Mercedes", "Mitsubishi"); // стрим автомобильных марок
        Stream<String> second = Stream.of("0", "1", "2", "3", "4"); // стрим нумерации
        Stream<String> zip = zip(first, second); // вызов метода смешивания
        System.out.println(Arrays.toString(zip.toArray())); // просмотр результата
    }
}