public class Main {
    private static Integer SERVER_PORT = 6666;

    public static void main(String[] args) {
        TcpMultiServer tcpMultiServer = TcpMultiServer.getInstance();
        tcpMultiServer.start(SERVER_PORT);
    }
}
