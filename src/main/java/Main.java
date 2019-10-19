public class Main {
    private static Integer SERVER_PORT = 6666;
    public static void main(String[] args) {
        TcpMultiServer tcpMultiServer = new TcpMultiServer();
        tcpMultiServer.start(SERVER_PORT);
    }
}
