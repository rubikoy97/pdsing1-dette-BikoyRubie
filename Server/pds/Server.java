package pds;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socketserver; // le server
    private BufferedReader in ; // pour recevoir le message du client
    private PrintWriter out ; // pour envoyer le message au client
    private int ServerPort = 9999; // port utilisé

    private Socket client; // Socket du client

    private String messageReceived = "";

    private String response = "";

    public Server() throws IOException {
        socketserver = new ServerSocket(ServerPort);
    }


    public void AcceptConnection(){
        try {

            client = this.socketserver.accept();


            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true) ;

            in = new BufferedReader(new InputStreamReader(client.getInputStream())) ;


                while (!client.isClosed()) {

                    messageReceived = in.readLine();

                    if(messageReceived.equals(null)) client.close();

                    switch (messageReceived) {
                        // le server appelle la base de données avec un SELECT
                        case "netflix":

                            response = "vous avez envoyé NETFLIX";
                            break;

                    }
                    // On retourne la réponse au client
                    out.println(response);
                    response = "";
                }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("<!> end of stream <!>");
        }
    }
