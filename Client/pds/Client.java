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
import java.util.HashMap;
import java.util.Map;

public class Client implements Runnable{

    // describe the tunnel to communicate with the server
    private Socket socket;

    // The object that will make possible to get the request from the client
    private BufferedReader in;

    // The object that will make possible to send the response to the client
    private PrintWriter out;

    // The object to convert to Json and vice versa
    private Gson gson ;

    // The template
    private Vue vue;

    /**
     *
     * This constructor create a client and then connect it to the server
     * given and ip address and a port (of the server)
     *
     */
    Client(String ServerName, int ServerPort) {
        try {
            this.socket = new Socket(InetAddress.getByName(ServerName), ServerPort); // we connect to the server given an ip address and port
            this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()),true); // we create the link to send request to the server
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); // we create the link to read the response from the server

            GsonBuilder builder = new GsonBuilder();
            gson = builder.create(); // creation of the json converter

            vue = new Vue();

            vue.getBtnShowTraffic().addActionListener(e -> {

                Map<String,String> map = new HashMap();
                map.put("id", vue.getEnteredId());
                map.put("monthId", String.valueOf(vue.getSelectedMonth()));

                out.println(gson.toJson(map));

                try {

                    if (in.ready()) {
                        String messageReceived = in.readLine();

                        if (messageReceived != null) {
                            vue.clearText();
                            vue.setText(gson.fromJson(messageReceived, String.class));
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {



    }
}
