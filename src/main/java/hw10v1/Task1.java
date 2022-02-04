package hw10v1;

import java.util.*;
import java.util.stream.Collectors;

public class Task1 {
    public String oddNames(String[] data) {
        // создать карту ключами у которой будут индексы, а значение у ключей будет значение с входного массива под этим индексом
        Map<Integer, String> dat = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            dat.put(i, data[i]);
        }

        StringBuilder result = new StringBuilder(); // строка для вывода
        List<Map.Entry<Integer, String>> collect = dat.entrySet()
                .stream()
                .filter(p -> p.getKey() % 2 != 0) // нужен нечетный ключ
                .peek(name -> result.append(name.getKey()).append(". ").append(name.getValue()).append(", "))
                .collect(Collectors.toList());
        return result.substring(0, result.length()-2); // удаляя последнюю запятую и пробел
    }

    public static void main(String[] args) {
        Task1 test = new Task1();
        System.out.println(test.oddNames(new String[]{"Ken", "Mari", "Bill", "Mark", "Steve", "Jo", "le"}));
    }
}