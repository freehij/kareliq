package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "<module name> <key name>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            return false;
        }
        Module module = ClientMain.getModuleByName(args[0]);
        if (module != null) {
            int key = Keyboard.getKeyIndex(args[1].toUpperCase());
            if (key != 0) {
                module.setKeyBind(key);
                ClientMain.addChatMessage(module.getName() + " is now bound to " + args[1].toUpperCase());
                return true;
            }
            ClientMain.addChatMessage("Unknown key: " + args[1]);
            return true;
        }
        ClientMain.addChatMessage("Unknown module: " + args[0]);
        return true;
    }
}
