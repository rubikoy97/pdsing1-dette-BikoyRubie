package pds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedReader kin;
    private PrintWriter out;
    
    Client(String ServerName, int ServerPort) {
        try {
            this.socket = new Socket(InetAddress.getByName(ServerName), ServerPort);
            this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()),true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.kin = new BufferedReader(new InputStreamReader(System.in));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void startCommunicate() {
        try {
            while(!socket.isClosed()) {

                if(kin.ready()) {
                    out.println(kin.readLine());
                } else {
                    if(in.ready()) {
                        String messageReceived = in.readLine();
                        if (messageReceived != null )System.out.println(messageReceived);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Client client = new Client("localhost",9999);
        client.startCommunicate();
    }
}
