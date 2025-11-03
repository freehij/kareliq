package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class UnNoteCommand extends Command {
    public UnNoteCommand() {
        super("unnote", "");
    }

    @Override
    public boolean execute(String[] args) {
        ClientMain.note = "";
        return true;
    }
}
