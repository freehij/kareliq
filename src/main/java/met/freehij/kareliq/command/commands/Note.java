package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class Note extends Command {
    public Note() {
        super("note", "<note(;unnote to remove)>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        ClientMain.note = this.argsToString(args);
        return true;
    }
}
