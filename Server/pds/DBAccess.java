package pds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class DBAccess {

    private static final String url = "jdbc:mysql://localhost:3306/pds";
    private static final String user = "root";
    private static final String password = "bikoy";

    /**
     *
     * This method insert the request send by any client in the database.
     *
     */
    static void create(String requestToSaveInDB) {
        try {
            // we load the driver that will allow us to connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");

            // we get a connection from the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // we define the SQL query that will insert our variable in the database
            String sql = "INSERT INTO test (test) VALUES (?)";

            // we prepare the query that will be executed
            PreparedStatement req = conn.prepareStatement(sql) ;

            // we bind our method parameter to the query
            req.setString(1,requestToSaveInDB);

            // we execute the query
            req.executeUpdate();

            // we close the statement
            req.close();

            // we close the connection to the database
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * This method get all the requests history from teh database
     *
     */
    static ArrayList<String> list() {

        // we create a list to store all rows from the database
        ArrayList<String> list = new ArrayList<>();

        try {
            // we load the driver that will allow us to connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");

            // we get a connection from the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // we define the SQL query that will fetch all data from the database
            String sql = "SELECT * FROM test";

            // we prepare the query that will be executed
            PreparedStatement req = conn.prepareStatement(sql) ;

            // we get the response from our query to the database in a set of result
            ResultSet rs = req.executeQuery();

            // we iterate over the set of result
            while (rs.next()) {

                // we each result from the database to a String
                String s = rs.getString(1);

                // we add this String to the list
                list.add(s);
            }

            // we close the statement
            req.close();

            // we close the connection to the database
            conn.close();


            // we return the list (will requested by the server)
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
