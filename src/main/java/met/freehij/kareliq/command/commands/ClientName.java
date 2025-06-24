package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class ClientName extends Command {
    public ClientName() {
        super("clientname", "<client name(use & to color)>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        ClientMain.name = this.argsToString(args);
        return true;
    }
}
