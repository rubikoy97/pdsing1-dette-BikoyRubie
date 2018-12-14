package pds;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socketserver; // the server
    private BufferedReader in ; // to receive message from the client
    private PrintWriter out ; // to send message to the client
    private int ServerPort = 9999; // port used

    private Socket client; // Socket of the client

    private String messageReceived = "";

    private String response = "";
