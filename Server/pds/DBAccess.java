package pds;

import pds.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


class DBAccess {

    private static ConnectionPool connectionPool;

    // This method create the pool with a fixed size
    static void initPool(int maxConnection) {
        if (maxConnection > 0) connectionPool = new ConnectionPool(maxConnection);
        else connectionPool = new ConnectionPool();
    }

    // This method close all the connection in the pool
    static void closePool() {
        connectionPool.closeAll();
    }

    /**
     *
     * This method insert the request send by any client in the database.
     *
     */
    static void create(String requestToSaveInDB) {
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
    static ArrayList<String> list() {

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

            Thread.sleep(5000);

            // we close the connection to the database
            connectionPool.releaseConnection(conn);


            // we return the list (will requested by the server)
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
