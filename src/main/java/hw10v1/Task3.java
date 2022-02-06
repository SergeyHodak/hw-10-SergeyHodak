package hw10v1;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class Task3<T> {
    public String getSortedNumbers(T[] data) {
        Optional<char[]> dats = Optional.of(Arrays.toString(data).toCharArray()); // превращение в чар в обертке опшн
        Stream<Character> peek = new String(dats.get()).chars().mapToObj(i -> (char) i) //создать поток по каждому символу
                .filter(Character::isDigit) // если этот чар есть цифрой
                .sorted(); // сортировка

        String result = Arrays.toString(peek.toArray()); // превратить поток в массив, а потом в строку
        return result.substring(1, result.length()-1); // отдать без скобок
    }


    public static void main(String[] args) {
        String[] data = new String[] {"1, 2, 0", "4, 5"}; // входной массив
        Task3<String> test = new Task3<>(); // экземпляр класса
        System.out.println(test.getSortedNumbers(data)); // вызов метода
    }
}