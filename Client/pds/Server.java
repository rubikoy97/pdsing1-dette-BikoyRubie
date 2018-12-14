package pds;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socketserver; // le server
    private BufferedReader in ; // pour recevoir le message du clien
    private PrintWriter out ; // pour envoyer le message au client
    private int ServerPort = 9999; // port utilisé

    private Socket client; // Socket du client

    private String messageReceived = "";

    private String response = "";
