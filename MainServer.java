import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 5
 * <p>
 * Main server for running
 *
 * @author Shih Kuan Chou, CS 180 - L09
 * @version May 1, 2022
 */

public class MainServer {

    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serverSocket = null;
        System.out.println("Server Listening......"); //SHOWS THAT SERVER IS WAITING FOR CONNECTION
        try {
            serverSocket = new ServerSocket(4242);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                socket = serverSocket.accept(); //SERVER SOCKET IS WAITING FOR A CONNECTION
                System.out.println("Connection Established"); //SHOWS THAT SOCKET IS CONNECTED TO SERVER
                ServerThread st = new ServerThread(socket);
                st.start();

            } catch (Exception e) { //CATCHES AND DISPLAYS ANY ERROR
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }
    }
}
