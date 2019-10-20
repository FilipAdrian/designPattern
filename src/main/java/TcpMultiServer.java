import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class TcpMultiServer {

    static Logger logger = Logger.getLogger(TcpMultiServer.class.getName());
    public static Integer numberOfPlayers = 0;
    private ServerSocket serverSocket;
    private static boolean isDay = false;

    static List<TcpServer> connectionList = new ArrayList<TcpServer>();

    TcpMultiServer() {
    }

    public void start(int port) {
        logger.info("TCP Multi-Server Started");
        try {
            serverSocket = new ServerSocket(port);
            while (true) {

                TcpServer myServer = new TcpServer(serverSocket.accept());
                myServer.start();
                connectionList.add(myServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, TcpServer excludeClient) {
        for (TcpServer client : connectionList) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    private static class TcpServer extends Thread {
        static Logger logger = Logger.getLogger(TcpServer.class.getName());
        Authentication authManager = new Authentication();
        private boolean isStarted = false;
        public Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        String ID;

        TcpServer(Socket socket) {
            this.clientSocket = socket;
            numberOfPlayers++;
            logger.info("New Client connected to server");
            this.ID = authManager.getId();
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (isStarted) {
                        String serverMessage = "Player " + ID + ": " + inputLine;
                        broadcast(serverMessage, this);

                    }
                    if (Pattern.compile(Pattern.quote("start"), Pattern.CASE_INSENSITIVE).matcher(inputLine).find() && !isStarted) {
                        out.println("Game Started !");
                        out.println("Role: " + authManager.getRole() + ", Id: " + ID);
                        logger.info("Player " + ID + ": " + "started conversation");
                        isStarted = true;

                    } else if (Pattern.compile(Pattern.quote("MAFIA: "), Pattern.CASE_INSENSITIVE).matcher(inputLine).find()) {
                        inputLine = inputLine.toLowerCase();
                        out.println("You have typed Mafia");
                        ArrayList<String> words = new ArrayList<String>(Arrays.asList(inputLine.split("\\s+")));
                        out.println("CHOSEN ID=" + words.get(1));

                        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
                            if (TcpMultiServer.connectionList.get(i).ID.equals(words.get(1))) {

                                TcpMultiServer.connectionList.get(i).in.close();
                                TcpMultiServer.connectionList.get(i).out.close();
                                TcpMultiServer.connectionList.get(i).clientSocket.close();

                            }
                        }

                    } else if (Pattern.compile(Pattern.quote("stop"), Pattern.CASE_INSENSITIVE).matcher(inputLine).find()) {
                        out.println("Number of users : " + numberOfPlayers);
                        out.println("Connection was closed");
                        break;
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
                numberOfPlayers--;
                logger.info("Session is Finished for: " + "Player " + ID);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
