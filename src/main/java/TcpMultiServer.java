import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


public class TcpMultiServer {

    static Logger logger = Logger.getLogger(TcpMultiServer.class.getName());
    public static Integer numberOfPlayers = 0;
    private ServerSocket serverSocket;

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


}
