package pds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    // describe the tunnel to communicate with the server
    private Socket socket;

    // The object that will make possible to get the request from the client
    private BufferedReader in;

    // The object that will make possible to send the response to the client
    private PrintWriter out;

    // The object that will make possible to read what we type in the terminal
    private BufferedReader kin;

    // The object to convert to Json and vice versa
    private Gson gson ;

    /**
     *
     * This constructor create a client and then connect it to the server
     * given and ip address and a port (of the server)
     *
     */
    private Client(String ServerName, int ServerPort) {
        try {
            this.socket = new Socket(InetAddress.getByName(ServerName), ServerPort); // we connect to the server given an ip address and port
            this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()),true); // we create the link to send request to the server
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); // we create the link to read the response from the server
            this.kin = new BufferedReader(new InputStreamReader(System.in)); // we create the link to get what is typed in the terminal

            GsonBuilder builder = new GsonBuilder();
            gson = builder.setPrettyPrinting().create(); // creation of the json converter
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method describe the communication protocol between the client and the server.
     * By default, it only read what is typed in the terminal, when the client press (enter on the keyboard)
     * everything that have been typed will be sent to the server.
     */
    private void startCommunicate() {
        try {
            // while the link to communicate with the server is not closed
            while(!socket.isClosed()) {

                // if we have something to send to the server (aka pressing enter)
                if(kin.ready()) {
                    // we send what have been typed to the server (convert from String to Json)
                    out.println(gson.toJson(kin.readLine()));

                    // otherwise
                } else {
                    // if the server have sent something
                    if(in.ready()) {
                        // we get what the server have sent (convert from Json to String)
                        String messageReceived = gson.fromJson(in.readLine(),String.class);

                        // and if what he sent is not null then we print it
                        if (messageReceived != null )System.out.println(messageReceived);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to create a client and connect it to the local server on port 9999
     */
    public static void main(String args[]) {
        Client client = new Client("192.168.20.4",9999);
        client.startCommunicate();
    }
}
