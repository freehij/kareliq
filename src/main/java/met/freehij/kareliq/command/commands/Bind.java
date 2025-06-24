package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.ReflectionHelper;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
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
                ReflectionHelper.addChatMessage(module.getName() + " is now bound to " + args[1].toUpperCase());
                return true;
            }
            ReflectionHelper.addChatMessage("Unknown key: " + args[1]);
            return true;
        }
        ReflectionHelper.addChatMessage("Unknown module: " + args[0]);
        return true;
    }
}
