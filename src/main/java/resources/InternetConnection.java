package resources;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetConnection {

    public static boolean isInternetAvailable() {
        try {
            Socket socket = new Socket();
            int timeout = 5000;
            socket.connect(new InetSocketAddress("www.google.com", 80), timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
