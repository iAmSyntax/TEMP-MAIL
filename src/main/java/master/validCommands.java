package master;

import java.util.HashSet;

public class validCommands {

    public boolean isValid(String cmd) {
        HashSet<String> commands = new HashSet<>();
        commands.add("/start");
        commands.add("/email");


        if (commands.contains(cmd))
            return true;
        else
            return false;
    }
}
