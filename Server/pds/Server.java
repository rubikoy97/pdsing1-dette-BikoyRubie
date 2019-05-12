package pds;

import pds.dbAccess.ShopDbAccess;
import pds.mock.ShopScript;
import pds.mock.ShopTrafficScript;
import pds.model.Shop;
import pds.pool.ConnectionPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

public class Server {

    // The server itself
    private ServerSocket socketserver;
    public static ConnectionPool connectionPool;

    /**
     * This constructor create the server on the port 9999 (listen port) with a connection pool of size = maxConnection
     */
    private Server(int maxConnection) throws IOException, SQLException {
        int serverPort = 9999; // port used
        socketserver = new ServerSocket(serverPort); // making the server listen on port 9999
        if (maxConnection > 0) connectionPool = new ConnectionPool(maxConnection);
        else connectionPool = new ConnectionPool();

       // ShopScript.insertAll();
       // ShopTrafficScript.insertAll();
    }

    /**
     * This method is used to accept and client and describe the communication protocol
     * that is used between the client and the server.
     * <p>
     * Here, every request send by the client will be stored in the database.
     * And when the server receive 'list' as a request, he query he database to get all
     * the previous request made by any client (history), and then send it back to the
     * client connected.
     */
    private void AcceptConnection() {
        try {
            new Thread(new ClientProcessing(this.socketserver.accept())).start();
        } catch (IOException e) {
            System.err.println("<!> end of stream <!>");
        }
    }

    /**
     * This method describe how the server will run.
     * By default, he only wait from a client to connect, and then accept to communicate with him
     */
    private void run() {
        while (!socketserver.isClosed()) AcceptConnection();
        connectionPool.closeAll();
    }

    /**
     * Main method that create a server and launch it
     */
    public static void main(String[] args) throws IOException, SQLException {
        // Int that represent the max connection to initialize the pool
        int maxConnection;

        try {
            // Get the number of maxConnection and number of simulated connection as argument passed to the program
            maxConnection = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
            // If no argument have been specified we initialize the maxConnection to 0 and the pool will be created with its default value (5 connections)
            maxConnection = 0;
        }

        new Server(maxConnection).run();
    }
}
