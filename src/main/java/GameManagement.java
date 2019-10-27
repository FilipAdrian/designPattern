import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GameManagement extends Thread {
    public static volatile boolean isStarted;
    public volatile static List<Integer> votes = new ArrayList<>();
    public volatile static boolean isDay = true;
    public volatile static boolean isChatPhase = false;
    static Logger logger = Logger.getLogger(GameManagement.class.getName());
    private TcpServer thread;

    public GameManagement() {
    }

    public GameManagement(TcpServer thread) {
        this.thread = thread;
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

    public static void startDayNightCycle() {
        logger.info("Day/Night Cycle Started");
        long startTime = System.currentTimeMillis();
        int timer = 30000; // 30 sec per day
        activateDayAndChat();
        while (true) {
            if (isStarted) {
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                if (totalTime >= timer) {
                    switch (timer) {
                        case 10000: {
                            logger.info("Night Finished");
                            timer = timer * 3;
                            logger.info("Day Started");
                            activateDayAndChat();
                            startTime = System.currentTimeMillis();
                            TcpServer.broadcast("\t <--- DAY --->", null);
                            break;
                        }
                        case 30000: {
                            logger.info("Day Finished");
                            timer += timer / 3;
                            logger.info("Vote Started");
                            isChatPhase = false;
                            TcpServer.broadcast("\t <--- VOTE NOW --->", null);
                            break;
                        }
                        case 40000: {
                            logger.info("Vote Finished");
                            timer = timer / 4;
                            logger.info("Night Started");
                            isDay = false;
                            TcpServer.broadcast("\t <--- NIGHT --->", null);
                            startTime = System.currentTimeMillis();
                            break;
                        }
                    }
                }
                checkGameStatus();
            } else {
                startTime = System.currentTimeMillis();
                timer = 30000;
                activateDayAndChat();
            }
        }
    }

    private static void activateDayAndChat() {
        isDay = true;
        isChatPhase = true;
    }

    private static void checkGameStatus() {

        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            String role = TcpMultiServer.connectionList.get(i).playerRole;
            Boolean isActive = TcpMultiServer.connectionList.get(i).isActive;
            if (TcpMultiServer.numberOfPlayers == 2) {
                if (role.equals("Mafia") && isActive) {
                    TcpServer.broadcast(" \t <--- GAME OVER ---> \n\t <--- MAFIA WIN --->", new TcpServer());
                    logger.info(" \t <--- GAME OVER ---> \n\t <--- MAFIA WIN --->");
                    isStarted = false;
                    disconnectAllPlayer();
                }
            } else if (role.equals("Mafia") && !isActive) {
                TcpServer.broadcast(" \t <--- GAME OVER ---> \n\t <--- CITIZEN WIN --->", new TcpServer());
                logger.info(" \t <--- GAME OVER ---> \n\t <--- CITIZEN WIN --->");
                isStarted = false;
                disconnectAllPlayer();
            }
        }
    }

    private static void disconnectAllPlayer() {
        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            try {
                TcpMultiServer.connectionList.get(i).in.close();
                TcpMultiServer.connectionList.get(i).out.close();
                TcpMultiServer.connectionList.get(i).clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TcpMultiServer.connectionList.clear();
        TcpMultiServer.numberOfPlayers = 0;
    }

    public void VoteProcessing() {

        logger.info("VOTE PROCESSING STARTED");
        logger.info("MOST VOTED PLAYER IS: " + mostCommon(votes));
        killPlayer(mostCommon(votes));
        votes.clear();
    }

    public void run() {
        startDayNightCycle();
    }

    public boolean checkIfItsMafia(Integer playerId) {
        String role = TcpMultiServer.connectionList.get(playerId).playerRole;
        return Pattern.compile(Pattern.quote("mafia"), Pattern.CASE_INSENSITIVE).matcher(role).find();
    }

    public void killPlayer(int ID) {
        for (int i = 0; i < TcpMultiServer.connectionList.size(); i++) {
            if (TcpMultiServer.connectionList.get(i).ID.equals(String.valueOf(ID))) {
                TcpMultiServer.connectionList.get(i).isActive = false;
                TcpMultiServer.numberOfPlayers--;
                TcpServer.broadcast("Player: " + TcpMultiServer.connectionList.get(i).ID + ", died", new TcpServer());
            }
        }
    }
}
