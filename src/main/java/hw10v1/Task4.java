package hw10v1;

import java.util.stream.LongStream;
import static java.lang.Math.pow;

public class Task4 {
    public static void main(String[] args) {
        Randomm r = new Randomm(25214903917L, (long) pow(2, 48)); // экземпляр собственного random
        LongStream.iterate(11, (seed) -> r.c(seed).next()) // запуск бесконечного потока, начиная с 11
                .limit(10) // сколько показать вариаций
                .forEach(System.out::println); // вывести в консоль вариацию
    }
}

class Randomm {
    private long a, m, c;

    public Randomm(long a, long m) { // конструктор
        this.a = a; // задать начальное значение для поля а
        this.m = m; // задать начальное значение для поля м
    }

    public Randomm c(long c) { // сет-гет метод
        this.c = c; // присвоить полю "с" входное значение методу
        return this;
    }

    public long next() { // генератор следующего значения относительно последнего
        return 1 * (a * c + c) % m;
    }

    public static void main(String[] args) {
        Randomm test = new Randomm(25214903917L, (long) pow(2, 48));
        test.c(11);
        System.out.println(test.a);
        System.out.println(test.m);
        System.out.println(test.c);
        System.out.println(test.next());
    }
}