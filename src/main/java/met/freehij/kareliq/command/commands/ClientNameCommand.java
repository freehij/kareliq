package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class ClientNameCommand extends Command {
    public ClientNameCommand() {
        super("clientname", "<client name(use & to color)>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        ClientMain.name = this.argsToString(args).replace("&", "§");
        return true;
    }
}
