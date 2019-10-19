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
    private static String MAGIC_WORD;
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
            logger.info ("New Client connected to server");
        }

        public void run() {
            try {
                out = new PrintWriter (clientSocket.getOutputStream ( ), true);
                in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ( )));
                String inputLine;
                out.println ("Welcome To  TCP Server");
                while ((inputLine = in.readLine ( )) != null) {

                }
                in.close ( );
                out.close ( );
                clientSocket.close ( );
                logger.info ("Session is Finished");
            } catch (IOException e) {
                e.printStackTrace ( );
            }


        }

    }
}
