package ru.gb.lesson3.hw;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Homework {

  /**
   * С помощью JDBC, выполнить следующие пункты:
   * 1. Создать таблицу Person (скопировать код с семниара)
   * 2. Создать таблицу Department (id bigint primary key, name varchar(128) not null)
   * 3. Добавить в таблицу Person поле department_id типа bigint (внешний ключ)
   * 4. Написать метод, который загружает Имя department по Идентификатору person
   * 5. * Написать метод, который загружает Map<String, String>, в которой маппинг person.name -> department.name
   *   Пример: [{"person #1", "department #1"}, {"person #2", "department #3}]
   * 6. ** Написать метод, который загружает Map<String, List<String>>, в которой маппинг department.name -> <person.name>
   *   Пример:
   *   [
   *     {"department #1", ["person #1", "person #2"]},
   *     {"department #2", ["person #3", "person #4"]}
   *   ]
   *
   *  7. *** Создать классы-обертки над таблицами, и в пунктах 4, 5, 6 возвращать объекты.
   */

  /**
   * Пункт 4
   */
  public static void main(String[] args) {
    try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test1")) {
     createTablePerson(connection);
     createTableDepartament(connection);
     insertDataPerson(connection);
     insertDataDepartment(connection);
     TableUpdatePerson(connection);
      System.out.println(getPersonDepartmentName(connection, 1));
     System.out.println(getDepartmentPersons(connection));
     getPersonDepartments(connection);
     getPersonDepartmentName(connection, 1);
    } catch (SQLException e) {
      System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
    }
  }
  private static void createTablePerson(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute("""
        create table person (
          id bigint primary key,
          name varchar(256),
          age integer,
          active boolean
        )
        """);
    } catch (SQLException e) {
      System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
      throw e;
    }
  }

  private static void createTableDepartament(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute("""
        create table department (
          id bigint primary key,
          name varchar(128)
        )
        """);
    } catch (SQLException e) {
      System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
      throw e;
    }
  }
  private static void insertDataPerson(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      StringBuilder insertQuery = new StringBuilder("insert into person(id, name, age, active) values\n");
      for (int i = 1; i <= 10; i++) {
        int age = ThreadLocalRandom.current().nextInt(20, 60);
        boolean active = ThreadLocalRandom.current().nextBoolean();
        insertQuery.append(String.format("(%s, '%s', %s, %s)", i, "Person #" + i, age, active));
        System.out.println(insertQuery.toString());
        if (i != 10) {
          insertQuery.append(",\n");
        }
      }

      int insertCount = statement.executeUpdate(insertQuery.toString());
      System.out.println("Вставлено строк: " + insertCount);
    }
  }

  private static void insertDataDepartment(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      StringBuilder insert_query = new StringBuilder("insert into department(id, name) values\n");
      for (int i = 1; i <= 10; i++) {
        insert_query.append(String.format("(%s, '%s')", i, "Department #" + i));
        if (i != 10) {
          insert_query.append(",\n");
          System.out.println(insert_query.toString());
        }
      }
      int insertCount1 = statement.executeUpdate(insert_query.toString());
      System.out.println("Вставлено строк: " + insertCount1);
    }
  }

  private static void TableUpdatePerson(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate("""
        alter table person add column department_id bigint;
        alter table person add foreign key (department_id) references department(id);
        update person set department_id = id;
        """);
      PreparedStatement preparedStatement = connection.prepareStatement("select * from person");
      preparedStatement.execute();
      ResultSet resultSet = preparedStatement.getResultSet();
      while (resultSet.next()) {
        System.out.println(resultSet.getString("department_id"));
      }
    } catch (SQLException e) {
      System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
      throw e;
    }
  }

  private static String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
    // FIXME: Ваш код тут
    try (Statement statement = connection.createStatement()) {
//      statement.executeQuery("select name from person where id = " + personId);
      PreparedStatement preparedStatement = connection.prepareStatement("select name from person where id = ?");
      preparedStatement.setLong(1, personId);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString("name");
      }
    }
    throw new UnsupportedOperationException();
  }

  /**
   * Пункт 5
   */
  private static Map<String, String> getPersonDepartments(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      PreparedStatement preparedStatement = connection.prepareStatement("""
      select p.name, d.name 
      from person as p 
      inner join department as d on p.department_id = d.id""");
      preparedStatement.execute();
    ResultSet resultSet = preparedStatement.getResultSet();
    Map<String, String> map = new HashMap<>();
    while (resultSet.next()) {
      map.put(resultSet.getString("name"), resultSet.getString("name"));
    }
      resultSet.close();
    throw new UnsupportedOperationException();
  }
  }
  /**
   * Пункт 6
   */
  private static Map<String, List<String>> getDepartmentPersons(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      PreparedStatement preparedStatement = connection.prepareStatement("""
      select p.name, d.name 
      from person as p 
      inner join department as d on p.department_id = d.id""");
      preparedStatement.execute();
    ResultSet resultSet = preparedStatement.getResultSet();
    Map<String, List<String>> map = new HashMap<>();
    while (resultSet.next()) {
      map.put(resultSet.getString("name"), List.of(resultSet.getString("name")));
    }
      resultSet.close();
      throw new UnsupportedOperationException();
    }
  }
    // FIXME: Ваш код тут


}
