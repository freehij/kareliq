package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

public class NoteCommand extends Command {
    public NoteCommand() {
        super("note", "<note(;unnote to remove)>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        ClientMain.note = this.argsToString(args).replace("&", "§");
        return true;
    }
}
