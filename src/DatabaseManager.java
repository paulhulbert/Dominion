import com.fasterxml.jackson.core.JsonParser;

import java.sql.*;

/**
 * Created by paulh on 10/1/2016.
 */
public class DatabaseManager {

    public static void main(String[] args) {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://204.246.56.27/dominion", "dominion", "La9mT6?92K!G");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM actions");


            while (rs.next()) {
                System.out.println(rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
