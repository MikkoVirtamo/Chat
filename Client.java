import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {

    private final String serverName;
    private final int serverPort;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader bufferedIn;
    private BufferedWriter bufferedOut;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }
    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException, InterruptedIOException {

            Socket socket = new Socket(serverName, serverPort);
            // creates new socket with the servername wich is localhost and the port specifie
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (true) try {
                // read the message sent to this client
                String msg = inputStream.readUTF();
                System.out.println("Message from " + socket.getInetAddress() + " '" + msg + "'");
            } catch (Exception e) {
                System.out.println("Connection with " + socket.getInetAddress() + " is terminated");
                break;
            }

    }



}
