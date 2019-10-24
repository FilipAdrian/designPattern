import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class GameManagement {
    static Logger logger = Logger.getLogger(GameManagement.class.getName());
    private PrintWriter out;
    private Authentication authManager;
    private TcpServer thread;
    public volatile static List<Integer> votes = new ArrayList<>();

    public static boolean isDay = true;
    public static boolean isChatPhase = false;

    public void VoteProcessing() {

        logger.info("VOTE PROCESSING STARTED");

        logger.info("MOST VOTED PLAYER IS: " + mostCommon(votes));

        killPlayerForReal(mostCommon(votes));

        votes.clear();
        isChatPhase = false;
        isDay = false;

    }

    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    GameManagement(PrintWriter out, Authentication authManager, TcpServer thread) {
        this.out = out;
        this.authManager = authManager;
        this.thread = thread;
    }

    //DAY_NIGHT CYCLE

    //COROUTINE 30 SEC
    // isDAY=true
    //COROUTINE 30
    //isDAY=FALSE
    //REPEAT

    public boolean checkIfItsMafia(Integer playerId) {
        String role = TcpMultiServer.connectionList.get(playerId).playerRole;
        if (Pattern.compile(Pattern.quote("mafia"), Pattern.CASE_INSENSITIVE).matcher(role).find()) {
            return true;
        }
        return false;
    }

    public void killPlayerForReal(int ID){
        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            if (TcpMultiServer.connectionList.get(i).ID.equals(String.valueOf(ID))) {
                TcpMultiServer.connectionList.get(i).isStarted = false;
                TcpServer.broadcast("Player: " + TcpMultiServer.connectionList.get(i).ID + ", died", thread);
            }
        }
    }

    public void killPlayer(String inputLine) {
        out.println("You have typed Mafia");
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(inputLine.split("\\s+")));
        out.println("CHOSEN ID=" + words.get(1));

        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            if (TcpMultiServer.connectionList.get(i).ID.equals(words.get(1))) {
                TcpMultiServer.connectionList.get(i).isStarted = false;
                TcpServer.broadcast("Player: " + TcpMultiServer.connectionList.get(i).ID + ", died", thread);

            }
        }
    }
}
