package pds.dbAccess;

import pds.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class DBAccess {

     static ConnectionPool connectionPool;

    // This method create the pool with a fixed size


    // This method close all the connection in the pool
    static void closePool() {
        connectionPool.closeAll();
    }

    /**
     *
     * This method insert the request send by any client in the database.
     *
     */
    public static void create(String requestToSaveInDB) {
        try {

            // we get a connection from the database
            Connection conn = connectionPool.getConnection();

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

            sleep(3000);

            // we close the connection to the database
            connectionPool.releaseConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * This method get all the requests history from teh database
     *
     */
    public static ArrayList<String> list() {

        // we create a list to store all rows from the database
        ArrayList<String> list = new ArrayList<>();

        try {
            // we load the driver that will allow us to connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");

            // we get a connection from the database
            Connection conn = connectionPool.getConnection();

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

            //sleep(3000);

            // we close the connection to the database
            connectionPool.releaseConnection(conn);


            // we return the list (will requested by the server)
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method simulate a number of connections to the database using the connection pool
     * We hold the connections for five seconds and then release them all
     * @param n the number of connections that will be simulated
     */
    static void simulateConnection(int n) {
        try {
            // We create the arrayList that will hold the connections
            ArrayList<Connection> heldConnection = new ArrayList<>(n);

            // We get the connections from the pool
            for (int i = 0; i < n; i++) heldConnection.add(connectionPool.getConnection());

            // We hold them for five seconds
            sleep(5000);

            // Then we release them to the pool
            for (Connection conn : heldConnection) connectionPool.releaseConnection(conn);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
