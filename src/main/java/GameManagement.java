import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class GameManagement {
    static Logger logger = Logger.getLogger(GameManagement.class.getName());
    private PrintWriter out;
    private Authentication authManager;
    private TcpServer thread;
    GameManagement(PrintWriter out, Authentication authManager,TcpServer thread) {
        this.out = out;
        this.authManager = authManager;
        this.thread = thread;
    }

    public boolean checkPlayersNumber() {
        boolean isStarted = false;
        if (TcpMultiServer.numberOfPlayers == 3) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(" \t <--- Game is ready to start ...  --->");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info(" \t <--- Game Started !  --->");
            isStarted = true;
            logger.info("Role: " + thread.playerRole + ", Id: " + thread.ID);
        }
        else {
            logger.info("Current Number Of Players: " + TcpMultiServer.numberOfPlayers);
        }
        return isStarted;
    }
 public boolean checkIfItsMafia(Integer playerId){
        String role = TcpMultiServer.connectionList.get(playerId).playerRole;
        if (Pattern.compile(Pattern.quote("mafia"), Pattern.CASE_INSENSITIVE).matcher(role).find()){
            return true;
        }
        return false;
 }
    public void killPlayer(String inputLine){
        out.println("You have typed Mafia");
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(inputLine.split("\\s+")));
        out.println("CHOSEN ID=" + words.get(1));

        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            if (TcpMultiServer.connectionList.get(i).ID.equals(words.get(1))) {
                TcpMultiServer.connectionList.get(i).isStarted = false;
                TcpServer.broadcast("Player: " + TcpMultiServer.connectionList.get(i).ID + ", died" , thread);

            }
        }
    }
}
