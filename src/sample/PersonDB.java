package sample;

import java.sql.*;
import java.util.ArrayList;

public class PersonDB {
    private static final String CONN_STRING = "jdbc:mysql://localhost/sample";

    private static Connection getConnection() {
        Connection conn = null;

        try {
            // Load the driver
            Class.forName("com.mysql.jdbc.Driver");

            // Define your Connection object
            conn = DriverManager.getConnection(CONN_STRING, "admin", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static void print(String text, Person person) {
        System.out.println(text + "\n" +
                "ID: " + person.getId() + " | Name: " + person.getName() +
                "\nNickname: " + person.getNickname() + " | Age: " + person.getAge() + "\n\n");
    }
    public static ArrayList<Person> getPersonList() {
        ArrayList<Person> returnValue = new ArrayList<>();
        Connection conn = null;

        try {
            // Define your Connection object
            conn = getConnection();

            // Create a Statement object
            Statement stmt = conn.createStatement();

            // Execute the statement
            ResultSet rs = stmt.executeQuery("SELECT * FROM persons");

            // Create a Metadata object from the ResultSet object
            ResultSetMetaData metaData = rs.getMetaData();

            // Process the data
            while (rs.next()) {

                int id = rs.getInt(1);
                String name = rs.getString(2);

                String nickname = rs.getString(3);  // nullable
                if(rs.wasNull())
                    nickname = null;

                Integer age = rs.getInt(4);         // nullable
                if(rs.wasNull())
                    age = null;

                returnValue.add(new Person(id, name, nickname, age));
            }

            // Close the connection
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return returnValue;
    }
    public static boolean updatePerson(Person oldPerson, Person newPerson) {
        boolean isSuccess = true;

        print("New Person", newPerson);
        print("Old Person", oldPerson);
        try {
            // Load the driver
            Class.forName("com.mysql.jdbc.Driver");

            // Define your Connection object
            Connection conn = getConnection();

            // Build the sql query
            String sql = "UPDATE persons " +
                    "SET `name` = ?," +
                    "`nickname` = ?, " +
                    "`age` = ? " +
                    "WHERE `id` = ? " +
                    "AND `name` = ? " +
                    "AND (`nickname` = ? OR " +
                         "`nickname` IS NULL AND ? IS NULL) " +
                    "AND (`age` = ? OR " +
                        "`age` IS NULL AND ? IS NULL)";


            // Create a Prepared Statement object
            PreparedStatement stmt = conn.prepareStatement(sql);

            //-----------------------------------------------------------------------------------
            // ASSIGNMENT
            stmt.setString(1, newPerson.getName());

            // Nullable - nickname
            if(newPerson.getNickname() == null || newPerson.getNickname().isEmpty()) {
                System.out.println("Nickname is null");
                stmt.setNull(2, Types.VARCHAR);
            }
            else {
                stmt.setString(2, newPerson.getNickname());
            }

            // Nullable - age
            if(newPerson.getAge() == null || newPerson.getAge() == 0) {
                System.out.println("Age is null");
                stmt.setNull(3, Types.INTEGER);
            }
            else {
                stmt.setInt(3, newPerson.getAge());
            }

            // ID
            stmt.setInt(4, oldPerson.getId());

            //-----------------------------------------------------------------------------------
            // OPTIMISTIC CONCURRENCY

            // name
            stmt.setString(5, oldPerson.getName());

            // nickname - nullable String
            stmt.setString(6, oldPerson.getNickname());
            stmt.setString(7, oldPerson.getNickname());


            // age - nullable Integer
            if(oldPerson.getAge() == null || oldPerson.getAge() == 0) {
                System.out.println("Age is null");
                stmt.setNull(8, Types.INTEGER);
            }
            else {
                stmt.setInt(8, oldPerson.getAge());
            }
            if(oldPerson.getAge() == null || oldPerson.getAge() == 0) {
                System.out.println("Age is null");
                stmt.setNull(9, Types.INTEGER);
            }
            else {
                stmt.setInt(9, oldPerson.getAge());
            }


            //-----------------------------------------------------------------------------------
            // Execute the statement
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                isSuccess = false;
            }

            // Close the connection
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }
}