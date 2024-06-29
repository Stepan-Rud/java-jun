package ru.gb.lesson1.hw;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Homework {
  public static void main(String[] args) {
    Person person = new Person();
    person.setName("John");
    person.setAge(25);
    person.setSalary(20000.0);
    person.setDepart(new Department());
    person.getDepart().setName("HR");
    Person person2 = new Person();
    person2.setName("Jane");
    person2.setAge(30);
    person2.setSalary(80000.0);
    person2.setDepart(new Department());
    person2.getDepart().setName("IT");
    Person person3 = new Person();
    person3.setName("Alex");
    person3.setAge(35);
    person3.setSalary(10000.0);
    person3.setDepart(new Department());
    person3.getDepart().setName("HR");
    List<Person> people = List.of(person, person2, person3);
    System.out.println(findMostExpensiveDepartment(people));
    System.out.println(groupByDepartmentName(people));
    System.out.println(getDepartmentOldestPerson(people));
    System.out.println(cheapPersonsInDepartment(people));

  }

  private static class Department {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Department{" +
              "name='" + name + '\'' +
              '}';
    }

    // TODO: геттеры, сеттеры
  }

  private static class Person {
    private String name;
    private int age;
    private double salary;
    private Department depart;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public double getSalary() {
      return salary;
    }

    public void setSalary(double salary) {
      this.salary = salary;
    }

    public Department getDepart() {
      return depart;
    }

    public void setDepart(Department depart) {
      this.depart = depart;
    }

    @Override
    public String toString() {
      return "Person{" +
              "name='" + name + '\'' +
              ", age=" + age +
              ", salary=" + salary +
              ", depart=" + depart +
              '}';
    }

    // TODO: геттеры, сеттеры
  }

  /**
   * Найти самого молодого сотрудника
   */
  static Optional<Person> findMostYoungestPerson(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .min((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()));
  }

  /**
   * Найти департамент, в котором работает сотрудник с самой большой зарплатой
   */
  static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return Optional.ofNullable(people.stream()
            .filter(o -> o.getSalary() != 0.0).max(((o1, o2) -> Double.compare(o1.getSalary(), o2.getSalary())))
            .get().getDepart());

  }

  /**
   * Сгруппировать сотрудников по департаментам
   */
  static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .collect(Collectors
                    .groupingBy(Person::getDepart, Collectors.toList()));
  }

  /**
   * Сгруппировать сотрудников по названиям департаментов
   */
  static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .collect(Collectors.groupingBy(o1 -> o1.getDepart().getName(), Collectors.toList()));
  }

  /**
   * В каждом департаменте найти самого старшего сотрудника
   */
  static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
    return people.stream()
            .collect(Collectors.toMap(
                    o1 -> o1.getDepart().getName(),
                    o1 -> o1,
                    (a, b) -> {
                      if (a.getAge() > b.getAge()) {
                        return a;
                      }
                      return b;
                    }
            ));
  }
//  static Map<String, Optional<Person>> getDepartmentOldestPerson(List<Person> people) {
//    // FIXME: ваша реализация здесь
//    return  people.stream()
//            .collect(Collectors.groupingBy(o1 -> o1.getDepart().getName(),
//                    Collectors.maxBy(Comparator.comparing(Person::getAge))));
//  }

  /**
   * *Найти сотрудников с минимальными зарплатами в своем отделе
   * (прим. можно реализовать в два запроса)
   */
  static List<Optional<Person>> cheapPersonsInDepartment(List<Person> people) {
    // FIXME: ваша реализация здесь
    return people.stream()
            .filter(o -> o.getSalary() != 0.0)
            .collect(Collectors.groupingBy(o1 -> o1.getDepart().getName(),
                    Collectors.minBy(Comparator.comparing(Person::getSalary))))
            .values().stream().toList();
  }

}