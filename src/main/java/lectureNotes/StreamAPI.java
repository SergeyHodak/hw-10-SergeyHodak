package lectureNotes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class StreamAPI {
}
/*
    Stream API#
    Stream API - это набор классов и интерфейсов в Java, позволяющих писать обработку данных в функциональном стиле.
    В этом случае мы представляем программу как набор данных, к которым поочередно применяются определенные операции.
    Этот набор данных называется потоком.

    ВНИМАНИЕ
    Не путайте потоки из Stream API из потоками ввода-вывода (IO). Это разные сущности, хотя и называются одинаково.

    Класс Stream#
    Базовый класс в Stream API - это интерфейс Stream из пакета java.util.stream. Этот интерфейс представляет собой
    поток данных, который можно получить несколькими способами. Один из самых простых вариантов - использовать метод
    stream() из интерфейса Collection. Чаще всего это делают для списков List:
*/
class Test11 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("John", "Bill");
        Stream<String> nameStream = names.stream();
        System.out.println(nameStream);
    }
}
/*
    В примере выше мы создали коллекцию names. Дальше из этой коллекции мы получили поток nameStream.

    Промежуточные методы#
    Элементы потока можно дальше обрабатывать, вызывая у потока промежуточные методы, которые принимают функциональные
    интерфейсы (обычно передают просто лямбды). Каждый такой промежуточный метод делает определенное действие над каждым
    элементом потока, и возвращает новый поток. Таким образом, мы можем писать цепочки действий, и выход каждого
    действия - это вход следующего действия. Давайте разберем некоторые промежуточные методы.

    Метод filter#
    Этот метод оставляет в потоке лишь те элементы, которые удовлетворяют написанному условию. Например, если у нас
    есть список имен, и мы хотим оставить в этом списке лишь имена, в которых есть буква J, то это можно записать
    следующим образом:
*/
class Test12 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("John", "Bill");
        Stream<String> nameStream = names.stream().filter((name) -> name.contains("J"));

        System.out.println(Arrays.toString(nameStream.toArray()));
    }
}
/*
    В потоке nameStream в коде выше теперь будет храниться лишь один элемент - John. Строка Bill не удовлетворяет
    условию name.contains("J"), и поэтому не попадет в результирующий поток.

    Вот другой пример, показывающий как можно из списка чисел оставить лишь четные:
*/
class Test13 {
    public static void main(String[] args) {
        Stream<Integer> even =
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                        .stream()
                        .filter(number -> number%2 == 0);

        System.out.println(Arrays.toString(even.toArray()));
    }
}

/*
    В переменной even останутся лишь числа 2, 4, 6, 8, 10. Остальные не пройдут фильтр.

    Метод map#
    Этот метод позволяет изменить каждый элемент потока. Например, вот так можно умножить все числа на 2:
*/
class Test14 {
    public static void main(String[] args) {
        Stream<Integer> numbers =
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                        .stream()
                        .map(number -> number * 2);

        System.out.println(Arrays.toString(numbers.toArray()));
    }
}
/*

    Метод distinct#
    Этот метод удаляет дубли элементов (сравнение идет методом equals()).
*/
class Test15 {
    public static void main(String[] args) {
        Stream<Integer> numbers =
                Arrays.asList(2, 2, 3)
                        .stream()
                        .distinct();

        System.out.println(Arrays.toString(numbers.toArray()));
    }
}
/*
    В примере выше в потоке numbers останется два элемента, 2 и 3. Один дубль (число 2) выбросится из потока.

    Метод sorted#
    Метод sorted() сортирует поток элементов, которые реализуют интерфейс Comparable:
*/
class Test16 {
    public static void main(String[] args) {
        Stream<Integer> numbers =
                Arrays.asList(1, 3, 2, 4, 5)
                        .stream()
                        .sorted();

        System.out.println(Arrays.toString(numbers.toArray()));
    }
}
/*
    В потоке numbers при дальнейшей обработке элементы будут идти в следующем порядке - 1, 2, 3, 4, 5. Есть также
    перегруженная версия этого метода, принимающая интерфейс Comparator для другого порядка сортировки.

    Метод limit#
    Этот метод обрезает поток до указанного числа элементов:
*/
class Test17 {
    public static void main(String[] args) {
        Stream<Integer> numbers =
                Arrays.asList(1, 3, 2, 4, 5)
                        .stream()
                        .limit(1);

        System.out.println(Arrays.toString(numbers.toArray()));
    }
}
/*
    В потоке выше останется лишь элемент 1, остальные отбросятся.

    Метод skip#
    Метод пропускает указанное число элементов от начала потока:
*/
class Test18 {
    public static void main(String[] args) {
        Stream<Integer> numbers =
                Arrays.asList(1, 3, 2, 4, 5)
                        .stream()
                        .skip(3);

        System.out.println(Arrays.toString(numbers.toArray()));
    }
}
/*
    В потоке выше останутся лишь элементы 4 и 5, первые 3 элемента пропустятся.

    Цепочка промежуточных методов#
    Поскольку каждый промежуточный метод тоже возвращает Stream, можно строить цепочки вызовов. Например, у нас есть
    список имен. Мы хотим:
        выбросить из этого списка все короткие (меньше 4-х символов имена);
        добавить к каждому имени префикс Mr/Ms;
        отсортировать полученный результат по алфавиту;
        оставить лишь первые два элемента.
    Вот как это можно записать с помощью Stream API:
*/
class Test19 {
    public static void main(String[] args) {
        Stream<String> names =
                Arrays.asList("John", "Bill", "Max", "Alex")
                        .stream()
                        .filter(name -> name.length() > 3)
                        .map(name -> "Mr/Ms " + name)
                        .sorted()
                        .limit(2);

        System.out.println(Arrays.toString(names.toArray()));
    }
}
/*
    В примере выше после вызова каждого промежуточного метода поток данных изменялся. В конце мы получим результат:
        [Mr/Ms Alex, Mr/Ms Bill]

    ПОЛЕЗНО
    Обычно Stream API используется именно так, как комбинация методов. Каждый метод делает свою часть работы, и после
    выполнения последнего метода мы получаем готовый результат работы.

    Терминальные методы#
    После вызова промежуточных методов мы получаем объект типа Stream, в котором есть нужные нам данные. Но дальше нам
    нужно как-то преобразовать объект Stream в другой формат, чтобы продолжить работу дальше. Например, получить
    коллекцию объектов (объект типа List), или найти минимальное-максимальное значение. Для этого есть понятие
    терминального метода. Когда мы вызываем такой метод у потока, то уже не возвращается Stream, а возвращается
    другой объект - например, коллекция. Рассмотрим некоторые наиболее часто используемые терминальные методы.

    Метод collect#
    Один из наиболее часто используемых терминальных методов - это метод collect(). Он предназначен для собрания всех
    элементов потока в какой-то другой объект. Типично этот объект - коллекция. Метод collect() принимает интерфейс
    типа Collector, и позволяет указать, как именно собирать элементы. Уже есть готовые коллекторы, которые позволяют
    собирать потоки в коллекции типа List, Set, Map. Например, вот так выглядит цельная завершенная программа по
    фильтрации имен, как в примере выше:
*/
class StreamTest {
    public static void main(String[] args) {
        Stream<String> names =
                Arrays.asList("John", "Bill", "Max", "Alex")
                        .stream()
                        .filter(name -> name.length() > 3)
                        .map(name -> "Mr/Ms " + name)
                        .sorted()
                        .limit(2);

        List<String> filteredNames = names.collect(Collectors.toList());

        System.out.println(filteredNames);
    }
}
/*
    Если выполнить эту программу, то в консоль выведется строка [Mr/Ms Alex, Mr/Ms Bill].

    Метод count#
    Если нам не нужно знать, какие именно элементы есть в стриме, а достаточно узнать лишь их количество, то для этого
    есть терминальный метод count():
*/
class StreamTest2 {
    public static void main(String[] args) {
        Stream<String> names =
                Arrays.asList("John", "Bill", "Max", "Alex")
                        .stream()
                        .filter(name -> name.length() > 3)
                        .map(name -> "Mr/Ms " + name)
                        .sorted()
                        .limit(2);

        //2
        System.out.println(names.count());
    }
}
/*

    Минимальный и максимальный элементы#
    Для поиска минимального и максимального элементов есть методы min() и max(). Они принимают на вход компаратор
    (объект типа Comparator), и возвращают подходящий наименьший или наибольший объект. Например, вот так можно найти
    наибольшее из чисел:
*/
class StreamTest3 {
    public static void main(String[] args) {
        int minValue =
                Arrays
                .asList(3, 10, 4, 1, 17, 8)
                .stream()
                .min((a, b) -> a - b)
                .get();

        System.out.println(minValue);
    }
}
/*
    В консоль код выше выведет 1. Обратите внимание, что в примере выше мы использовали в качестве компаратора тоже
    лямбда-выражение - (a, b) -> a - b.

    Как еще можно получить Stream#
    У класса Stream есть статический метод of(), который принимает массив или varargs параметры, и возвращает поток:
        Stream<Integer> intStream = Stream.of(1, 2, 5);

    Дополнительный материал#
    Тема Stream API довольно обширная. В этом конспекте мы не разбирали все методы, а лишь основные. Цель конспекта -
    дать понимание, как работают потоки и в каких случаях их стоит использовать. Настоятельно рекомендуем почитать вот
    эти материалы для самостоятельного изучения:
        https://habr.com/ru/post/437038/
        https://habr.com/ru/company/luxoft/blog/270383/
    Хорошей стратегией при возникновении вопросов вида "А можно ли с помощью Stream API сделать такую-то возможность"
    читать официальную документацию - https://docs.oracle.com/javase/8/docs/api/?java/util/stream/Stream.html
 */