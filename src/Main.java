import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static String userInput;

    private static final String INSTRUCTION = " 1 - add country to DB \n 2 - add city to DB \n 3 - add person to DB \n 4 - show all persons \n 5 - Find person with id " +
            "\n 6 - Find city with id   \n 8 - Find persons from one city  \n 9 - Find city from one country \n 10 - Show all information about person ";
    private static Connection conn;

    public static void main(String[] args) throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/person_information?useSSL=false";
        String username = "root";
        String password = "1234567890Bodia";

        conn = (Connection) DriverManager.getConnection(dbUrl, username, password);
        System.out.println("Connected? " + !conn.isClosed());
        createTablePerson();
        createTableCity();
        createtableCountry();
        joinTable1();
        joinTable2();
        program();

        conn.close();

    }

    private static void program() throws SQLException {
        System.out.println(INSTRUCTION);
        Scanner sc = new Scanner(System.in);
        while (true) {
            userInput = sc.nextLine();
            switch (userInput) {

                case "1":
                    addCountryToDb(1);
                    System.out.println(INSTRUCTION);
                    break;

                case "2":
                    addCityToDb(1);
                    System.out.println(INSTRUCTION);
                    break;

                case "3":
                    addPersonToDb(1);
                    System.out.println(INSTRUCTION);
                    break;

                case "4":
                    selectPersons();
                    System.out.println(INSTRUCTION);
                    break;

                case "5":
                    selectPersonId();
                    System.out.println(INSTRUCTION);
                    break;

                case "6":
                    selectCityId();
                    System.out.println(INSTRUCTION);
                    break;

                case "7":
                    selectCountryId();
                    System.out.println(INSTRUCTION);
                    break;
                case "8":
                    slectPersonFromOneCity();
                    System.out.println(INSTRUCTION);
                    break;
                case "9":
                    slectCityFromOneCountry();
                    System.out.println(INSTRUCTION);
                    break;
                case "10":
                    allPersonInformation();
                    System.out.println(INSTRUCTION);
                    break;
            }

        }

    }


    private static void joinTable1() throws SQLException {
        String db = " alter table person add foreign key (city_id) references city(id);";
        Statement dbSt = (Statement) conn.createStatement();
        dbSt.execute(db);
        dbSt.close();
    }

    private static void joinTable2() throws SQLException {
        String db = "alter table city add foreign key (country_id) references country(id);";
        Statement dbSt = (Statement) conn.createStatement();
        dbSt.execute(db);
        dbSt.close();
    }

    private static void createTablePerson() throws SQLException {
        String db = "drop table if exists person";
        String query = "create table person("
                + "id int primary key auto_increment,"
                + "name varchar(30),"
                + "city_id int "
                + ");";
        Statement dbSt = (Statement) conn.createStatement();
        Statement stmt = (Statement) conn.createStatement();
        dbSt.execute(db);
        stmt.execute(query);
        stmt.close();
        System.out.println("Table 'person' created ");
    }

    private static void createTableCity() throws SQLException {
        String db = "drop table if exists city";
        String query = "create table city("
                + "id int primary key auto_increment,"
                + "name varchar(30),"
                + "country_id int"
                + ");";

        Statement dbSt = (Statement) conn.createStatement();
        Statement stmt = (Statement) conn.createStatement();
        dbSt.execute(db);
        stmt.execute(query);
        stmt.close();
        System.out.println("Table 'city' created ");

    }

    private static void createtableCountry() throws SQLException {
        String db = "drop table if exists country";
        String query = "create table country("
                + "id int primary key auto_increment,"
                + "name varchar(30)"
                + ");";

        Statement dbSt = (Statement) conn.createStatement();
        Statement stmt = (Statement) conn.createStatement();
        dbSt.execute(db);
        stmt.execute(query);
        stmt.close();
        System.out.println("Table 'country' created ");
    }

    public static void addCountryToDb(int i) throws SQLException {
        System.out.println("Enter country name");
        Scanner sc = new Scanner(System.in);
        String query = "insert into country(name)"
                + "values(?)";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, sc.nextLine() + i);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Country added to DB");
    }

    public static void addCityToDb(int i) throws SQLException {
        System.out.println("Enter City name and country id");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        int country_id = sc.nextInt();
        String query = "insert into city(name, country_id)"
                + "values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name + i);
        pstmt.setInt(2, country_id);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("City added to DB");
    }


    public static void addPersonToDb(int i) throws SQLException {
        System.out.println("Enter person name and city id");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        int city_id = sc.nextInt();
        String query = "insert into person(name , city_id )"
                + "values(? , ? )";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name + i);
        System.out.println("Enter city id");
        pstmt.setInt(2, city_id);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Person added to DB");
    }

    public static void selectPersons() throws SQLException {

        String query = "select * from person";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<String> persons = new ArrayList<>();

        while (rs.next()) {
            //System.out.println(rs.getString("first_name"));

            persons.add("id: " + rs.getInt("id") + "\t |"
                    + "name" + rs.getString("name") + "\t |"
                    + "city_id" + rs.getInt("city_id"));
        }

        persons.forEach(System.out::println);

        rs.close();
        pstmt.close();
    }


    private static void selectPersonId() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String query = "select * from person  where id=?;";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery(); // зберігає результати

        while (rs.next()) {
            //System.out.println(rs.getString("first_name"));
            System.out.println("id: " + rs.getInt("id") + "\t |"
                    + "name: " + rs.getString("name") + "\t |"
                    + "city_id: " + rs.getInt("city_id"));
        }
        rs.close();
        pstmt.close();
    }

    private static void selectCityId() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String query = "select * from city  where id=?;";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery(); // зберігає результати

        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + "\t|"
                    + "name: " + rs.getString("name") + "\t|"
                    + "country_id: " + rs.getInt("country_id"));
        }
        rs.close();
        pstmt.close();
    }

    private static void selectCountryId() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String query = "select * from country where id=?;";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery(); // зберігає результати

        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + "\t|"
                    + "name: " + rs.getString("name") + "\t|");
        }
        rs.close();
        pstmt.close();
    }

    private static void slectPersonFromOneCity() throws SQLException {
        String query = "select * from person p join city c on p.city_id = c.id ; ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<String> persons = new ArrayList<>();
        while (rs.next()) {
            //System.out.println(rs.getString("first_name"));

            persons.add("id: " + rs.getInt("id") + "\t |"
                    + "name: " + rs.getString("name") + "\t |"
                    + "city_id: " + rs.getInt("city_id"
                    + "name: " + rs.getString("name") + "\t |"));

        }
        rs.close();
        pstmt.close();
    }
    private static void slectCityFromOneCountry() throws SQLException {
        String query = "select * from city c join country co on c.country_id = co.id ; ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<String> persons;
        persons = new ArrayList<>();
        while (rs.next()) {
            //System.out.println(rs.getString("first_name"));

            persons.add("id: " + rs.getInt("id") + "\t |"
                    + "name: " + rs.getString("name") + "\t |"
                    + "country_id: " + rs.getInt("country_id"
                    + "name: " + rs.getString("name") + "\t |"));

        }
        rs.close();
        pstmt.close();
    }

    private static void allPersonInformation() throws SQLException {
        String query = "select * from person p , city c , country co  join city c on p.city_id = c.id join country co on c.country_id = co.id ; ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        List<String> persons = new ArrayList<>();
        while (rs.next()) {
            //System.out.println(rs.getString("first_name"));

            persons.add("id: " + rs.getInt("id") + "\t |"
                    + "name: " + rs.getString("name") + "\t |"
                    + "city_id: " + rs.getInt("city_id"
                    + "country_id: " + rs.getInt("country_id") + "\t |"
                    + "name: " + rs.getString("name") + "\t |"));

        }
        rs.close();
        pstmt.close();
    }
}




