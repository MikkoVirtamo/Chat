import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chat {

    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    public static List<Socket> sockets = new ArrayList<>();
    public static int port;

    public static void main(String[] args) throws IOException {

        System.out.println("Chat Client!!\nType help for instructions\n");
        port = Integer.parseInt(args[0]); // port number from command line
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress();


        Thread serverListen = new Thread(new ServerWorker(sockets, port, host));
        serverListen.start();

        Thread client = new Thread(new Client(host, port));
        client.start();

        handleClient();


    }
    private static void handleClient() throws IOException, InterruptedIOException {

        Scanner in = new Scanner(System.in); // just used to read from console
        String s;
        String[] input;


        do{

        s = in.nextLine();
        input = s.split(" ", 3);

            if("myip".equalsIgnoreCase(input[0])) {
                //prints ip address to console
                System.out.print("Your IP Address: ");
                System.out.println(sockets.get(0).getLocalAddress().toString());

            } else if ("myport".equalsIgnoreCase(input[0])) {
                //shows port that you wrote on command line
                System.out.print("Port Number: ");
                System.out.println(port);
            } else if ("connect".equalsIgnoreCase(input[0])) {
                //connects to another server
                //have to specify ipaddres and port number
                System.out.println("Connecting");
                System.out.println(input[1]);
                System.out.println(Integer.parseInt(input[2]));
                Thread newClient = new Thread (new Client(input[1], Integer.parseInt(input[2])));
                newClient.start();
                System.out.println("Connected");

            } else if ("list".equalsIgnoreCase(input[0])) {
                //shows all currently available connections
                System.out.println("List");
                for(int i = 0; i < sockets.size(); i++)
                {
                    System.out.println((i+1) + ". IP " + sockets.get(i).getLocalAddress().toString() + " Port " + sockets.get(i).getPort());
                }
            } else if ("terminate".equalsIgnoreCase(input[0])) {
                //terminate connection from your own list
                if(Integer.parseInt(input[1]) <= sockets.size()) {
                    System.out.print("Terminating: ");
                    System.out.println(sockets.get(Integer.parseInt(input[1]) - 1).getLocalAddress());
                    sockets.remove(Integer.parseInt(input[1]) - 1);
                }
                else System.out.println("that connection doesn't exist");

            } else if ("send".equalsIgnoreCase(input[0])) {
                //send message to ipaddress and port from specified connection in list
                if(Integer.parseInt(input[1]) <= sockets.size()) {
                    outputStream = new DataOutputStream(sockets.get(Integer.parseInt(input[1]) - 1).getOutputStream());
                    outputStream.writeUTF(input[2]);
                    System.out.println("Sending");
                }
                else System.out.println("that connection doesn't exist");


            }else if ("help".equalsIgnoreCase(input[0])) {
                //lists all possible choices
                System.out.println("help -                             Display information about the available user interface options or command manual. \n");
                System.out.println("myip -                             Display the IP address of this process.\n");
                System.out.println("myport -                           helpDisplay the port on which this process is listening for incoming connections. \n");
                System.out.println("connect <destination> <port no> -  This command establishes a new TCP connection to the specified <destination> at the specified < port no>. \n" +
                        "                                   The <destination> is the IP address of the computer. Any attempt to connect to an invalid IP should be rejected and suitable error message should be displayed.\n" +
                        "                                   Success or failure in connections between two peers should be indicated by both the peers using suitable messages. Self-connections and duplicate connections \n" +
                        "                                   should be flagged with suitable error messages.\n");
                System.out.println("list -                             Display a numbered list of all the connections this process is part of.\n" +
                        "                                   This numbered list will include connections initiated by this process and connections \n" +
                        "                                   initiated by other processes. The output should display the IP address and the listening \n" +
                        "                                   port of all the peers the process is connected to.  \n");
                System.out.println("Terminate <connection id.>  -      This command will terminate the connection listed under the \n" +
                        "                                   specified number when LIST is used to display all connections. E.g., terminate 2. In this example, the \n" +
                        "                                   connection with 192.168.21.21 should end. An error message is displayed if a valid connection does not exist as number 2. \n" +
                        "                                   If a remote machine terminates one of your connections, you should also display a message. \n");
                System.out.println("send <connection id.> <message> -  This will send the message to the host on the connection that is designated by the number 3 when command list is used. \n" +
                        "                                   The message to be sent can be up-to 100 characters long, including blank spaces. " +
                        "                                   On successfully executing the command, the sender should display Message sent to <connection id> on the screen. \n" +
                        "                                   On receiving any message from the peer, the receiver should display the received message along with the sender information.\n");
                System.out.println("exit -                              Close all connections and terminate this process. \n" +
                        "                                   The other peers should also update their connection list by removing the peer that exits. ");


            }

        }while(!input[0].equalsIgnoreCase("exit"));
        //if you type exit, the program exits
        System.exit(0);
    }

}

