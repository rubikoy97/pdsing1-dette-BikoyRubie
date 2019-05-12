package pds;

public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(new Client("localhost",9999));
        t.start();
    }
}
