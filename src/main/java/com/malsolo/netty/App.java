package com.malsolo.netty;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Comparator;

import static java.util.Comparator.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        Arrays.asList(
                new Person(5, "Niña", "W"),
                new Person(20, "Chica", "W"),
                new Person(30, "Señor, la pelota", "M"),
                new Person(30, "Señoraaaa", "W")
        ).stream()
                .filter(p -> "W".equals(p.getGender()))
                .sorted(comparing(Person::getAge))
                .map(Person::getName)
                .forEach(System.out::println);

        System.out.println( "Hello World!" );
    }
}

@Data
class Person {
    private final int age;
    private final String name;
    private final String gender;
}
