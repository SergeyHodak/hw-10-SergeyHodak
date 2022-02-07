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
        String collect = Stream.of(data5) // стрим из массива цифровых значений
                .sorted() // сортировка по возрастанию
                .map(i -> i.toString()) // превратить инт в строку
                .collect(Collectors.joining(", ")); // разделитель между значениями
        return collect; // выдать результат в виде строки
    }

    public static void main(String[] args) {
        String[] data = new String[] {"1, 2, 0", "4, 10, 5, 11"}; // входной массив
        Task3<String> test = new Task3<>(); // экземпляр класса
        System.out.println(test.getSortedNumbers(data)); // вызов метода
    }
}