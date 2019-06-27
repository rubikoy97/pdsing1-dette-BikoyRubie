package pds;

public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(new Client("192.168.20.4",9999));
        t.start();
    }
}
