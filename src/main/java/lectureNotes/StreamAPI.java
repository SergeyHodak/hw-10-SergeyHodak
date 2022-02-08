package lectureNotes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
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

/*
    TODO первая статья https://habr.com/ru/post/437038/
    Bodichka 22 января 2019 в 09:59

    Перевод руководства по Stream API от Benjamin Winterberg#
    Привет, Хабр! Представляю вашему вниманию перевод статьи "Java 8 Stream Tutorial". Это руководство, основанное на
    примерах кода, представляет всесторонний обзор потоков в Java 8. При моем первом знакомстве с Stream API, я был
    озадачен названием, поскольку оно очень созвучно с InputStream и OutputStream из пакета java.io; Однако потоки в
    Java 8 — нечто абсолютно другое. Потоки представляют собой монады, которые играют важную роль в развитии
    функционального программирования в Java. В функциональном программировании монада является структурой, которая
    представляет вычисление в виде цепи последовательных шагов. Тип и структура монады определяют цепочку операций, в
    нашем случае — последовательность методов с встроенными функциями заданного типа. Это руководство научит работе с
    потоками и покажет как обращаться с различными методами, доступными в Stream API. Мы разберем порядок выполнения
    операций и проследим как последовательность методов в цепочке влияет на производительность. Познакомимся с мощными
    методами Stream API, такими как reduce, collect и flatMap. В конце руководства уделим внимание параллельной работе
    с потоками. Если вы не чувствуете себя свободно в работе с лямбда-выражениями, функциональными интерфейсами и
    ссылочными методами, вам будет полезно ознакомиться с моим руководством по нововведениям в Java 8 (перевод на Хабре),
    а после этого вернуться к изучению потоков.

    Как работают потоки#
    Поток представляет последовательность элементов и предоставляет различные методы для произведения вычислений над
    данными элементами:
*/
class Test20 {
    public static void main(String[] args) {
        List<String> myList =
                Arrays.asList("a1", "a2", "b1", "c2", "c1");

        myList
                .stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
        // C1
        // C2
    }
}
/*
    Методы потоков бывают промежуточными (intermediate) и терминальными (terminal). Промежуточные методы возвращают
    поток, что позволяет последовательно вызывать множество таких методов. Терминальные методы либо не возвращают
    значения (void) либо возвращают результат типа отличного от потока. В вышеприведенном примере методы filter, map и
    sorted являются промежуточными, а forEach — терминальным. Для ознакомления с полным списком доступных методов потока
    обратитесь к документации. Такая цепочка потоковых операций также известна как конвейер операций (operation pipeline).

    Большинство методов из Stream API принимают в качестве параметров лямбда-выражения, функциональный интерфейс,
    описывающие конкретное поведение метода. Большая их часть должна одновременно быть невмешивающейся (non-interfering)
    и не запоминающей состояние (stateless). Что же это означает?

    Метод является невмешивающимся (non-interfering), если он не изменяет исходные данные, лежащие в основе потока.
    Например, в вышеприведенном примере никакие лямбда-выражения не вносят изменений в списочный массив myList. Метод
    является не запоминающим состояние (stateless), если порядок выполнения операции определен. Например, ни одно
    лямбда-выражение из примера не зависит от изменяемых переменных или состояний внешнего пространства, которые могли
    бы меняться во время выполнения.

    Различные виды потоков#
    Потоки могут быть созданы из различных исходных данных, главным образом из коллекций. Списки (Lists) и множества
    (Sets) поддерживают новые методы stream() и parllelStream() для создания последовательных и параллельных потоков.
    Параллельные потоки способны работать в многопоточном режиме (on multiple threads) и будут рассмотрены в конце
    руководства. А пока рассмотрим последовательные потоки:
*/
class Test21 {
    public static void main(String[] args) {
        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);  // a1
    }
}
/*
    Здесь вызов метода stream() для списка возвращает обычный объект потока. Однако для работы с потоком вовсе не
    обязательно создавать коллекцию:
*/
class Test22 {
    public static void main(String[] args) {
        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);  // a1
    }
}
/*
    Просто используйте Stream.of() для создания потока из нескольких объектных ссылок.

    Помимо обычных потоков объектов Java 8 располагает специальными типами потоков для работы с примитивными типами:
    int, long, double. Как можно догадаться это IntStream, LongStream, DoubleStream. Потоки IntStream могут заменить
    обычные циклы for(;;) используя IntStream.range():
*/
class Test23 {
    public static void main(String[] args) {
        IntStream.range(1, 4)
                .forEach(System.out::println);
        // 1
        // 2
        // 3
    }
}
/*
    Все эти потоки для работы с примитивными типами работают так же как и обычные потоки объектов за исключением
    следующего:
        Потоки примитивов используют специальные лямбда-выражения. Например, IntFunction вместо Function,
         или IntPredicate вместо Predicate.
        Потоки примитивов поддерживают дополнительные терминальные методы: sum() и average()
*/
class Test24 {
    public static void main(String[] args) {
        Arrays.stream(new int[] {1, 2, 3})
                .map(n -> 2 * n + 1)
                .average() // это типа взять среднее арифметике
                .ifPresent(System.out::println);  // 5.0
    }
}
/*
    Иногда полезно превратить поток объектов в поток примитивов или наоборот. Для этой цели потоки объектов
    поддерживают специальные методы: mapToInt(), mapToLong(), mapToDouble():
*/
class Test25 {
    public static void main(String[] args) {
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1)) // взять со второго символа до конца
                .mapToInt(Integer::parseInt) // превратить в поток интов
                .max() // взять максимальное
                .ifPresent(System.out::println);  // 3
    }
}
/*
    Потоки примитивов могут быть преобразованы в потоки объектов посредством вызова mapToObj():
*/
class Test26 {
    public static void main(String[] args) {
        IntStream.range(1, 4) // от до
                .mapToObj(i -> "a" + i) // превратить в поток объектов
                .forEach(System.out::println); // печатать в консоль поток

        // a1
        // a2
        // a3
    }
}
/*
    В следующем примере поток из чисел с плавающей точкой отображается в поток целочисленных чисел и затем отображается
    в поток объектов:
*/
class Test27 {
    public static void main(String[] args) {
        Stream.of(1.0, 2.0, 3.0) // поток из чисел с плавающей точкой
                .mapToInt(Double::intValue) // отображается в поток целочисленных чисел
                .mapToObj(i -> "a" + i) // затем отображается в поток объектов
                .forEach(System.out::println); // печатать в консоль поток
        // a1
        // a2
        // a3
    }
}
/*

    Порядок выполнения#
    Сейчас, когда мы узнали как создавать различные потоки и как с ними работать, погрузимся глубже и рассмотрим, как
    потоковые операции выглядят под капотом. Важная характеристика промежуточных методов — их лень. В этом примере
    отсутствует терминальный метод:
*/
class Test28 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });
    }
}
/*
    При выполнении этого фрагмента кода ничего не будет выведено в консоль. А все потому, что промежуточные методы
    выполняются только при наличии терминального метода. Давайте расширим пример добавлением терминального метода forEach:
*/
class Test29 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }
}
/*
    Выполнение этого фрагмента кода приводит к выводу на консоль следующего результата:
        filter:  d2
        forEach: d2
        filter:  a2
        forEach: a2
        filter:  b1
        forEach: b1
        filter:  b3
        forEach: b3
        filter:  c
        forEach: c

    Порядок, в котором расположены результаты, может удивить. Можно наивно ожидать, что методы будут выполняться
    “горизонтально”: один за другим для всех элементов потока. Однако вместо этого элемент двигается по цепочке
    “вертикально”. Сначала первая строка “d2” проходит через метод filter затем через forEach и только тогда, после
    прохода первого элемента через всю цепочку методов, следующий элемент начинает обрабатываться.

    Принимая во внимание такое поведение, можно уменьшить фактическое количество операций:
*/
class Test30 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });

        // map:      d2
        // anyMatch: D2
        // map:      a2
        // anyMatch: A2
    }
}
/*
    Метод anyMatch вернет true, как только предикат будет применен к входящему элементу. В данном случае это второй
    элемент последовательности — “A2”. Соответственно, благодаря “вертикальному” выполнению цепочки потока map будет
    вызван только дважды. Таким образом вместо отображения всех элементов потока, map будет вызван минимально возможное
    количество раз.

    Почему последовательность имеет значение#
    Следующий пример состоит из двух промежуточных методов map и filter и терминального метода forEach. Рассмотрим
    как выполняются данные методы:
*/
class Test31 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach: " + s));

        // map:     d2
        // filter:  D2
        // map:     a2
        // filter:  A2
        // forEach: A2
        // map:     b1
        // filter:  B1
        // map:     b3
        // filter:  B3
        // map:     c
        // filter:  C
    }
}
/*
    Нетрудно догадаться, что оба метода map и filter вызываются 5 раз за время выполнения — по разу для каждого
    элемента исходной коллекции, в то время как forEach вызывается только единожды — для элемента прошедшего фильтр.

    Можно существенно сократить число операций, если изменить порядок вызовов методов, поместив filter на первое место:
*/
class Test32 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));

        // filter:  d2
        // filter:  a2
        // map:     a2
        // forEach: A2
        // filter:  b1
        // filter:  b3
        // filter:  c
    }
}
/*
    Сейчас map вызывается только один раз. При большом количестве входящих элементов будем наблюдать ощутимый прирост
    производительности. Помните об этом составляя сложные цепочки методов.

    Расширим вышеприведенный пример, добавив дополнительную операцию сортировки — метод sorted:
*/
class Test33 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
    }
}
/*
    Сортировка — это специальный вид промежуточных операций. Это так называемая операция с запоминанием состояния
    (stateful), поскольку для сортировки коллекции необходимо учитывать ее состояния на протяжении всей операции.

    В результате выполнения данного кода получаем следующий вывод в консоль:
        sort:    a2; d2
        sort:    b1; a2
        sort:    b1; d2
        sort:    b1; a2
        sort:    b3; b1
        sort:    b3; d2
        sort:    c; b3
        sort:    c; d2
        filter:  a2
        map:     a2
        forEach: A2
        filter:  b1
        filter:  b3
        filter:  c
        filter:  d2

    Сперва производится сортировка всей коллекции целиком. Другими словами метод sorted выполняется “горизонтально”.
    В данном случае sorted вызывается 8 раз для нескольких комбинаций из элементов входящей коллекции.

    Еще раз оптимизируем выполнение данного кода посредством изменения порядка вызовов методов в цепочке:
*/
class Test34 {
    public static void main(String[] args) {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .sorted((s1, s2) -> {
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));

        // filter:  d2
        // filter:  a2
        // filter:  b1
        // filter:  b3
        // filter:  c
        // map:     a2
        // forEach: A2
    }
}
/*
    В этом примере sorted вообще не вызывается т.к. filter сокращает входную коллекцию до одного элемента. В случае с
    большими входящими данными производительность выиграет существенно.

    Повторное использование потоков#
    В Java 8 потоки не могут быть использованы повторно. После вызова любого терминального метода поток завершается:
*/
class Test35 {
    public static void main(String[] args) {
        Stream<String> stream =
                Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);    // ok
        stream.noneMatch(s -> true);   // exception
    }
}
/*
    Вызов noneMatch после anyMatch в одном потоке приводит к следующей исключительной ситуации:
    java.lang.IllegalStateException: stream has already been operated upon or closed
        at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:229)
        at java.util.stream.ReferencePipeline.noneMatch(ReferencePipeline.java:459)
        at com.winterbe.java8.Streams5.test7(Streams5.java:38)
        at com.winterbe.java8.Streams5.main(Streams5.java:28)

    Для преодоления этого ограничения следует создавать новый поток для каждого терминального метода.
    Например, можно создать поставщика (supplier) для конструктора нового потока, в котором будут установлены все
    промежуточные методы:
*/
class Test36 {
    public static void main(String[] args) {
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok
    }
}
/*
    Каждый вызов метода get создает новый поток, в котором можно безопасно вызвать желаемый терминальный метод.

    Продвинутые методы#
    Потоки поддерживают большое количество различных методов. Мы уже ознакомились с наиболее важными методами. Чтобы
    самостоятельно ознакомиться с остальными, обратитесь к документации. А сейчас погрузимся еще глубже в более сложные
    методы: collect, flatMap и reduce.

    Большая часть примеров кода из этого раздела обращается к следующему фрагменту кода для демонстрации работы:
*/
class Person2 {
    String name;
    int age;

    Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Test37 {
    private static List<Person2> persons =
            Arrays.asList(
                    new Person2("Max", 18),
                    new Person2("Peter", 23),
                    new Person2("Pamela", 23),
                    new Person2("David", 12));
    public static List<Person2> get() {
        return persons;
    }
}
/*

    Collect#
    Collect очень полезный терминальный метод, который служит для преобразования элементов потока в результат иного
    типа, например, List, Set или Map. Collect принимает Collector, который содержит четыре различных метода:
    поставщик (supplier). аккумулятор (accumulator), объединитель (combiner), финишер (finisher). На первый взгляд
    это выглядит очень сложно, однако Java 8 поддерживает различные встроенные коллекторы через класс Collectors,
    где реализованы наиболее используемые методы.
    Популярный случай:
*/
class Test38 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        List<Person2> filtered =
                persons
                        .stream()
                        .filter(p -> p.name.startsWith("P"))
                        .collect(Collectors.toList());

        System.out.println(filtered);    // [Peter, Pamela]
    }
}
/*
    Как видите, создать список из элементов потока очень просто. Нужен не список а множество? Используйте
    Collectors.toSet().

    В следующем примере люди группируются по возрасту:
*/
class Test39 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Map<Integer, List<Person2>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));

        personsByAge
                .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // age 18: [Max]
        // age 23: [Peter, Pamela]
        // age 12: [David]
    }
}
/*
    Коллекторы невероятно разнообразны. Также можно агрегировать элементы коллекции, например, определить средний
    возраст:
*/
class Test40 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));

        System.out.println(averageAge);     // 19.0
    }
}
/*
    Для получения более исчерпывающей статистики используем резюмирующий коллектор, который возвращает специальный
    объект с информацией: минимальным, максимальным и средним значениями, суммой значений и количеством элементов:
*/
class Test41 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        IntSummaryStatistics ageSummary =
                persons
                        .stream()
                        .collect(Collectors.summarizingInt(p -> p.age));

        System.out.println(ageSummary);
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}
    }
}
/*
    Следующий пример объединяет все имена в одну строку:
*/
class Test42 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        String phrase = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(phrase);
        // In Germany Max and Peter and Pamela are of legal age.
    }
}
/*
    Соединяющий коллектор принимает разделитель, а также опционально префикс и суффикс.

    Для преобразования элементов потока в отображение, следует определить каким образом должны отображаться ключи и
    значения. Помните, что ключи в отображении должны быть уникальными. В противном случае получим IllegalStateException.
    Можно опционально добавить функцию слияния для обхода исключения:
*/
class Test43 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Map<Integer, String> map = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));

        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}
    }
}
/*
    Итак, мы ознакомились с некоторыми из наиболее мощных встроенных коллекторов. Попробуем построить собственный. Мы
    хотим преобразовать все элементы потока в одну строку, которая состоит из имен в верхнем регистре, разделенных
    вертикальной чертой |. Для этого создадим новый коллектор используя Collector.of(). Нам нужны четыре составные
    части нашего коллектора: поставщик, аккумулятор, соединитель, финишер.
*/
class Test44 {
    public static void main(String[] args) {
        Collector<Person2, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),  // supplier //поставщик
                        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        String names = persons
                .stream()
                .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }
}
/*
    Поскольку строки в Java неизменяемы, нам нужен класс-помощник типа StringJoiner, позволяющий коллектору построить
    для нас строку. На первой стадии поставщик конструирует StringJoiner с присвоенным разделителем. Аккумулятор
    используется для добавления каждого имени в StringJoiner.

    Соединитель знает как соединить два StringJoinerа в один. И в конце финишер конструирует желаемую строку из
    StringJoinerов.

    FlatMap#
    Итак, мы узнали как превращать объекты потока в другие типы объектов при помощи метода map. Map — своего рода
    ограниченный метод, поскольку каждый объект может быть отображен в только один другой объект. Но что если нужно
    отобразить один объект в множество других, или вовсе не отображать его? Вот тут-то выручает метод flatMap.
    FlatMap превращает каждый объект потока в поток других объектов. Содержимое этих потоков затем упаковывается в
    возвращаемый поток метода flatMap.

    Для того чтобы посмотреть на flatMap в действии, соорудим подходящую иерархию типов для примера:
*/
class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}

class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }
}
/*
    Создадим несколько объектов:
*/
class Test45 {
    public static void main(String[] args) {
        List<Foo> foos = new ArrayList<>();

        // create foos
        IntStream
                .range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

        // create bars
        foos.forEach(f ->
                IntStream
                        .range(1, 4)
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

        foos.stream()
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));

        // Bar1 <- Foo1
        // Bar2 <- Foo1
        // Bar3 <- Foo1
        // Bar1 <- Foo2
        // Bar2 <- Foo2
        // Bar3 <- Foo2
        // Bar1 <- Foo3
        // Bar2 <- Foo3
        // Bar3 <- Foo3
    }
}
/*
    Теперь у нас есть список из трех foo, каждый из которых содержит по три bar.
    FlatMap принимает функцию, которая должна вернуть поток объектов. Таким образом, чтобы получить доступ к объектам
    bar каждого foo, нам просто нужно подобрать подходящую функцию: foos.stream().flatMap(f -> f.bars.stream())
     .forEach(b -> System.out.println(b.name))
    Итак, мы успешно превратили поток из трех объектов foo в поток из 9 объектов bar.

    Наконец, весь вышеприведенный код можно сократить до простого конвейера операций:
*/
class Test46 {
    public static void main(String[] args) {
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }
}
/*

    FlatMap также доступен в классе Optional, введенном в Java 8. FlatMap из класса Optional возвращает опциональный
    объект другого класса. Это может быть использовано чтобы избежать нагромождения проверок на null.

    Представьте себе иерархическую структуру типа этой:
*/
class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}
/*
    Для получения вложенной строки foo из внешнего объекта необходимо добавить множественные проверки на null для
    избежания NullPointException:
*/
class Test47 {
    public static void main(String[] args) {
        Outer outer = new Outer();
        if (outer != null && outer.nested != null && outer.nested.inner != null) {
            System.out.println(outer.nested.inner.foo);
        }
    }
}
/*
    Того же можно добиться, используя flatMap класса Optional:
*/
class Test48 {
    public static void main(String[] args) {
        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);
    }
}
/*
    Каждый вызов flatMap возвращает обертку Optional для желаемого объекта, если он присутствует, либо для null в
    случае отсутствия объекта.

    Reduce#
    Операция упрощения объединяет все элементы потока в один результат. Java 8 поддерживает три различных типа
    метода reduce. Первый сокращает поток элементов до единственного элемента потока. Используем этот метод для
    определения элемента с наибольшим возрастом:
*/
class Test49 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);    // Pamela
    }
}
/*
    Метод reduce принимает аккумулирующую функцию с бинарным оператором (BinaryOperator). Тут reduce является
    би-функцией (BiFunction), где оба аргумента принадлежат одному типу. В нашем случае, к типу Person. Би-функция —
    практически тоже самое, что и функция (Function), однако принимает 2 аргумента. В нашем примере функция сравнивает
    возраст двух людей и возвращает элемент с большим возрастом.

    Следующий вид метода reduce принимает и начальное значение, и аккумулятор с бинарным оператором. Этот метод может
    быть использован для создания нового элемента. У нас — Person с именем и возрастом, состоящими из сложения все
    имен и суммы прожитых лет:
*/
class Test50 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Person2 result =
                persons
                        .stream()
                        .reduce(new Person2("", 0), (p1, p2) -> {
                            p1.age += p2.age;
                            p1.name += p2.name;
                            return p1;
                        });

        System.out.format("name=%s; age=%s", result.name, result.age);
        // name=MaxPeterPamelaDavid; age=76
    }
}
/*
    Третий метод reduce принимает три параметра: изначальное значение, аккумулятор с би-функцией и объединяющую
    функцию типа бинарного оператора. Поскольку начальное значение типа не ограничено до типа Person, можно
    использовать редуцирование для определения суммы прожитых лет каждого человека:
*/
class Test51 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Integer ageSum = persons
                .stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println(ageSum);  // 76
    }
}
/*
    Как видим, мы получили результат 76, но что же на самом деле происходит под капотом?

    Расширим вышеприведенный фрагмент кода выводом текста для дебага:
*/
class Test52 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Integer ageSum = persons
                .stream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        // accumulator: sum=0; person=Max
        // accumulator: sum=18; person=Peter
        // accumulator: sum=41; person=Pamela
        // accumulator: sum=64; person=David
    }
}
/*
    Как видим, всю работу выполняет аккумулирующая функция. Впервые она вызывается с изначальным значением 0 и первым
    человеком Max. В последующих трех шагах sum постоянно возрастает на возраст человека из последнего шага пока не
    достигает общего возраста 76.

    И что дальше? Объединитель никогда не вызывается? Рассмотрим параллельное выполнение этого потока:
*/class Test53 {
    public static void main(String[] args) {
        List<Person2> persons =
                Arrays.asList(
                        new Person2("Max", 18),
                        new Person2("Peter", 23),
                        new Person2("Pamela", 23),
                        new Person2("David", 12));

        Integer ageSum = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        // accumulator: sum=0; person=Pamela
        // accumulator: sum=0; person=David
        // accumulator: sum=0; person=Max
        // accumulator: sum=0; person=Peter
        // combiner: sum1=18; sum2=23
        // combiner: sum1=23; sum2=12
        // combiner: sum1=41; sum2=35
    }
}
/*
    При параллельном выполнении получаем совершенно другой консольный вывод. Сейчас объединитель действительно
    вызывается. Поскольку аккумулятор вызывался параллельно, объединитель должен был суммировать значения,
    сохраненные по-отдельности.

    В следующей главе более детально изучим параллельное выполнение потоков.

    Параллельные потоки#
    Потоки могут выполняться параллельно для повышения производительности при работе с большими количествами входящих
    элементов. Параллельные потоки используют обычный ForkJoinPool доступный через вызов статического метода
    ForkJoinPool.commonPool(). Размер основного пула потоков может достигать 5 потоков выполнения — точное число
    зависит от количества доступных физических ядер процессора.
*/
class Test54 {
    public static void main(String[] args) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 3
    }
}
/*
    На моем компьютере обычный пул потоков по умолчанию инициализируется с распараллеливанием на 3 потока. Это значение
    можно увеличить или уменьшить посредством установки следующего параметра JVM:
        -Djava.util.concurrent.ForkJoinPool.common.parallelism=5

    Коллекции поддерживают метод parallelStream() для создания параллельных потоков данных. Также можно вызвать
    промежуточный метод parallel() для превращения последовательного потока в параллельный.
    Для понимания поведения потока при параллельном выполнении, следующий пример печатает информацию про каждый
    текущий поток (thread) в System.out:
*/
class Test55 {
    public static void main(String[] args) {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }
}
/*
    Рассмотрим выводы с записями для дебага чтобы лучше понять, какой поток (thread) используется для выполнения
    конкретных методов потока (stream):
        filter:  b1 [main]
        filter:  a2 [ForkJoinPool.commonPool-worker-1]
        map:     a2 [ForkJoinPool.commonPool-worker-1]
        filter:  c2 [ForkJoinPool.commonPool-worker-3]
        map:     c2 [ForkJoinPool.commonPool-worker-3]
        filter:  c1 [ForkJoinPool.commonPool-worker-2]
        map:     c1 [ForkJoinPool.commonPool-worker-2]
        forEach: C2 [ForkJoinPool.commonPool-worker-3]
        forEach: A2 [ForkJoinPool.commonPool-worker-1]
        map:     b1 [main]
        forEach: B1 [main]
        filter:  a1 [ForkJoinPool.commonPool-worker-3]
        map:     a1 [ForkJoinPool.commonPool-worker-3]
        forEach: A1 [ForkJoinPool.commonPool-worker-3]
        forEach: C1 [ForkJoinPool.commonPool-worker-2]

    Как видим, при параллельном выполнении потока данных используются все доступные потоки (threads)
    текущего ForkJoinPool. Последовательность вывода может отличаться, так как не определена последовательность
    выполнения каждого конкретного потока (thread).

    Давайте расширим пример добавлением метода sort:
*/
class Test56 {
    public static void main(String[] args) {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort: %s <> %s [%s]\n",
                            s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }
}
/*
    На первый взгляд результат может показаться странным:
        filter:  c2 [ForkJoinPool.commonPool-worker-3]
        filter:  c1 [ForkJoinPool.commonPool-worker-2]
        map:     c1 [ForkJoinPool.commonPool-worker-2]
        filter:  a2 [ForkJoinPool.commonPool-worker-1]
        map:     a2 [ForkJoinPool.commonPool-worker-1]
        filter:  b1 [main]
        map:     b1 [main]
        filter:  a1 [ForkJoinPool.commonPool-worker-2]
        map:     a1 [ForkJoinPool.commonPool-worker-2]
        map:     c2 [ForkJoinPool.commonPool-worker-3]
        sort:    A2 <> A1 [main]
        sort:    B1 <> A2 [main]
        sort:    C2 <> B1 [main]
        sort:    C1 <> C2 [main]
        sort:    C1 <> B1 [main]
        sort:    C1 <> C2 [main]
        forEach: A1 [ForkJoinPool.commonPool-worker-1]
        forEach: C2 [ForkJoinPool.commonPool-worker-3]
        forEach: B1 [main]
        forEach: A2 [ForkJoinPool.commonPool-worker-2]
        forEach: C1 [ForkJoinPool.commonPool-worker-1]

    Кажется, будто sort выполняется последовательно и только в потоке main. На самом деле при параллельном выполнении
    потока под капотом метода sort из Stream API спрятан метод сортировки класса Arrays, добавленный в Java 8,
    — Arrays.parallelSort(). Как указано в документации, этот метод на основании длины входящей коллекции определяет,
    как именно — параллельно или последовательно будет произведена сортировка: Если длина определенного массива меньше
    минимальной “зернистости”, сортировка производится посредством выполнения метода Arrays.sort.

    Вернемся к примеру с методом reduce из предыдущей главы. Мы уже выяснили, что объединительная функция вызывается
    только при параллельной работе с потоком. Рассмотрим, какие потоки задействованы:
*/
class Test57 {
    public static void main(String[] args) {
        List<Person2> persons = Arrays.asList(
                new Person2("Max", 18),
                new Person2("Peter", 23),
                new Person2("Pamela", 23),
                new Person2("David", 12));

        persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> {
                            System.out.format("accumulator: sum=%s; person=%s [%s]\n",
                                    sum, p, Thread.currentThread().getName());
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1=%s; sum2=%s [%s]\n",
                                    sum1, sum2, Thread.currentThread().getName());
                            return sum1 + sum2;
                        });
    }
}
/*
    Консольный вывод показывает, что обе функции: аккумулирующая и объединяющая, выполняются параллельно,
    используя все возможные потоки:
        accumulator: sum=0; person=Pamela; [main]
        accumulator: sum=0; person=Max;    [ForkJoinPool.commonPool-worker-3]
        accumulator: sum=0; person=David;  [ForkJoinPool.commonPool-worker-2]
        accumulator: sum=0; person=Peter;  [ForkJoinPool.commonPool-worker-1]
        combiner:    sum1=18; sum2=23;     [ForkJoinPool.commonPool-worker-1]
        combiner:    sum1=23; sum2=12;     [ForkJoinPool.commonPool-worker-2]
        combiner:    sum1=41; sum2=35;     [ForkJoinPool.commonPool-worker-2]

    Можно утверждать, что параллельное выполнение потока способствует значительному повышению эффективности при работе
    с большими количествами входящих элементов. Однако следует помнить, что некоторые методы при параллельном
    выполнении требуют дополнительных расчетов (объединительных операций), которые не требуются при последовательном
    выполнении.

    Кроме того, для параллельного выполнения потока используется все тот же ForkJoinPool, так широко используемый в JVM.
    Так что применение медленных блокирующих методов потока может негативно отразиться на производительности всей
    программы, за счет блокирования потоков (threads), используемых для обработки в других задачах.

    Вот и все#
    Мое руководство по использованию потоков в Java 8 окончено. Для более подробного изучения работы с потоками можно
    обратиться к документации. Если вы хотите углубиться и больше узнать про механизмы, лежащие в основе работы потоков,
    вам может быть интересно прочитать статью Мартина Фаулера (Martin Fowler) Collection Pipelines.

    Если вам так же интересен JavaScript, вы можете захотеть взглянуть на Stream.js — JavaScript реализацию
    Java 8 Streams API. Возможно, вы также захотите прочитать мои статьи Java 8 Tutorial (русский перевод на Хабре)
    и Java 8 Nashorn Tutorial.

    Надеюсь, это руководство было полезным и интересным для вас, и вы наслаждались в процессе чтения. Полный код
    хранится в GitHub. Чувствуйте себя свободно, создавая ответвление в репозитории.
 */

class SpeedStatistic { //03.02.2022 Анна Гой, были проблемы с циклом фор
    public static int[] getSpeedStatistic(int[] results) {
        if (results.length == 0) {return new int[] {0, 0};}

        int max = results[0]; // присвоить первое значение из массива
        int min = results[0]; // присвоить первое значение из массива

        for (int result : results) { // пробежка по массиву
            if (result < min) { // если число из массива меньше чем минимальная переменная
                min = result; // обновить значение для переменной минимального числа
            } else if (result > max) { //если число из массива больше чем максимальная переменная
                max = result; // обновить значение для максимальной переменной
            }
        }
        return new int[] {min, max}; // отдать результат
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getSpeedStatistic(new int[0])));
        System.out.println(Arrays.toString(getSpeedStatistic(new int[2])));
        System.out.println(Arrays.toString(getSpeedStatistic(new int[]{20})));
        System.out.println(Arrays.toString(getSpeedStatistic(new int[]{20, 30, 40, 5, 1000, 1})));
    }
}

/*
    TODO вторая статья https://habr.com/ru/company/luxoft/blog/270383/
    vedenin1980 18 ноября 2015 в 15:43

    Шпаргалка Java программиста 4. Java Stream API#
    Несмотря на то, что Java 8 вышла уже достаточно давно, далеко не все программисты используют её новые возможности,
    кого-то останавливает то, что рабочие проекты слишком сложно перевести с Java 7 или даже Java 6, кого-то
    использование в своих проектах GWT, кто-то делает проекты под Android и не хочет или не может использовать
    сторонние библиотеки для реализации лямбд и Stream Api. Однако знание лямбд и Stream Api для программиста Java
    зачастую требуют на собеседованиях, ну и просто будет полезно при переходе на проект где используется Java 8. Я
    хотел бы предложить вам краткую шпаргалку по Stream Api с практическими примерами реализации различных задач с
    новым функциональным подходом. Знания лямбд и функционального программирования не потребуется (я постарался дать
    примеры так, чтобы все было понятно), уровень от самого базового знания Java и выше.

    Также, так как это шпаргалка, статья может использоваться, чтобы быстро вспомнить как работает та или иная
    особенность Java Stream Api. Краткое перечисление возможностей основных функций дано в начале статьи.

    Для тех кто совсем не знает что такое Stream Api#
    Stream API это новый способ работать со структурами данных в функциональном стиле. Чаще всего с помощью stream в
    Java 8 работают с коллекциями, но на самом деле этот механизм может использоваться для самых различных данных.

    Stream Api позволяет писать обработку структур данных в стиле SQL, то если раньше задача получить сумму всех
    нечетных чисел из коллекции решалась следующим кодом:
    */
class Test58 {
    public static void main(String[] args) {
        Integer sumOddOld = 0;
        Integer[] collection = new Integer[] {0, 2, 1, 5};
        for(Integer i: collection) {
            if(i % 2 != 0) {
                sumOddOld += i;
            }
        }
        System.out.println(sumOddOld);
    }
}
    /*
    То с помощью Stream Api можно решить такую задачу в функциональном стиле:
    */
class Test59 {
    public static void main(String[] args) {
        List<Integer> collection = Arrays.asList(0, 2, 1, 5);
        Integer sumOdd = collection.stream()
                .filter(o -> o % 2 != 0)
                .reduce((s1, s2) -> s1 + s2)
                .orElse(0);
        System.out.println(sumOdd);
    }
}
    /*
    Более того, Stream Api позволяет решать задачу параллельно лишь изменив stream() на parallelStream() без всякого
    лишнего кода, т.е.
    */
class Test60 {
    public static void main(String[] args) {
        List<Integer> collection = Arrays.asList(0, 2, 1, 5);
        Integer sumOdd = collection.parallelStream()
                .filter(o -> o % 2 != 0)
                .reduce((s1, s2) -> s1 + s2)
                .orElse(0);
        System.out.println(sumOdd);
    }
}
    /*
    Уже делает код параллельным, без всяких семафоров, синхронизаций, рисков взаимных блокировок и т.п.
    Давайте начнем с начала, а именно с создания объектов stream в Java 8.

    I. Способы создания стримов#
    Перечислим несколько способов создать стрим
        _________________________________________________
        Способ создания стрима | Шаблон создания | Пример
        _________________________________________________
        1. Классический: Создание стрима из коллекции. | collection.stream() |
        */
        class Test61 {
            public static void main(String[] args) {
                Collection<String> collection = Arrays.asList("a1", "a2", "a3");
                Stream<String> streamFromCollection = collection.stream();
                //System.out.println(Arrays.toString(streamFromCollection.toArray()));
            }
        }
        /*
        _________________________________________________
        2. Создание стрима из значений. | Stream.of(значение1,… значениеN) |
        */
        class Test62 {
            public static void main(String[] args) {
                Stream<String> streamFromValues = Stream.of("a1", "a2", "a3");
                //System.out.println(Arrays.toString(streamFromValues.toArray()));
            }
        }
        /*
        _________________________________________________
        3. Создание стрима из массива. | Arrays.stream(массив) |
        */
        class Test63 {
            public static void main(String[] args) {
                String[] array = {"a1","a2","a3"};
                Stream<String> streamFromArrays = Arrays.stream(array);
                //System.out.println(Arrays.toString(streamFromArrays.toArray()));
            }
        }
        /*
        _________________________________________________
        4. Создание стрима из файла (каждая строка в файле будет отдельным элементом в стриме) |
           Files.lines(путь_к_файлу) |
        */
        class Test64 {
            public static void main(String[] args) throws IOException {
                Stream<String> streamFromFiles = Files.lines(Paths.get("src\\main\\java\\lectureNotes\\file.txt"));
                //System.out.println(Arrays.toString(streamFromFiles.toArray()));
            }
        }
        /*
        _________________________________________________
        5. Создание стрима из строки | «строка».chars() |
        */
        class Test65 {
            public static void main(String[] args) {
                IntStream streamFromString = "123".chars();
                //StringBuilder builder = new StringBuilder();
                //streamFromString.forEach(i -> builder.append(Character.getNumericValue(i)).append(" "));
                //System.out.println(builder);
            }
        }
        /*
        _________________________________________________
        6. С помощью Stream.builder | Stream.builder().add(...)....build() |
        */
        class Test66 {
            public static void main(String[] args) {
                Stream<Object> build = Stream.builder().add("a1").add("a2").add("a3").build();
                //System.out.println(Arrays.toString(build.toArray()));
            }
        }
        /*
        _________________________________________________
        7. Создание параллельного стрима | collection.parallelStream() |
        */
        class Test67 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a1","a2","a3");
                Stream<String> stream = collection.parallelStream();
                //System.out.println(Arrays.toString(stream.toArray()));
            }
        }
        /*
        _________________________________________________
        8. Создание бесконечных стрима с помощью Stream.iterate |
           Stream.iterate(начальное_условие, выражение_генерации) |
        */
        class Test68 {
            public static void main(String[] args) {
                Stream<Integer> streamFromIterate = Stream.iterate(1, n -> n + 1);
                // без ограничения опасно запускать, лупит бесконечно
                //System.out.println(Arrays.toString(streamFromIterate
                //        .limit(20)
                //        .toArray()));
            }
        }
        /*
        _________________________________________________
        9. Создание бесконечных стрима с помощью Stream.generate |
           Stream.generate(выражение_генерации) |
        */
        class Test69 {
            public static void main(String[] args) {
                Stream<String> streamFromGenerate = Stream.generate(() -> "a1");
                // без ограничения опасно запускать, лупит бесконечно
                //System.out.println(Arrays.toString(streamFromGenerate
                //        .limit(20)
                //        .toArray()));
            }
        }
        /*
        _________________________________________________

    В принципе, кроме последних двух способов создания стрима, все не отличается от обычных способов создания коллекций.
    Последние два способа служат для генерации бесконечных стримов, в iterate задается начальное условие и выражение
    получение следующего значения из предыдущего, то есть Stream.iterate(1, n -> n + 1) будет выдавать значения
    1, 2, 3, 4,… N. Stream.generate служит для генерации константных и случайных значений, он просто выдает значения
    соответствующие выражению, в данном примере, он будет выдавать бесконечное количество значений «a1».

    Для тех кто не знает лямбды#
    Выражение n -> n + 1, это просто аналог выражения Integer func(Integer n) { return n+1;}, а выражение () -> «a1»
    аналог выражения String func() { return «a1»;} обернутых в анонимный класс.

    Более подробные примеры#
    Так же этот пример можно найти на github'e
    */
    class Test70 {
        public static void main(String[] args) throws IOException {
            System.out.println("Тестовый запуск buildStream");

            // Создание стрима из значений
            Stream<String> streamFromValues = Stream.of("a1", "a2", "a3");
            // напечатает: streamFromValues = [a1, a2, a3]
            System.out.println("streamFromValues = " + streamFromValues.collect(Collectors.toList()));

            // Создание стрима из массива
            String[] array = {"a1", "a2", "a3"};
            Stream<String> streamFromArrays = Arrays.stream(array);
            // напечатает: streamFromArrays = [a1, a2, a3]
            System.out.println("streamFromArrays = " + streamFromArrays.collect(Collectors.toList()));

            Stream<String> streamFromArrays1 = Stream.of(array);
            // напечатает streamFromArrays = [a1, a2, a3]
            System.out.println("streamFromArrays1 = " + streamFromArrays1.collect(Collectors.toList()));

            // Создание стрима из файла (каждая запись в файле будет отдельной строкой в стриме)
            File file = new File("src\\main\\java\\lectureNotes\\file.txt");
            file.deleteOnExit();
            PrintWriter out = new PrintWriter(file);
            out.println("a1");
            out.println("a2");
            out.println("a3");
            out.close();

            Stream<String> streamFromFiles = Files.lines(Paths.get(file.getAbsolutePath()));
            // напечатает: streamFromFiles = [a1, a2, a3]
            System.out.println("streamFromFiles = " + streamFromFiles.collect(Collectors.toList()));

            // Создание стрима из коллекции
            Collection<String> collection = Arrays.asList("a1", "a2", "a3");
            Stream<String> streamFromCollection = collection.stream();
            // напечатает streamFromCollection = [a1, a2, a3]
            System.out.println("streamFromCollection = " + streamFromCollection.collect(Collectors.toList()));

            // Создание числового стрима из строки
            IntStream streamFromString = "123".chars();
            System.out.print("streamFromString = ");
            // напечатает: streamFromString = 49 , 50 , 51 ,
            streamFromString.forEach((e) -> System.out.print(e + " , "));
            System.out.println();

            // С помощью Stream.builder
            Stream.Builder<String> builder = Stream.builder();
            Stream<String> streamFromBuilder = builder.add("a1").add("a2").add("a3").build();
            // напечатает: streamFromFiles = [a1, a2, a3]
            System.out.println("streamFromBuilder = " + streamFromBuilder.collect((Collectors.toList())));

            // Создание бесконечных стримов
            // С помощью Stream.iterate
            Stream<Integer> streamFromIterate = Stream.iterate(1, n -> n + 2);
            // напечатает streamFromIterate = [1, 3, 5]
            System.out.println("streamFromIterate = " + streamFromIterate.limit(3).collect(Collectors.toList()));

            // С помощью Stream.generate
            Stream<String> streamFromGenerate = Stream.generate(() -> "a1");
            // напечатает streamFromGenerate = [a1, a1, a1]
            System.out.println("streamFromGenerate = " + streamFromGenerate.limit(3).collect(Collectors.toList()));

            // Создать пустой стрим
            Stream<String> streamEmpty = Stream.empty();
            // напечатает streamEmpty = []
            System.out.println("streamEmpty = " + streamEmpty.collect(Collectors.toList()));

            // Создать параллельный стрим из коллекции
            Stream<String> parallelStream = collection.parallelStream();
            // напечатает parallelStream = [a1, a2, a3]
            System.out.println("parallelStream = " + parallelStream.collect(Collectors.toList()));
        }
    }
    /*

    II. Методы работы со стримами#
    Java Stream API предлагает два вида методов:
        1. Конвейерные — возвращают другой stream, то есть работают как builder,
        2. Терминальные — возвращают другой объект, такой как коллекция, примитивы, объекты, Optional и т.д.

    О том чем отличаются конвейерные и терминальные методы#
    Общее правило: у stream'a может быть сколько угодно вызовов конвейерных вызовов и в конце один терминальный, при
    этом все конвейерные методы выполняются лениво и пока не будет вызван терминальный метод никаких действий на самом
    деле не происходит, так же как создать объект Thread или Runnable, но не вызвать у него start.

    В целом, этот механизм похож на конструирования SQL запросов, может быть сколько угодно вложенных Select'ов и
    только один результат в итоге. Например, в выражении
    collection.stream().filter((s) -> s.contains(«1»)).skip(2).findFirst()
    filter и skip — конвейерные, а findFirst — терминальный, он возвращает объект Optional и это заканчивает
    работу со stream'ом.

    2.1 Краткое описание конвейерных методов работы со стримами#
        ------------------------------------------------------------
        Метод stream | Описание | Пример
        ------------------------------------------------------------
        filter | Отфильтровывает записи, возвращает только записи, соответствующие условию |
        */
        class Test71 {
            public static void main(String[] args) {
                Stream<String> streamFromValues = Stream.of("a1", "a2", "a3", "a1");
                System.out.println(streamFromValues
                        .filter("a1"::equals)
                        .count()); // сколько в массиве таких вхождений
            }
        }
        /*
        ------------------------------------------------------------
        skip | Позволяет пропустить N первых элементов |
        */
        class Test72 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4");
                String s = collection
                        .stream()
                        .skip(collection.size() - 1) // пропустить размер массива -1
                        .findFirst() // взять первый
                        .orElse("1"); // если такого нету вернуть "1"
                System.out.println(s);
            }
        }
        /*
        ------------------------------------------------------------
        distinct | Возвращает стрим без дубликатов (для метода equals) |
        */
        class Test73 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4", "a3");
                List<String> collect = collection.
                        stream().
                        distinct().
                        collect(Collectors.toList());
                System.out.println(collect);
            }
        }
        /*
        ------------------------------------------------------------
        map | Преобразует каждый элемент стрима |
        */class Test74 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4", "a3");
                List<String> collect = collection
                        .stream()
                        .map((s) -> s + "_1")
                        .collect(Collectors.toList());
                System.out.println(collect);
            }
        }
        /*
        ------------------------------------------------------------
        peek | Возвращает тот же стрим, но применяет функцию к каждому элементу стрима
        */
        class Test75 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4", "a3");
                List<String> collect = collection
                        .stream()
                        .map(String::toUpperCase)
                        .peek((e) -> System.out.print("," + e))
                        .collect(Collectors.toList());
                System.out.println("\n" + collect);
            }
        }
        /*
        ------------------------------------------------------------
        limit | Позволяет ограничить выборку определенным количеством первых элементов |
        */
        class Test76 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4", "a3");
                List<String> collect = collection
                        .stream()
                        .limit(2)
                        .collect(Collectors.toList());
                System.out.println(collect);
            }
        }
        /*
        ------------------------------------------------------------
        sorted | Позволяет сортировать значения либо в натуральном порядке, либо задавая Comparator |
        */
        class Test77 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1","a2","a3", "a4", "a3");
                List<String> collect = collection
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println(collect);
            }
        }
        /*
        ------------------------------------------------------------
        mapToInt, mapToDouble, mapToLong |
        Аналог map, но возвращает числовой стрим (то есть стрим из числовых примитивов) |
        */
        class Test78 {
            public static void main(String[] args) {
                List<String> collection = List.of("1", "2", "4", "200", "0", "79");
                int[] ints = collection
                        .stream()
                        .mapToInt((s) -> Integer.parseInt(s))
                        .toArray();
                System.out.println(Arrays.toString(ints));
            }
        }
        /*
        ------------------------------------------------------------
        flatMap, flatMapToInt, flatMapToDouble, flatMapToLong |
        Похоже на map, но может создавать из одного элемента несколько
        */
        class Test79 {
            public static void main(String[] args) {
                List<String> collection = List.of("1", "2, 676", "4", "200, 45", "0", "79");
                String[] strings = collection
                        .stream()
                        .flatMap((p) -> Arrays.stream(p.split(",")))
                        .toArray(String[]::new);
                System.out.println(Arrays.toString(strings));
            }
        }
        /*
 */