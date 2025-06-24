package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.utils.ReflectionHelper;

public class Help extends Command {
    public Help() {
        super("help", "");
    }

    @Override
    public boolean execute(String[] args) {
        ReflectionHelper.addChatMessage("Command list:");
        for (Command command : ClientMain.commands) {
            ReflectionHelper.addChatMessage(command.getUsage());
        }
        return true;
    }
}
