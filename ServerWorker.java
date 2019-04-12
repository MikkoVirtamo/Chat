import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;


public class ServerWorker extends Thread {

    private List<Socket> sockets;
    private int port;
    private String host;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader bufferedIn;
    private BufferedReader consoleReader;



    public ServerWorker(List<Socket> sockets, int port, String host) {

        this.host = host;
        this.sockets = sockets;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleClient() throws IOException, InterruptedIOException {

        try {

            ServerSocket serverSocket = new ServerSocket(port);
             // server socket
            while (true) {
                sockets.add(serverSocket.accept());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

