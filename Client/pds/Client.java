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
