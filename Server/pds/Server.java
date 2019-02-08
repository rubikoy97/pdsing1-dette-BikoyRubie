package pds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

public class Server {

    // The server itself
    private ServerSocket socketserver;

    // The response that will be send to the client
    private String response = "";

    // The object to convert to Json and vice versa
    private Gson gson ;

    /**
     *
     * This constructor create the server on the port 9999 (listen port) with a connection pool of size = maxConnection
     *
     */
    private Server(int maxConnection) {
        try {

            int serverPort = 9999; // port used
            socketserver = new ServerSocket(serverPort); // making the server listen on port 9999
            GsonBuilder builder = new GsonBuilder();
            gson = builder.setPrettyPrinting().create(); // creation of the json converter

            DBAccess.initPool(maxConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  This method is used to accept and client and describe the communication protocol
     *  that is used between the client and the server.
     *
     *  Here, every request send by the client will be stored in the database.
     *  And when the server receive 'list' as a request, he query he database to get all
     *  the previous request made by any client (history), and then send it back to the
     *  client connected.
     */
    private void AcceptConnection(){
        try {

            // we accept the client that want to connect to the server
            Socket client = this.socketserver.accept();


            // we create the object that will make possible to send the response to the client
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

            // we create the object that will make possible to get the request from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // while the client is not disconnected
                while (!client.isClosed()) {

                    // If he sent a request
                    if (in.ready()) {

                        // we get his request (convert from Json to String)
                        String request = gson.fromJson(in.readLine(), String.class);

                        // if he disconnected then we close the communication with him
                        if(null == request) client.close();
                        else {
                            // we look at what he send to know what to do
                            switch (request) {

                                // if he send 'list' we query the database to get the history of all requests made to the server
                                case "list":

                                    // we store the database response in a list
                                    ArrayList<String> resultFromDb = DBAccess.list();

                                    // we make sure that this list is not null
                                    assert resultFromDb != null;

                                    // we iterate over the list to update our response
                                    //for(String s : resultFromDb) response += s + " - ";
                                    Type listType = new TypeToken<List<String>>() {}.getType();
                                    response = gson.toJson(resultFromDb, listType);

                                    break;

                                // By default we save every request made by any client in the database
                                default: DBAccess.create(request);

                            }
                            // We send back the response to the client (convert from String to Json)
                            out.println(response);

                            response = "";

                        }

                    }

                }
        } catch (IOException e) {
            System.err.println("<!> end of stream <!>");
        }
    }

    /**
     * This method describe how the server will run.
     * By default, he only wait from a client to connect, and then accept to communicate with him
     */
    void run() {
        while(!socketserver.isClosed()) AcceptConnection();
        DBAccess.closePool();
    }

    /**
     * Main method that create a server and launch it
     */
    public static void main(String[] args) {
        // Int that represent the max connection to initialize the pool
        int maxConnection ;
        try {
            // Get the number of maxConnection as argument passed to the program
            maxConnection = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
            // If no argument have been specified we initialize the maxConnection to 0 and the pool will be created with its default value (5 connections)
            maxConnection = 0;
        }

        Server server = new Server(maxConnection);
        server.run();
    }

}
