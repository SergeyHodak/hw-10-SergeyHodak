package hw10v1;

import java.util.Arrays;
import java.util.stream.Stream;

public class Task3<T> {
    public String getSortedNumbers(T[] data) {
        char[] dats = Arrays.toString(data).toCharArray(); // превратить входные данные в массив чаров

        Stream<Character> peek = new String(dats).chars().mapToObj(i -> (char) i) //создать поток по каждому символу
                .filter(s -> Character.isDigit(s)) // если этот чар есть цифрой
                .sorted(); // сортировка

        String result = Arrays.toString(peek.toArray()); // превратить поток в массив, а потом в строку
        return result.substring(1, result.length()-1); // отдать без скобок
    }

    public static void main(String[] args) {
        String[] data = new String[] {"1, 2, 0", "4, 5"};
        Task3<String> test = new Task3<>();
        System.out.println(test.getSortedNumbers(data));
    }
}