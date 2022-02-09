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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4");
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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3");
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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3");
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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3");
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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3");
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
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3");
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
        ------------------------------------------------------------

    2.2 Краткое описание терминальных методов работы со стримами#
        __________________________________________________________________
        Метод stream | Описание | Пример
        __________________________________________________________________
        findFirst | Возвращает первый элемент из стрима (возвращает Optional) |
        */
        class Test80 {
            public static void main(String[] args) {
                List<String> collection = List.of("1", "2, 676", "4", "200, 45", "0", "79");
                String s = collection
                        .stream()
                        .findFirst()
                        .orElse("0");
                System.out.println(s);
            }
        }
        /*
        __________________________________________________________________
        findAny | 	Возвращает любой подходящий элемент из стрима (возвращает Optional) |
        */
        class Test81 {
            public static void main(String[] args) {
                List<String> collection = List.of("15", "2, 676", "4", "200, 45", "0", "79");
                String s = collection
                        .stream()
                        .findAny()
                        .orElse("10000000000");
                System.out.println(s.getClass() + "  >> " + s);
            }
        }
        /*
        __________________________________________________________________
        collect | Представление результатов в виде коллекций и других структур данных |
        */
        class Test82 {
            public static void main(String[] args) {
                List<String> collection = List.of("15", "2, 676", "4", "200, 45", "0", "79");
                List<String> collect = collection
                        .stream()
                        .filter((s) -> s.contains("2"))
                        .collect(Collectors.toList());
                System.out.println(collect);
            }
        }
        /*
        __________________________________________________________________
        count | Возвращает количество элементов в стриме |
        */
        class Test83 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1" ,"a2", "a3", "a4", "a3", "a1", "a1");
                long count = collection
                        .stream()
                        .filter("a1"::equals)
                        .count();
                System.out.println(count);
            }
        }
        /*
        __________________________________________________________________
        anyMatch | 	Возвращает true, если условие выполняется хотя бы для одного элемента |
        */
        class Test84 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1" ,"a2" ,"a3", "a4", "a3", "a1", "a1");
                boolean b = collection.stream().anyMatch("a1"::equals);
                System.out.println(b);
            }
        }
        /*
        __________________________________________________________________
        noneMatch | Возвращает true, если условие не выполняется ни для одного элемента |
        */
        class Test85 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a0", "a1", "a2", "a3", "a4", "a3", "a1", "a1");
                boolean b = collection.stream().noneMatch("a8"::equals);
                System.out.println(b);
            }
        }
        /*
        __________________________________________________________________
        allMatch | 	Возвращает true, если условие выполняется для всех элементов |
        */
        class Test86 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a1", "a1", "a1", "a113", "a14", "a13", "a1", "a1");
                boolean b = collection.stream().allMatch((s) -> s.contains("1"));
                System.out.println(b);
            }
        }
        /*
        __________________________________________________________________
        min | Возвращает минимальный элемент, в качестве условия использует компаратор |
        */
        class Test87 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a2", "f", "a", "a113", "a14", "a13", "f", "a1");
                String s = collection.stream().min(String::compareTo).get();
                System.out.println(s);
            }
        }
        /*
        __________________________________________________________________
        max | Возвращает максимальный элемент, в качестве условия использует компаратор |
        */
        class Test88 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a2", "fa", "a", "aw113v", "a14", "a13", "ff", "a1");
                String s = collection.stream().max(String::compareTo).get();
                System.out.println(s);
                //мягко говоря специфичный
            }
        }
        /*
        __________________________________________________________________
        forEach | Применяет функцию к каждому объекту стрима, порядок при параллельном выполнении не гарантируется |
        */
        class Test89 {
            public static void main(String[] args) {
                List<String> set = Arrays.asList("a2", "fa", "a", "aw113v", "a14", "a13", "ff", "a1");
                StringBuilder result = new StringBuilder();
                set.stream()
                        .forEach((p) -> result.append(p).append("_1, "));
                System.out.println(result);
            }
        }
        /*
        __________________________________________________________________
        forEachOrdered | Применяет функцию к каждому объекту стрима, сохранение порядка элементов гарантирует |
        */
        class Test90 {
            public static void main(String[] args) {
                List<String> list = Arrays.asList("a2", "fa", "a", "aw113v", "a14", "a13", "ff", "a1");
                StringBuilder result = new StringBuilder();
                list
                        .stream()
                        .forEachOrdered((p) -> result.append(p).append("_new, "));
                System.out.println(result);
            }
        }
        /*
        __________________________________________________________________
        toArray | Возвращает массив значений стрима |
        */
        class Test91 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a2", "fa", "a", "aw113v", "a14", "a13", "ff", "a1");
                String[] strings = collection
                        .stream()
                        .map(String::toUpperCase)
                        .toArray(String[]::new);
                System.out.println(Arrays.toString(strings));
            }
        }
        /*
        __________________________________________________________________
        reduce | Позволяет выполнять агрегатные функции на всей коллекцией и возвращать один результат |
        */
        class Test92 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("a2", "fa", "a", "aw113v", "a14", "a13", "ff", "a1");
                String s = collection.stream().reduce((s1, s2) -> s1 + s2).orElse("0");
                System.out.println(s);
            }
        }
        /*
        __________________________________________________________________

    Обратите внимание методы findFirst, findAny, anyMatch это short-circuiting методы, то есть обход стримов
    организуется таким образом, чтобы найти подходящий элемент максимально быстро, а не обходить весь изначальный стрим.

    2.3 Краткое описание дополнительных методов у числовых стримов#
        __________________________________________________________________
        Метод stream | Описание | Пример
        __________________________________________________________________
        sum | Возвращает сумму всех чисел |
        */
        class Test93 {
            public static void main(String[] args) {
                List<String> collection = Arrays.asList("1", "3", "40");
                int sum = collection
                        .stream()
                        .mapToInt((s) -> Integer.parseInt(s))
                        .sum();
                System.out.println(sum);
            }
        }
        /*
        __________________________________________________________________
        average | Возвращает среднее арифметическое всех чисел |
        */
        class Test94 {
            public static void main(String[] args) {
                OptionalDouble average = Arrays
                        .asList("1", "3", "40")
                        .stream()
                        .mapToInt((s) -> Integer.parseInt(s))
                        .average();
                System.out.println(average);
            }
        }
        /*
        __________________________________________________________________
        mapToObj | Преобразует числовой стрим обратно в объектный |
        */
        class Test95 {
            public static void main(String[] args) {
                Object[] objects = IntStream
                        .of(1, 4, 6, 7)
                        .mapToObj(Integer::valueOf)
                        .toArray();
                System.out.println(Arrays.toString(objects) + " " + objects.getClass());
            }
        }
        /*
        __________________________________________________________________



    2.4 Несколько других полезных методов стримов#
        __________________________________________________________________
        Метод stream | Описание
        __________________________________________________________________

        isParallel | Узнать является ли стрим параллельным
        __________________________________________________________________
        parallel | Вернуть параллельный стрим, если стрим уже параллельный, то может вернуть самого себя
        __________________________________________________________________
        sequential | Вернуть последовательный стрим, если стрим уже последовательный, то может вернуть самого себя
        __________________________________________________________________

    С помощью, методов parallel и sequential можно определять какие операции могут быть параллельными, а какие только
    последовательными. Так же из любого последовательного стрима можно сделать параллельный и наоборот, то есть:
            collection.stream().
            peek(...). // операция последовательна
            parallel().
            map(...). // операция может выполняться параллельно,
            sequential().
            reduce(...) // операция снова последовательна

    Внимание: крайне не рекомендуется использовать параллельные стримы для сколько-нибудь долгих операций (получение
    данных из базы, сетевых соединений), так как все параллельные стримы работают c одним пулом fork/join и такие долгие
    операции могут остановить работу всех параллельных стримов в JVM из-за того отсутствия доступных потоков в пуле,
    т.е. параллельные стримы стоит использовать лишь для коротких операций, где счет идет на миллисекунды, но не для
    тех где счет может идти на секунды и минуты.

    III. Примеры работы с методами стримов#
    Рассмотрим работу с методами на различных задачах, обычно требующихся при работе с коллекциями.

    3.1 Примеры использования filter, findFirst, findAny, skip, limit и count#

        Условие: дана коллекция строк Arrays.asList(«a1», «a2», «a3», «a1»), давайте посмотрим как её можно обрабатывать
        используя методы filter, findFirst, findAny, skip и count:

        ---------------------------------------------------------------------------------------------------------------
        Задача | Код примера | // Результат
        ---------------------------------------------------------------------------------------------------------------
            Вернуть количество вхождений объекта "a1" |
            */
            class Test96 {
                public static void main(String[] args) {
                    long count = Stream.of("a1", "a2", "a3", "a1")
                            .filter("a1"::equals).count();
                    System.out.println(count); // 2
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Вернуть первый элемент коллекции или 0, если коллекция пуста |
            */
            class Test97 {
                public static void main(String[] args) {
                    String s = Stream.of("a1", "a2", "a3", "a1")
                            .findFirst()
                            .orElse("0");
                    System.out.println(s); // a1
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Вернуть последний элемент коллекции или «empty», если коллекция пуста |
            */
            class Test98 {
                public static void main(String[] args) {
                    List<String> collection = List.of("a1", "a2", "a3", "a1");
                    String empty = collection
                            .stream()
                            .skip(collection.size() - 1)
                            .findAny()
                            .orElse("empty");
                    System.out.println(empty); // a1
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Найти элемент в коллекции равный «a3» или кинуть ошибку |
            */
            class Test99 {
                public static void main(String[] args) {
                    List<String> collection = List.of("a1", "a2", "a3", "a1");
                    String s = collection
                            .stream()
                            .filter("a3"::equals)
                            .findFirst()
                            .get();
                    System.out.println(s); // a3
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Вернуть третий элемент коллекции по порядку |
            */
            class Test100 {
                public static void main(String[] args) {
                    List<String> collection = List.of("a1", "a2", "a3", "a1");
                    String s = collection
                            .stream()
                            .skip(2)
                            .findFirst()
                            .get();
                    System.out.println(s); // a3
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Вернуть два элемента начиная со второго |
            */
            class Test101 {
                public static void main(String[] args) {
                    List<String> collection = List.of("a1", "a2", "a3", "a1");
                    Object[] objects = collection
                            .stream()
                            .skip(1)
                            .limit(2)
                            .toArray();
                    System.out.println(Arrays.toString(objects)); // [a2, a3]
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Выбрать все элементы по шаблону |
            */
            class Test102 {
                public static void main(String[] args) {
                    List<String> collection = List.of("a1", "a2", "a3", "a1");
                    List<String> collect = collection
                            .stream()
                            .filter((s) -> s.contains("1"))
                            .collect(Collectors.toList());
                    System.out.println(collect); // [a1, a1]
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------

    Обратите внимание, что методы findFirst и findAny возвращают новый тип Optional, появившийся в Java 8, для того
    чтобы избежать NullPointerException. Метод filter удобно использовать для выборки лишь определенного множества
    значений, а метод skip позволяет пропускать определенное количество элементов.

    Если вы не знаете лямбды#
    Выражение "a3"::equals это аналог boolean func(s) { return "a3".equals(s);},
    а (s) -> s.contains("1") это аналог boolean func(s) { return s.contains("1");} обернутых в анонимный класс.

        Условие: дана коллекция класс People (с полями name — имя, age — возраст, sex — пол), вида
        Arrays.asList( new People(«Вася», 16, Sex.MAN), new People(«Петя», 23, Sex.MAN),
        new People(«Елена», 42, Sex.WOMEN), new People(«Иван Иванович», 69, Sex.MAN)).
        Давайте посмотрим примеры как работать с таким классом:

        ---------------------------------------------------------------------------------------------------------------
            Задача | Код примера | // Результат
        ---------------------------------------------------------------------------------------------------------------
            Выбрать мужчин-военнообязанных (от 18 до 27 лет) |
            */
            class People {
                private final String name;
                private final int age;
                private final String sex;

                public People(String name, int age, String sex) {
                    this.name = name;
                    this.age = age;
                    this.sex = sex;
                }

                public String getName() {return name;}
                public int getAge() {return age;}
                public String getSex() {return sex;}

                @Override
                public String toString() {
                    return "{" +
                            "name='" + name + '\'' +
                            ", age=" + age +
                            ", sex='" + sex + '\'' +
                            '}';
                }
            }

            class CollectionPeoples {
                private static List<People> people = Arrays.asList(
                        new People("Вася", 16, "MAN"),
                        new People("Петя", 23, "MAN"),
                        new People("Елена", 42, "WOMEN"),
                        new People("Иван Иванович", 69, "MAN")
                );

                public static List<People> getPeoples() {
                    return people;
                }
            }

            class Test103 {
                public static void main(String[] args) {
                    List<People> peoples = CollectionPeoples.getPeoples();
                    List<People> man = peoples.
                            stream()
                            .filter((p) -> p.getAge() >= 18 && p.getAge() < 27 && Objects.equals(p.getSex(), "MAN"))
                            .collect(Collectors.toList());
                    System.out.println(man); // [{name='Петя', age=23, sex='MAN'}]
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Найти средний возраст среди мужчин |
            */
            class Test104 {
                public static void main(String[] args) {
                    List<People> peoples = CollectionPeoples.getPeoples();
                    double man = peoples
                            .stream()
                            .filter((p) -> Objects.equals(p.getSex(), "MAN"))
                            .mapToInt(People::getAge)
                            .average()
                            .getAsDouble();
                    System.out.println(man); // 36.0
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------
            Найти кол-во потенциально работоспособных людей в выборке (т.е. от 18 лет и учитывая что женщины выходят
            в 55 лет, а мужчина в 60) |
            */
            class Test105 {
                public static void main(String[] args) {
                    List<People> peoples = CollectionPeoples.getPeoples();
                    long count = peoples
                            .stream()
                            .filter((p) -> p.getAge() >= 18)
                            .filter((p) -> (Objects.equals(p.getSex(), "WOMEN") && p.getAge() < 55)
                                    || (Objects.equals(p.getSex(), "MAN") && p.getAge() < 60))
                            .count();
                    System.out.println(count); // 2
                }
            }
            /*
        ---------------------------------------------------------------------------------------------------------------

    Детальные примеры#

    Также этот пример можно найти на github'e: первый класс и второй класс
    https://github.com/Vedenin/java_in_examples/blob/master/stream_api/src/com/github/vedenin/rus/stream_api/FiterAndCountTests.java
    https://github.com/Vedenin/java_in_examples/blob/master/stream_api/src/com/github/vedenin/rus/stream_api/LimitAndSkipTests.java
    */
class Test106 {
    // filter - возвращает stream, в котором есть только элементы, соответствующие условию фильтра
    // count - возвращает количество элементов в стриме
    // collect - преобразует stream в коллекцию или другую структуру данных
    // mapToInt - преобразовать объект в числовой стрим (стрим, содержащий числа)
    private static void testFilterAndCount() {
        System.out.println();
        System.out.println("Test filter and count start");
        Collection<String> collection = Arrays.asList("a1", "a2", "a3", "a1");
        Collection<People> peoples = Arrays.asList(
                new People("Вася", 16, Sex.MAN),
                new People("Петя", 23, Sex.MAN),
                new People("Елена", 42, Sex.WOMEN),
                new People("Иван Иванович", 69, Sex.MAN)
        );

        // Вернуть количество вхождений объекта
        long count = collection.stream().filter("a1"::equals).count();
        System.out.println("count = " + count); // напечатает count = 2

        // Выбрать все элементы по шаблону
        List<String> select = collection.stream().filter((s) -> s.contains("1")).collect(Collectors.toList());
        System.out.println("select = " + select); // напечатает select = [a1, a1]

        // Выбрать мужчин-военнообязанных
        List<People> militaryService = peoples.stream().filter((p)-> p.getAge() >= 18 && p.getAge() < 27
                && p.getSex() == Sex.MAN).collect(Collectors.toList());
        System.out.println("militaryService = " + militaryService); // напечатает militaryService = [{name='Петя', age=23, sex=MAN}]

        // Найти средний возраст среди мужчин
        double manAverageAge = peoples.stream().filter((p) -> p.getSex() == Sex.MAN).
                mapToInt(People::getAge).average().getAsDouble();
        System.out.println("manAverageAge = " + manAverageAge); // напечатает manAverageAge = 36.0

        // Найти кол-во потенциально работоспособных людей в выборке (т.е. от 18 лет и учитывая что женщины выходят в 55 лет, а мужчина в 60)
        long peopleHowCanWork = peoples.stream().filter((p) -> p.getAge() >= 18).filter(
                (p) -> (p.getSex() == Sex.WOMEN && p.getAge() < 55) || (p.getSex() == Sex.MAN && p.getAge() < 60)).count();
        System.out.println("peopleHowCanWork = " + peopleHowCanWork); // напечатает manAverageAge = 2

    }

   // findFirst - возвращает первый Optional элемент из стрима
    // skip - пропускает N первых элементов (где N параметр метода)
    // collect преобразует stream в коллекцию или другую структуру данных
    private static void testFindFirstSkipCount() {
        Collection<String> collection = Arrays.asList("a1", "a2", "a3", "a1");

        System.out.println("Test findFirst and skip start");
        // вернуть первый элемент коллекции
        String first = collection.stream().findFirst().orElse("1");
        System.out.println("first = " + first); // напечатает first = a1

        // вернуть последний элемент коллекции
        String last = collection.stream().skip(collection.size() - 1).findAny().orElse("1");
        System.out.println("last = " + last ); // напечатает last = a1

        // найти элемент в коллекции
        String find = collection.stream().filter("a3"::equals).findFirst().get();
        System.out.println("find = " + find); // напечатает find = a3

        // вернуть третий элемент коллекции по порядку
        String third = collection.stream().skip(2).findFirst().get();
        System.out.println("third = " + third); // напечатает third = a3

        System.out.println();
        System.out.println("Test collect start");
        // выбрать все элементы по шаблону
        List<String> select = collection.stream().filter((s) -> s.contains("1")).collect(Collectors.toList());
        System.out.println("select = " + select); // напечатает select = [a1, a1]
    }

    // Метод Limit позволяет ограничить выборку определенным количеством первых элементов
    private static void testLimit() {
        System.out.println();
        System.out.println("Test limit start");
        Collection<String> collection = Arrays.asList("a1", "a2", "a3", "a1");

        // Вернуть первые два элемента
        List<String> limit = collection.stream().limit(2).collect(Collectors.toList());
        System.out.println("limit = " + limit); // напечатает limit = [a1, a2]

        // Вернуть два элемента начиная со второго
        List<String> fromTo = collection.stream().skip(1).limit(2).collect(Collectors.toList());
        System.out.println("fromTo = " + fromTo); // напечатает fromTo = [a2, a3]

        // вернуть последний элемент коллекции
        String last = collection.stream().skip(collection.size() - 1).findAny().orElse("1");
        System.out.println("last = " + last ); // напечатает last = a1
    }

    private enum Sex {
        MAN,
        WOMEN
    }

    private static class People {
        private final String name;
        private final Integer age;
        private final Sex sex;

        public People(String name, Integer age, Sex sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public Sex getSex() {
            return sex;
        }

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sex=" + sex +
                    '}';
        }
    }
}
/*

    3.2 Примеры использования distinct#

Метод distinct возвращает stream без дубликатов, при этом для упорядоченного стрима (например, коллекция на основе list) порядок стабилен, для неупорядоченного — порядок не гарантируется. Рассмотрим результаты работы над коллекцией Collection ordered = Arrays.asList(«a1», «a2», «a2», «a3», «a1», «a2», «a2») и Collection nonOrdered = new HashSet<>(ordered).
Задача	Код примера	Результат
Получение коллекции без дубликатов из неупорядоченного стрима	nonOrdered.stream().distinct().collect(Collectors.toList())	[a1, a2, a3] —
порядок не гарантируется
Получение коллекции без дубликатов из упорядоченного стрима	ordered.stream().distinct().collect(Collectors.toList());	[a1, a2, a3] —
порядок гарантируется


Обратите внимание:
1. Если вы используете distinct с классом, у которого переопределен equals, обязательно так же корректно переопределить hashCode в соответствие с контрактом equals/hashCode (самое главное чтобы hashCode для всех equals объектов, возвращал одинаковое значение), иначе distinct может не удалить дубликаты (аналогично, как при использовании HashSet/HashMap),
2. Если вы используете параллельные стримы и вам не важен порядок элементов после удаления дубликатов — намного лучше для производительности сделать сначала стрим неупорядоченным с помощь unordered(), а уже потом применять distinct(), так как подержание стабильности сортировки при параллельном стриме довольно затратно по ресурсам и distinct() на упорядоченным стриме будет выполнятся значительно дольше чем при неупорядоченном,

Детальные примеры




3.3 Примеры использования Match функций (anyMatch, allMatch, noneMatch)

Условие: дана коллекция строк Arrays.asList(«a1», «a2», «a3», «a1»), давайте посмотрим, как её можно обрабатывать используя Match функции

Задача	Код примера	Результат
Найти существуют ли хоть один «a1» элемент в коллекции	collection.stream().anyMatch(«a1»::equals)	true
Найти существуют ли хоть один «a8» элемент в коллекции	collection.stream().anyMatch(«a8»::equals)	false
Найти есть ли символ «1» у всех элементов коллекции	collection.stream().allMatch((s) -> s.contains(«1»))	false
Проверить что не существуют ни одного «a7» элемента в коллекции	collection.stream().noneMatch(«a7»::equals)	true

Детальные примеры




3.4 Примеры использования Map функций (map, mapToInt, FlatMap, FlatMapToInt)

Условие: даны две коллекции collection1 = Arrays.asList(«a1», «a2», «a3», «a1») и collection2 = Arrays.asList(«1,2,0», «4,5»), давайте посмотрим как её можно обрабатывать используя различные map функции

Задача	Код примера	Результат
Добавить "_1" к каждому элементу первой коллекции	collection1.stream().map((s) -> s + "_1").collect(Collectors.toList())	[a1_1, a2_1, a3_1, a1_1]
В первой коллекции убрать первый символ и вернуть массив чисел (int[])	collection1.stream().mapToInt((s) -> Integer.parseInt(s.substring(1))).toArray()	[1, 2, 3, 1]
Из второй коллекции получить все числа, перечисленные через запятую из всех элементов	collection2.stream().flatMap((p) -> Arrays.asList(p.split(",")).stream()).toArray(String[]::new)	[1, 2, 0, 4, 5]
Из второй коллекции получить сумму всех чисел, перечисленных через запятую	collection2.stream().flatMapToInt((p) -> Arrays.asList(p.split(",")).stream().mapToInt(Integer::parseInt)).sum()	12

Обратите внимание: все map функции могут вернуть объект другого типа (класса), то есть map может работать со стримом строк, а на выходе дать Stream из значений Integer или получать класс людей People, а возвращать класс Office, где эти люди работают и т.п., flatMap (flatMapToInt и т.п.) на выходе должны возвращать стрим с одним, несколькими или ни одним элементов для каждого элемента входящего стрима (см. последние два примера).

Детальные примеры




3.5 Примеры использования Sorted функции

Условие: даны две коллекции коллекция строк Arrays.asList(«a1», «a4», «a3», «a2», «a1», «a4») и коллекция людей класса People (с полями name — имя, age — возраст, sex — пол), вида Arrays.asList( new People(«Вася», 16, Sex.MAN), new People(«Петя», 23, Sex.MAN), new People(«Елена», 42, Sex.WOMEN), new People(«Иван Иванович», 69, Sex.MAN)). Давайте посмотрим примеры как их можно сортировать:

Задача	Код примера	Результат
Отсортировать коллекцию строк по алфавиту	collection.stream().sorted().collect(Collectors.toList())	[a1, a1, a2, a3, a4, a4]
Отсортировать коллекцию строк по алфавиту в обратном порядке	collection.stream().sorted((o1, o2) -> -o1.compareTo(o2)).collect(Collectors.toList())	[a4, a4, a3, a2, a1, a1]
Отсортировать коллекцию строк по алфавиту и убрать дубликаты	collection.stream().sorted().distinct().collect(Collectors.toList())	[a1, a2, a3, a4]
Отсортировать коллекцию строк по алфавиту в обратном порядке и убрать дубликаты	collection.stream().sorted((o1, o2) -> -o1.compareTo(o2)).distinct().collect(Collectors.toList())	[a4, a3, a2, a1]
Отсортировать коллекцию людей по имени в обратном алфавитном порядке	peoples.stream().sorted((o1,o2) -> -o1.getName().compareTo(o2.getName())).collect(Collectors.toList())	[{'Петя'}, {'Иван Иванович'}, {'Елена'}, {'Вася'}]
Отсортировать коллекцию людей сначала по полу, а потом по возрасту	peoples.stream().sorted((o1, o2) -> o1.getSex() != o2.getSex()? o1.getSex().
compareTo(o2.getSex()): o1.getAge().compareTo(o2.getAge())).collect(Collectors.toList())	[{'Вася'}, {'Петя'}, {'Иван Иванович'}, {'Елена'}]


Детальные примеры




3.6 Примеры использования Max и Min функций

Условие: дана коллекция строк Arrays.asList(«a1», «a2», «a3», «a1»), и коллекция класса Peoples из прошлых примеров про Sorted и Filter функции.

Задача	Код примера	Результат
Найти максимальное значение среди коллекции строк	collection.stream().max(String::compareTo).get()	a3
Найти минимальное значение среди коллекции строк	collection.stream().min(String::compareTo).get()	a1
Найдем человека с максимальным возрастом	peoples.stream().max((p1, p2) -> p1.getAge().compareTo(p2.getAge())).get()	{name='Иван Иванович', age=69, sex=MAN}
Найдем человека с минимальным возрастом	peoples.stream().min((p1, p2) -> p1.getAge().compareTo(p2.getAge())).get()	{name='Вася', age=16, sex=MAN}


Детальные примеры




3.7 Примеры использования ForEach и Peek функций

Обе ForEach и Peek по сути делают одно и тоже, меняют свойства объектов в стриме, единственная разница между ними в том что ForEach терминальная и она заканчивает работу со стримом, в то время как Peek конвейерная и работа со стримом продолжается. Например, есть коллекция:
        Collection<StringBuilder> list = Arrays.asList(new StringBuilder("a1"), new StringBuilder("a2"), new StringBuilder("a3"));

И нужно добавить к каждому элементу "_new", то для ForEach код будет
        list.stream().forEachOrdered((p) -> p.append("_new")); // list - содержит [a1_new, a2_new, a3_new]

а для peek код будет
  List<StringBuilder> newList = list.stream().peek((p) -> p.append("_new")).collect(Collectors.toList()); // и list и newList содержат [a1_new, a2_new, a3_new]


Детальные примеры




3.8 Примеры использования Reduce функции

Метод reduce позволяет выполнять агрегатные функции на всей коллекцией (такие как сумма, нахождение минимального или максимального значение и т.п.), он возвращает одно значение для стрима, функция получает два аргумента — значение полученное на прошлых шагах и текущее значение.

Условие: Дана коллекция чисел Arrays.asList(1, 2, 3, 4, 2) выполним над ними несколько действий используя reduce.

Задача	Код примера	Результат
Получить сумму чисел или вернуть 0	collection.stream().reduce((s1, s2) -> s1 + s2).orElse(0)	12
Вернуть максимум или -1	collection.stream().reduce(Integer::max).orElse(-1)	4
Вернуть сумму нечетных чисел или 0	collection.stream().filter(o -> o % 2 != 0).reduce((s1, s2) -> s1 + s2).orElse(0)	4


Детальные примеры




3.9 Примеры использования toArray и collect функции

Если с toArray все просто, можно либо вызвать toArray() получить Object[], либо toArray(T[]::new) — получив массив типа T, то collect позволяет много возможностей преобразовать значение в коллекцию, map'у или любой другой тип. Для этого используются статические методы из Collectors, например преобразование в List будет stream.collect(Collectors.toList()).

Давайте рассмотрим статические методы из Collectors:
Метод	Описание
toList, toCollection, toSet	представляют стрим в виде списка, коллекции или множества
toConcurrentMap, toMap	позволяют преобразовать стрим в map
averagingInt, averagingDouble, averagingLong	возвращают среднее значение
summingInt, summingDouble, summingLong	возвращает сумму
summarizingInt, summarizingDouble, summarizingLong	возвращают SummaryStatistics с разными агрегатными значениями
partitioningBy	разделяет коллекцию на две части по соответствию условию и возвращает их как Map<Boolean, List>
groupingBy	разделяет коллекцию на несколько частей и возвращает Map<N, List<T>>
mapping	дополнительные преобразования значений для сложных Collector'ов


Теперь давайте рассмотрим работу с collect и toArray на примерах:
Условие: Дана коллекция чисел Arrays.asList(1, 2, 3, 4), рассмотрим работу collect и toArray с ней
Задача	Код примера	Результат
Получить сумму нечетных чисел	numbers.stream().collect(Collectors.summingInt(((p) -> p % 2 == 1? p: 0)))	4
Вычесть от каждого элемента 1 и получить среднее	numbers.stream().collect(Collectors.averagingInt((p) -> p — 1))	1.5
Прибавить к числам 3 и получить статистику	numbers.stream().collect(Collectors.summarizingInt((p) -> p + 3))	IntSummaryStatistics{count=4, sum=22, min=4, average=5.5, max=7}
Разделить числа на четные и нечетные	numbers.stream().collect(Collectors.partitioningBy((p) -> p % 2 == 0))	{false=[1, 3], true=[2, 4]}


Условие: Дана коллекция строк Arrays.asList(«a1», «b2», «c3», «a1»), рассмотрим работу collect и toArray с ней
Задача	Код примера	Результат
Получение списка без дубликатов	strings.stream().distinct().collect(Collectors.toList())	[a1, b2, c3]
Получить массив строк без дубликатов и в верхнем регистре	strings.stream().distinct().map(String::toUpperCase).toArray(String[]::new)	{A1, B2, C3}
Объединить все элементы в одну строку через разделитель: и обернуть тегами <b>… </b>	strings.stream().collect(Collectors.joining(": ", "<b> ", " </b>"))	<b> a1: b2: c3: a1 </b>
Преобразовать в map, где первый символ ключ, второй символ значение	strings.stream().distinct().collect(Collectors.toMap((p) -> p.substring(0, 1), (p) -> p.substring(1, 2)))	{a=1, b=2, c=3}
Преобразовать в map, сгруппировав по первому символу строки	strings.stream().collect(Collectors.groupingBy((p) -> p.substring(0, 1)))	{a=[a1, a1], b=[b2], c=[c3]}
Преобразовать в map, сгруппировав по первому символу строки и объединим вторые символы через :	strings.stream().collect(Collectors.groupingBy((p) -> p.substring(0, 1), Collectors.mapping((p) -> p.substring(1, 2), Collectors.joining(":"))))	{a=1:1, b=2, c=3}


Детальные примеры




3.10 Пример создания собственного Collector'a

Кроме Collector'ов уже определенных в Collectors можно так же создать собственный Collector, Давайте рассмотрим пример как его можно создать.

Метод определения пользовательского Collector'a:

Collector<Тип_источника, Тип_аккумулятора, Тип_результата> сollector =  Collector.of(
                метод_инициализации_аккумулятора,
                метод_обработки_каждого_элемента,
                метод_соединения_двух_аккумуляторов,
                [метод_последней_обработки_аккумулятора]
        );


Как видно из кода выше, для реализации своего Collector'a нужно определить три или четыре метода (метод_последней_обработки_аккумулятора не обязателен). Рассмотрим следующий кода, который мы писали до Java 8, чтобы объединить все строки коллекции:

        StringBuilder b = new StringBuilder(); // метод_инициализации_аккумулятора
        for(String s: strings) {
            b.append(s).append(" , "); // метод_обработки_каждого_элемента,
        }
        String joinBuilderOld = b.toString(); // метод_последней_обработки_аккумулятора


И аналогичный код, который будет написан в Java 8

String joinBuilder = strings.stream().collect(
   Collector.of(
                StringBuilder::new, // метод_инициализации_аккумулятора
                (b ,s) -> b.append(s).append(" , "), // метод_обработки_каждого_элемента,
                (b1, b2) -> b1.append(b2).append(" , "), // метод_соединения_двух_аккумуляторов
                StringBuilder::toString // метод_последней_обработки_аккумулятора
        )
);


В общем-то, три метода легко понять из кода выше, их мы писали практически при каждой обработки коллекций, но вот что такое метод_соединения_двух_аккумуляторов? Это метод который нужен для параллельной обработки Collector'a, в данном случае при параллельном стриме коллекция может быть разделенной на две части (или больше частей), в каждой из которых будет свой аккумулятор StringBuilder и потом необходимо будет их объединить, то код до Java 8 при 2 потоках будет таким:

        StringBuilder b1 = new StringBuilder(); // метод_инициализации_аккумулятора_1
        for(String s: stringsPart1) { //  stringsPart1 - первая часть коллекции strings
            b1.append(s).append(" , "); // метод_обработки_каждого_элемента,
        }

        StringBuilder b2 = new StringBuilder(); // метод_инициализации_аккумулятора_2
        for(String s: stringsPart2) { //  stringsPart2 - вторая часть коллекции strings
            b2.append(s).append(" , "); // метод_обработки_каждого_элемента,
        }

        StringBuilder b = b1.append(b2).append(" , "), // метод_соединения_двух_аккумуляторов

        String joinBuilderOld = b.toString(); // метод_последней_обработки_аккумулятора


Напишем свой аналог Collectors.toList() для работы со строковым стримом:
 // Напишем свой аналог toList
        Collector<String, List<String>, List<String>> toList = Collector.of(
                ArrayList::new, // метод инициализации аккумулятора
                List::add, // метод обработки каждого элемента
                (l1, l2) -> { l1.addAll(l2); return l1; } // метод соединения двух аккумуляторов при параллельном выполнении
        );
// Используем его для получение списка строк без дубликатов из стрима
        List<String> distinct1 = strings.stream().distinct().collect(toList);


Детальные примеры



IV. Заключение

Вот и все. Надеюсь, моя небольшая шпаргалка по работе со stream api была для вас полезной. Все исходники есть на github'е, удачи в написании хорошего кода.

P.S. Список других статей, где можно прочитать дополнительно про Stream Api:
1. Processing Data with Java SE 8 Streams, Part 1 от Oracle,
2. Processing Data with Java SE 8 Streams, Part 2 от Oracle,
3. Полное руководство по Java 8 Stream

P.P.S. Так же советую посмотреть мой opensource проект useful-java-links — возможно, наиболее полная коллекция полезных Java библиотек, фреймворков и русскоязычного обучающего видео. Так же есть аналогичная английская версия этого проекта и начинаю opensource подпроект Hello world по подготовке коллекции простых примеров для разных Java библиотек в одном maven проекте (буду благодарен за любую помощь).

Общее оглавление 'Шпаргалок'
 */