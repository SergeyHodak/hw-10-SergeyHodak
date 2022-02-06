package hw10v1;

import java.util.*;
import java.util.stream.Collectors;

public class Task1 {
    public String oddNames(String[] data) {
        Map<Integer, String> dataMap = new HashMap<>(); // карта индекс-значение
        for (int i = 0; i < data.length; i++) { // пробежка по входному массиву
            dataMap.put(i, data[i]); // добавить в карту ключ-значение
        }

        StringBuilder result = new StringBuilder(); // строка для вывода
        Optional<Map<Integer, String>> dataOptional = Optional.of(dataMap); // обертка в опшн
        dataOptional.get().entrySet()
                .stream()
                .filter(p -> p.getKey() % 2 != 0) // нужен нечетный ключ
                .peek(name -> result.append(name.getKey()).append(". ").append(name.getValue()).append(", "))
                .collect(Collectors.toList());
        return result.substring(0, result.length()-2); // удаляя последнюю запятую и пробел
    }

    public static void main(String[] args) {
        Task1 test = new Task1(); // экземпляр класса
        String[] data = new String[]{"Ken", "Mari", "Bill", "Mark", "Steve", "Jo", "le"}; // входной массив
        String result = test.oddNames(data); // вызов метода
        System.out.println(result); // посмотреть результат работы метода
    }
}