import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


public class TcpMultiServer extends Thread {

    public static volatile Integer numberOfPlayers = 0;
    protected static ServerSocket serverSocket;
    static List<TcpServer> connectionList = new ArrayList<>();
    private static Logger logger = Logger.getLogger(TcpMultiServer.class.getName());
    private static TcpMultiServer singleTcpServerInstance = null;

    private TcpMultiServer() {
    }


    public static TcpMultiServer getInstance() {
        if (singleTcpServerInstance == null) {
            singleTcpServerInstance = new TcpMultiServer();
        }
        return singleTcpServerInstance;
    }

    public void start(int port) {
        logger.info("TCP Multi-Server Started");
        new GameManagement().start();
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
