import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;


public class TcpMultiServer {
    static Logger logger = Logger.getLogger (TcpMultiServer.class.getName ( ));
    public static Integer numberOfPlayers = 0;
    private ServerSocket serverSocket;
    TcpMultiServer(){
    }
    public void start(int port) {
        logger.info ("TCP Multi-Server Started");
        try {
            serverSocket = new ServerSocket (port);
            while (true) {
                new TcpServer (serverSocket.accept ( )).start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    private static class TcpServer extends Thread {
        static Logger logger = Logger.getLogger (TcpServer.class.getName ( ));
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public TcpServer(Socket socket) {
            this.clientSocket = socket;
            numberOfPlayers ++;
            logger.info ("New Client connected to server");
        }

        public void run() {
            try {
                out = new PrintWriter (clientSocket.getOutputStream ( ), true);
                in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ( )));
                String inputLine;
                out.println ("Welcome To  TCP Server");
                while ((inputLine = in.readLine ( )) != null) {
                    if (Pattern.compile (Pattern.quote ("start"), Pattern.CASE_INSENSITIVE).matcher (inputLine).find ( )) {
                        inputLine = inputLine.toLowerCase ( );
                        out.println ("You are on server");
                    } else if (Pattern.compile (Pattern.quote ("stop"), Pattern.CASE_INSENSITIVE).matcher (inputLine).find ( )){
                        out.println ("Number of users : " + numberOfPlayers);
                        out.println ("Connection was closed");
                        break;
                    }
                }
                in.close ( );
                out.close ( );
                clientSocket.close ( );
                numberOfPlayers --;
                logger.info ("Session is Finished");
            } catch (IOException e) {
                e.printStackTrace ( );
            }


        }

    }
}
