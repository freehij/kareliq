package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class Help extends Command {
    public Help() {
        super("help", "");
    }

    @Override
    public boolean execute(String[] args) {
        ClientMain.addChatMessage("Command list:");
        for (Command command : ClientMain.commands) {
            ClientMain.addChatMessage(command.getUsage());
        }
        return true;
    }
}
