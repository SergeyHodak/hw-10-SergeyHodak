package lectureNotes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClassOptional {
}
/*
    Класс Optional#
    Одна из наиболее неприятных вещей в программировании на Java - это NullPointerException, или же сокращенно - NPE.
    Эта ошибка возникает тогда, когда мы пытаемся использовать объект, но он не инициализирован, и значение ссылки на
    объект - null.

    Например, код ниже вызовет исключение NPE:
*/
class Test4 {
    public static void main(String[] args) {
        List<String> names = null;
        System.out.println(names.get(0));
    }
}

/*
    Поскольку в объекте names хранится значение null, получить значение элемента с индексом 0 мы не можем, и
    выбрасывается исключение NullPointerException. Чтобы избежать NullPointerException, в потенциально возможных к
    такой проблеме местах программы мы пишем проверки на null:
*/
class Test5 {
    public static void main(String[] args) {
        List<String> names = null;
        if (names != null) {
            System.out.println(names.get(0));
        } else {
            System.out.println("names is null");
        }
    }
}
/*

    Класс Optional#
    Подход с проверкой объекта на null не всегда удобен. Чтобы явно обрабатывать ситуацию, когда объект может быть null,
    есть объект-обертка Optional. По сути, он оборачивает полученный объект, и дальше у объекта Optional мы можем узнать
    - есть ли объект, и что делать, если объекта нет (он равен null).
*/
class Test6 {
    public static void main(String[] args) {
        //Optional<User> user = Optional.of(database.getUser("admin"));
    }
}
/*

    Получаем объект из Optional#,
    Чтобы получить объект, мы используем метод get():
*/
class Test7 {
    public static void main(String[] args) {
//        Optional<User> user = Optional.of(database.getUser("admin"));
//        User unwrapped = user.get();
    }
}
/*

    Проверяем объект перед получением#
    Если мы хотим вначале проверить, есть ли объект, есть метод isPresent():
*/
class Test8 {
    public static void main(String[] args) {
//        Optional<User> user = Optional.of(database.getUser("admin"));
//
//        if (user.isPresent()) {
//            User unwrapped = user.get();
//        } else {
//            System.out.println("User not found");
//        }
    }
}
/*

    Возвращаем объект, даже если он равен null#
    Если объект null, и в этом случае мы знаем, какой объект возвращать - мы используем метод orElse(). Этот метод
    возвращает объект из обертки, а если этого объекта нет - то возвращается объект, который мы передали в метод
    orElse():
*/
class Test9 {
    public static void main(String[] args) {
        String name = null;
        System.out.println(Optional.of(name).orElse("Bill"));
    }
}
/*
    В консоль в коде выше выведется текст Bill, потому что переменная name равна null.

    Также есть методы с похожей семантикой:
    orElseThrow() - выбрасывает исключение, если объекта нет;
    orElseGet() - если объекта нет, то выполняется код, переданный в orElseGet(). Этот код должен возвратить требуемое
    значение. Отличие от метода orElse() в том, что код, переданный в orElseGet() исполнится лишь в случае когда объекта
    нет. Это прием оптимизации по большей мере.

    Optional решает проблему NPE на 100%?#
    Нет, даже используя Optional, вы можете получить NullPointerException.
    Optional - это скорее способ более явно указать что нужно делать, если необходимого объекта нет. Раньше программа
    бы упала с NPE, а если же вы предусмотрели эту ситуацию, используя Optional - у вас есть шанс как-то обработать эту
    ситуацию.

    Дополнительное чтение#
    Ознакомьтесь, пожалуйста, со следующей статьей: https://habr.com/ru/post/346782/
 */

/*
    TODO статья: https://habr.com/ru/post/346782/
    xpendence 22 января 2018 в 08:27. Optional: Кот Шрёдингера в Java 8
    Представим, что в коробке находятся кот, радиоактивное вещество и колба с синильной кислотой. Вещества так мало,
    что в течение часа может распасться только один атом. Если в течение часа он распадётся, считыватель разрядится,
    сработает реле, которое приведёт в действие молоток, который разобьёт колбу, и коту настанет карачун. Поскольку
    атом может распасться, а может и не распасться, мы не знаем, жив ли кот или уже нет, поэтому он одновременно и жив,
    и мёртв. Таков мысленный эксперимент, именуемый «Кот Шрёдингера».

    Класс Optional обладает схожими свойствами — при написании кода разработчик часто не может знать — будет ли
    существовать нужный объект на момент исполнения программы или нет, и в таких случаях приходится делать проверки
    на null. Если такими проверками пренебречь, то рано или поздно (обычно рано) Ваша программа рухнет с
    NullPointerException.

    Коллеги! Статья, как и любая другая, не идеальна и может быть поправлена. Если Вы видите возможность существенного
    улучшения данного материала, укажите её в комментариях.

    Как получить объект через Optional?#

    Как уже было сказано, класс Optional может содержать объект, а может содержать null. К примеру, попытаемся извлечь
    из репозитория юзера с заданным ID:
*/
//User = repository.findById(userId);
/*
    Возможно, юзер по такому ID есть в репозитории, а возможно, нет. Если такого юзера нет, к нам в стектрейс прилетает
    NullPointerException. Не имей мы в запасе класса Optional, нам пришлось бы изобретать какую-нибудь такую конструкцию:
*/

class Test10 {
    public static void main(String[] args) {
//        User user;
//        if (Objects.nonNull(user =  repository.findById(userId))) {
//            (остальная борода пишется тут)
//        }
    }
}
/*
    Согласитесь, не очень. Намного приятнее иметь дело с такой строчкой:
        Optional<User> user = Optional.of(repository.findById(userId));
    Мы получаем объект, в котором может быть запрашиваемый объект — а может быть null. Но с Optional надо как-то
    работать дальше, нам нужна сущность, которую он содержит (или не содержит).
    Cуществует всего три категории Optional:
        Optional.of — возвращает Optional-объект.
        Optional.ofNullable - возвращает Optional-объект, а если нет дженерик-объекта, возвращает пустой Optional-объект.
        Optional.empty — возвращает пустой Optional-объект.

    Существует так же два метода, вытекающие из познания, существует обёрнутый объект или нет — isPresent() и ifPresent();
        .ifPresent()

    Метод позволяет выполнить какое-то действие, если объект не пустой.
        Optional.of(repository.findById(userId)).ifPresent(createLog());

    Если обычно мы выполняем какое-то действие в том случае, когда объект отсутствует (об этом ниже), то здесь как
    раз наоборот.
        .isPresent()

    Этот метод возвращает ответ, существует ли искомый объект или нет, в виде Boolean:
        Boolean present = repository.findById(userId).isPresent();

    Если Вы решили использовать нижеописанный метод get(), то не будет лишним проверить, существует ли данный объект,
    при помощи этого метода, например:
        Optional<User> optionalUser = repository.findById(userId);
        User user = optionalUser.isPresent() ? optionalUser.get() : new User();

    Но такая конструкция лично мне кажется громоздкой, и о более удобных методах получения объекта мы поговорим ниже.

    Как получить объект, содержащийся в Optional?
    Существует три прямых метода дальнейшего получения объекта семейства orElse(); Как следует из перевода, эти методы
    срабатывают в том случае, если объекта в полученном Optional не нашлось.
        orElse() — возвращает объект по дефолту.
        orElseGet() — вызывает указанный метод.
        orElseThrow() — выбрасывает исключение.

    .orElse()
    Подходит для случаев, когда нам обязательно нужно получить объект, пусть даже и пустой. Код, в таком случае,
    может выглядеть так:
        User user = repository.findById(userId).orElse(new User());
    Эта конструкция гарантированно вернёт нам объект класса User. Она очень выручает на начальных этапах познания
    Optional, а также, во многих случаях, связанных с использованием Spring Data JPA (там большинство классов семейства
    find возвращает именно Optional).

    .orElseThrow()
    Очень часто, и опять же, в случае с использованием Spring Data JPA, нам требуется явно заявить, что такого объекта
    нет, например, когда речь идёт о сущности в репозитории. В таком случае, мы можем получить объект или, если его нет,
    выбросить исключение:
        User user = repository.findById(userId).orElseThrow(() -> new NoEntityException(userId));
    Если сущность не обнаружена и объект null, будет выброшено исключение NoEntityException (в моём случае, кастомное).
    В моём случае, на клиент уходит строчка «Пользователь {userID} не найден. Проверьте данные запроса».

    .orElseGet()
    Если объект не найден, Optional оставляет пространство для «Варианта Б» — Вы можете выполнить другой метод, например:
        User user = repository.findById(userId).orElseGet(() -> findInAnotherPlace(userId));
    Если объект не был найден, предлагается поискать в другом месте. Этот метод, как и orElseThrow(), использует Supplier.
    Также, через этот метод можно, опять же, вызвать объект по умолчанию, как и в .orElse():
        User user = repository.findById(userId).orElseGet(() -> new User());

    Помимо методов получения объектов, существует богатый инструментарий преобразования объекта, морально унаследованный
    от stream().

    Работа с полученным объектом.
    Как я писал выше, у Optional имеется неплохой инструментарий преобразования полученного объекта, а именно:
        get() — возвращает объект, если он есть.
        map() — преобразовывает объект в другой объект.
        filter() — фильтрует содержащиеся объекты по предикату.
        flatmap() — возвращает множество в виде стрима.

    .get()
    Метод get() возвращает объект, запакованный в Optional. Например:
        User user = repository.findById(userId).get();
    Будет получен объект User, запакованный в Optional. Такая конструкция крайне опасна, поскольку минует проверку на
    null и лишает смысла само использование Optional, поскольку Вы можете получить желаемый объект, а можете получить
    NPE. Такую конструкцию придётся оборачивать в .isPresent().

    .map()
    Этот метод полностью повторяет аналогичный метод для stream(), но срабатывает только в том случае, если в Optional
    есть не-нулловый объект.
        String name = repository.findById(userId).map(user -> user.getName()).orElseThrow(() -> new Exception());
    В примере мы получили одно из полей класса User, упакованного в Optional.

    .filter()
    Данный метод также позаимствован из stream() и фильтрует элементы по условию.
        List<User> users = repository.findAll().filter(user -> user.age >= 18).orElseThrow(() -> new Exception());

    .flatMap()
    Этот метод делает ровно то же, что и стримовский, с той лишь разницей, что он работает только в том случае, если
    значение не null.

    Заключение
    Класс Optional, при умелом использовании, значительно сокращает возможности приложения рухнуть с NullPoinerException,
    делая его более понятным и компактным, чем как если бы Вы делали бесчисленные проверки на null. А если Вы пользуетесь
    популярными фреймворками, то Вам тем более придётся углублённо изучить этот класс, поскольку тот же Spring гоняет
    его в своих методах и в хвост, и в гриву. Впрочем, Optional — приобретение Java 8, а это значит, что знать его в
    2018 году просто обязательно.
 */