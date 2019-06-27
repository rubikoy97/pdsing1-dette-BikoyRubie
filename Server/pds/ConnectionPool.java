package pds.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ConnectionPool implements IConnectionPool {

    // Array list that will contains the connections
    private static ArrayList<Connection> pool ;

    private static final String url = "jdbc:postgresql://192.168.20.4:5432/pds";
    private static final String user = "kan10";
    private static final String password = "kan10pwd";

    // By default the pool will make available five connections
    private static final int defaultPoolSize = 5;

    // Represent the maximum of connections that the pool offer
    private int maxConnection;

    // Create a pool with the default size (5 connections)
    public ConnectionPool() {
        this(defaultPoolSize);
    }

    // Create the pool with a specified size represented by maxConnection
    public ConnectionPool(int maxConnection) {
        try {
            System.out.println(maxConnection);
            // We create the pool
            pool = new ArrayList<>(maxConnection);

            // We load the jdbc driver
            Class.forName("org.postgresql.Driver");

            // We fulfil the pool with connections
            for (int i = 0; i < maxConnection; i++) {
                pool.add(DriverManager.getConnection(url,user,password));
            }

            pool.forEach(a -> System.out.println(a.toString()));
            // We set the maxConnection value
            this.maxConnection = maxConnection;

            // We launch the thread that will show the actives connections
           monitoring();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // This method take a connection from the pool
    @Override
    public Connection getConnection() {
        if (pool.size() > 0) return pool.remove(pool.size()-1);
        System.err.println("<!> MAX CONNECTION REACHED <!>");
        return null;
    }

    // This method release a connection to back to the pool
    @Override
    public void releaseConnection(Connection connection) {
        if (pool.size() < maxConnection) pool.add(connection);
    }

    // This method close all the connection from the pool
    @Override
    public void closeAll() {
        for (Connection connection : pool) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // This method return the number of used connection
    private int getActiveConnection() {
        return maxConnection - pool.size();
    }

    // This method return the maximum of connection that the pool can offer
    private int getMaxConnection() {
        return maxConnection;
    }
    // This method show the number of connection in use
    private void monitoring() {

        new Thread(() -> {
            while(true) {
                try {
                    System.out.println(String.format("%s active(s) connection(s) from %s !", getActiveConnection(), getMaxConnection()));
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
