package hw10v1;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task3<T> {
    public String getSortedNumbers(T[] data) {
        Optional<String> data1 = Optional.of(Arrays.toString(data)); // превращение в строку, в обертке опшн
        String data2 = data1.get().replaceAll(",", ""); // удались все запятые
        String data3 = data2.substring(1, data2.length()-1); // убрать первый и последний символы '[' ']'
        String[] data4 = data3.split(" "); // разбить строку на массив слов по пробелу
        Integer[] data5 = new Integer[data4.length]; // создать новый массив интов
        for (int i = 0; i < data5.length; i++) { // пробежка по новому массиву
            data5[i] = Integer.parseInt(data4[i]); // заполнить новый массив
        }
        return Stream.of(data5) // стрим из массива цифровых значений
                .sorted() // сортировка по возрастанию
                .map(Object::toString) // превратить инт в строку
                .collect(Collectors.joining(", ")); // выдать результат в виде строки, разделитель ", "
    }

    public String getSortedNumbersV2(T[] data) {
        Optional<T[]> data1 = Optional.of(data); // обертка в опшн
        Stream<Integer> peek = Stream.of(data1.get())
                .map(Object::toString) // превратить в строку
                .flatMap((p) -> Arrays.stream(p.split(","))) // разделить по запятой
                .map(p -> Integer.parseInt(p.strip())) // удалив со всех сторон пробелы и невидимые знаки, превратить в инт
                .sorted(); // сортировка
        String result = Arrays.toString(peek.toArray()); // поток превратить в массив, а массив в строку
        return result.substring(1, result.length()-1); // удалить скобки массива, и отдать результат
    }

    public static void main(String[] args) {
        String[] data = new String[] {"1, 2, 0", "4, 10, 5, 11"}; // входной массив
        Task3<String> test = new Task3<>(); // экземпляр класса

        System.out.println(test.getSortedNumbers(data)); // вызов метода первой версии

        System.out.println(test.getSortedNumbersV2(data)); // вызов метода второй версии
    }
}