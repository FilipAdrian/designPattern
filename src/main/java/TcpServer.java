import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class TcpServer extends Thread {
    static Logger logger = Logger.getLogger(TcpServer.class.getName());
    private Authentication authManager = new Authentication();
    public boolean isStarted;
    public Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    protected String ID;
    protected String playerRole;

    TcpServer(Socket socket) {
        this.clientSocket = socket;
        TcpMultiServer.numberOfPlayers++;
        logger.info("New Client connected to server");
        this.ID = authManager.getId();
        this.playerRole = authManager.getRole();
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            GameManagement gameManagement = new GameManagement(out, authManager, this);
            String inputLine;

            while (TcpMultiServer.numberOfPlayers < 3) {
                //NOTHING
                //out.println("WAITING FOR PLAYERS.......");
            }

            out.println(" \t <--- Game Started !  --->");
            out.println("Role: " + playerRole + ", Id: " + ID);
            logger.info("Player " + ID + ": " + "started conversation");
            isStarted = true;

            while ((inputLine = in.readLine()) != null) {
                if (isStarted) {

                    if (GameManagement.isDay && GameManagement.isChatPhase) {
                        String serverMessage = "Player " + ID + ": " + inputLine;
                        broadcast(serverMessage, this);
                    } else if (GameManagement.isDay) {
                        out.println("VOTE NOW");
                        GameManagement.votes.add(Integer.parseInt(inputLine));
                        logger.info(Integer.parseInt(inputLine));
                        if (GameManagement.votes.size() == TcpMultiServer.numberOfPlayers){
                            gameManagement.VoteProcessing();
                        }
                    } else {
                        if (Pattern.compile(Pattern.quote("MAFIA: "), Pattern.CASE_INSENSITIVE).matcher(inputLine).find()) {
                            gameManagement.killPlayer(inputLine);
                        } else if (Pattern.compile(Pattern.quote("COP: "), Pattern.CASE_INSENSITIVE).matcher(inputLine).find()) {
                            ArrayList<String> words = new ArrayList<String>(Arrays.asList(inputLine.split("\\s+")));
                            boolean isMafia = gameManagement.checkIfItsMafia(Integer.parseInt(words.get(1)));
                            out.println(isMafia);
                        }
                    }
                }
                if (Pattern.compile(Pattern.quote("stop"), Pattern.CASE_INSENSITIVE).matcher(inputLine).find()) {
                    out.println("Number of users : " + TcpMultiServer.numberOfPlayers);
                    out.println("Connection was closed");
                    break;
                }
            }

            in.close();
            out.close();
            clientSocket.close();
            TcpMultiServer.numberOfPlayers--;
            logger.info("Session is Finished for: " + "Player " + ID);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void broadcast(String message, TcpServer excludeClient) {
        for (TcpServer client : TcpMultiServer.connectionList) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }

    }

    public void sendMessage(String message) {
        out.println(message);
    }
}