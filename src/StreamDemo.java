import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import model.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) throws IOException {
        /*Stream 初始化的几种方式*/
        Stream stream = Stream.of("a", "b", "c");
       /*Arrays*/
        String[] strArray = new String[]{"a", "b", "c"};
        stream = Stream.of(strArray);
        stream = Arrays.stream(strArray);
       /*Collection*/
        List<String> list = Arrays.asList(strArray);
        stream = list.stream();


        /*数值流的构造*/
        IntStream.of(new int[]{1, 3, 4}).forEach(System.out::println);
        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.rangeClosed(1, 3).forEach(System.out::println);

        /*流转换为其他数据结构*/
        /*Array*/
        String[] strArray1 = (String[]) stream.toArray(String[]::new);
        /*Collection*/
        List<String> list1 = (List<String>) stream.collect(Collectors.toList());
        List<String> list2 = (List<String>) stream.collect(Collectors.toCollection(LinkedList::new));
        Set<String> set = (Set<String>) stream.collect(Collectors.toSet());
        Stack<String> stack = (Stack<String>) stream.collect(Collectors.toCollection(Stack::new));
        /*String*/
        String str = stream.collect(Collectors.joining()).toString();
        /*一个 Stream 只可以使用一次，上面的代码为了简洁而重复使用了数次。*/


        /*Stream 典型用法*/
        /*转换大写*/
        List<String> output = list.stream().
                map(String::toUpperCase).
                collect(Collectors.toList());

        /*平方数*/
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> result = nums.stream().
                map(n -> n * n).
                collect(Collectors.toList());

        /*一对多映射*/
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6));
        Stream<Integer> outputStream = inputStream.
                flatMap(Collection::stream);

        /*留下偶数*/
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Integer[] evens = Stream.of(sixNums).filter(n -> n % 2 == 0).toArray(Integer[]::new);

        /*把单词挑出来*/
        BufferedReader reader = new BufferedReader(new ASCIIReader(null, null, null));
        List<String> output1 = reader.lines().
                flatMap(line -> Stream.of(line.split(","))).
                filter(word -> word.length() > 0).
                collect(Collectors.toList());


        /*打印姓名*/
        List<Person> personList = new LinkedList<>();
        /*java8*/
        personList.stream().
                filter(p -> p.getGender() == 0).
                forEach(System.out::println);
        /*java8 之前*/
        for (Person person : personList) {
            if (person.getGender() == 0) {
                System.out.println(person);
            }
        }


        /*peek 对每个元素执行操作并返回一个新的 Stream*/
        Stream.of("one", "two", "three", "four", "five").
                filter(x -> x.length() > 3).
                peek(x -> System.out.println("filtered value:" + x)).
                map(String::toUpperCase).
                peek(x -> System.out.println("Mapped value:" + x)).
                collect(Collectors.toList());

        /*Optional的两个用例*/
        String strA = " abcd ", strB = null;
        print(strA);
        print("");
        print(strB);
        System.out.println(getLength(strA));
        System.out.println(getLength(""));
        System.out.println(getLength(strB));


        /*reduce示例*/
        Integer sum = Stream.of(1, 2, 3, 4, 5).reduce(0, (a, b) -> a + b);
        Integer sum1 = Stream.of(1, 2, 3, 4, 5).reduce(0, Integer::sum);

        /*字符串连接，concat = "ABCD"*/
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        /*求最小值，minValue = - 3.0*/
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        /*求和，sumValue = 10 有起始值*/
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        /*求和，sumValue = 10 无起始值*/
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        /*过滤，字符串连接，concat = "ace"*/
        concat = Stream.of("a", "B", "c", "D", "e", "F").
                filter(x -> x.compareTo("Z") > 0).
                reduce("", String::concat);


        testLimitAndSkip();
        testLimitAndSkip2();

        /*找出最长的一行长度 max/min/distinct*/
        BufferedReader br = new BufferedReader(new FileReader("D:\\data.log"));
        int longest = br.lines().mapToInt(String::length).max().orElse(0);
        br.close();
        System.out.println(longest);

        /*找出全文的单词，转小写，并排序*/
        List<String> words = br.lines().flatMap(line ->Stream.of(line.split(" "))).
                filter(word -> word.length() > 0).map(String::toLowerCase).distinct().
                sorted().collect(Collectors.toList());
        br.close();
        System.out.println(words);

        /*检测名称编号大于3的*/
        List<Person> persons = new LinkedList<>();
        persons.add(new Person(0,"name1"));
        persons.add(new Person(0,"name2"));
        persons.add(new Person(0,"name3"));
        persons.add(new Person(0,"name4"));
        persons.add(new Person(0,"name5"));
        boolean isAllMoreThan3 = persons.stream().
                allMatch(p -> p.getName().compareTo("name3") > 0);
        System.out.println("All more than 3 ? " + isAllMoreThan3);
        boolean isAnyMoreThan3 = persons.stream().
                anyMatch(p -> p.getName().compareTo("name3") >0);
        System.out.println("Is there any more than 3? " + isAnyMoreThan3);

        /*使用supplier生成10个随机整数*/
        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random).limit(10).
                forEach(System.out::println);
        /*Another way*/
        IntStream.generate(() -> (int)(System.nanoTime()%100)).
                limit(10).forEach(System.out::println);


        /*自实现supplier*/
        Stream.generate(new PersonSupplier()).limit(10).
                forEach(p -> System.out.println(p.getName()));

        /*使用iterate生成一个等差数列*/
        Stream.iterate(0, m -> m+3).limit(10).forEach(System.out::println);

        /*使用groupingBy按照姓名分组*/
        Map<String,List<Person>> personGroups = Stream.generate(new PersonSupplier()).
                limit(100).collect(Collectors.groupingBy(Person::getName));
        /*按照指定条件归组,将name10作为分界*/
        Map<Boolean,List<Person>> children = Stream.generate(new PersonSupplier()).limit(20).
                collect(Collectors.partitioningBy(person -> person.getName().compareTo("name10") > 0));




    }

    private static class PersonSupplier implements Supplier<Person>{
        private int index = 0;
        private Random random = new Random();
        @Override
        public Person get() {
            return new Person(index++,"name"+random.nextInt(100));
        }
    }



    /*limit和skip对运行次数的影响*/
    public static void testLimitAndSkip() {
        List<Person> persons = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            Person person = new Person();
            person.setGender(2);
            person.setName("name" + i);
            persons.add(person);
        }

        persons.stream().map(Person::getName).
                limit(10).skip(3).collect(Collectors.toList()).
                forEach(System.out::println);

    }

    /*limit和skip对运行次数的影响*/
    public static void testLimitAndSkip2() {
        List<Person> persons = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            Person person = new Person();
            person.setGender(1);
            person.setName("name" + i);
            persons.add(person);
        }

        List<Person> list = persons.stream().
                sorted(Comparator.comparing(Person::getName)).
                limit(2).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(list));
    }


    public static void print(String text) {
    /*Java8*/
        Optional.ofNullable(text).ifPresent(System.out::println);
        /*Java8 之前版本*/
        if (text != null) {
            System.out.println(text);
        }
    }

    public static int getLength(String text) {
        /*Java8*/
        return Optional.ofNullable(text).map(String::length).orElse(-1);
        /*Java8之前*/
//        return text != null ? text.length() : -1;
    }


}
