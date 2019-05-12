package pds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pds.dbAccess.DBAccess;
import pds.dbAccess.ShopTrafficDbAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientProcessing implements Runnable {

	/* Variable declaration*/
	private Socket client ; // The client we are treating
	private PrintWriter out ; // to send message to the client 
	private BufferedReader in ; // to receive message from the client 
	private String response = "";	// The response that will be send to the client
	private Gson gson;	// The response that will be send to the client

	public ClientProcessing() { }

	/* Getter & Setter */

	private void setOut(PrintWriter out) {
		this.out = out;
	}

	private void setIn(BufferedReader in) {
		this.in = in;
	}

	/* Constructor */
	public ClientProcessing(Socket client) {
		try {
			this.client = client;
			setOut(new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true)) ;
			setIn(new BufferedReader(new InputStreamReader(client.getInputStream()))) ;
            gson = new GsonBuilder().create(); // creation of the json converter
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/* Methods */
	
	/**
	 * This method describe "how to treat a client request" 
	 * While the client is connected 
	 * 1 : We get his request
	 * 2 : based on what he want we are able to perform differents actions
	 * 3 : We send back the response according to its request
	 */
	public void run() {
		try {
			while (!client.isClosed()) {
				// If he sent a request
				if (in.ready()) {

					String messageReceived = in.readLine();

					Type type = new TypeToken<Map<String, Object>>(){}.getType();

					Map<String,String> request = gson.fromJson(messageReceived, type);

					// if he disconnected then we close the communication with him
					if (null == request) client.close();
					else {
						// we look at what he send to know what to do
						int id = Integer.parseInt(request.get("id"));
						int monthId = Integer.parseInt(request.get("monthId"));

						ShopTrafficDbAccess.find(id, monthId).forEach(s -> response += s + "\n");

						// We send back the response to the client (convert from String to Json)
						out.println(gson.toJson(response, String.class));

						response = "";

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
