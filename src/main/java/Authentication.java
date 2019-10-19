
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Authentication {

    private static List<String> roleList = new ArrayList<String>() {
        {
            add("Mafia");
            add("Citizen");
            add("Citizen");
            add("Cop");
        }
    };

    static int player_id;

    String getRole() {
        if (roleList.size() != 0) {
            Random r = new Random();
            int index = r.nextInt(roleList.size());
            String role = roleList.get(index);
            roleList.remove(index);
            return role;
        } else return "NO_ROLES_AVAILABLE_GO_PLAY_SOMETHING_ELSE_YOU_DUMB_PIECE_OF_SHIT";
    }

    String getId() {
        player_id++;
        return  Integer.toString(player_id - 1);
    }
}
