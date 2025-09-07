package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.module.*;
import met.freehij.kareliq.util.Utils;
import org.lwjgl.input.Keyboard;
import met.freehij.kareliq.module.Module;

public class SettingCommand extends Command {
    public SettingCommand() {
        super("setting", "<list|set> <module name> <setting name> <value>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        switch (args[0].toLowerCase()) {
            case "list":
                if (args.length < 2) return false;
                Module module = ClientMain.getModuleByName(args[1].toLowerCase());
                if (module == null) {
                    ClientMain.addChatMessage("No such module: " + args[1].toLowerCase());
                    return true;
                }
                ClientMain.addChatMessage("Setting list for " + module.getName() + ":");
                for (met.freehij.kareliq.module.Setting setting : module.getSettings()) {
                    Object value = setting.getValue();
                    if (setting instanceof SettingModes) {
                        value = ((SettingModes) setting).getCurrentMode() + " (" + arrayToString(((SettingModes) setting).getModes()) + ")";
                    }
                    ClientMain.addChatMessage(setting.getName() + ": " + value);
                }
                ClientMain.addChatMessage("Bind: " + Keyboard.getKeyName(module.getKeyBind()));
                break;
            case "set":
                if (args.length < 3) return false;
                Module module1 = ClientMain.getModuleByName(args[1].toLowerCase());
                if (module1 == null) {
                    ClientMain.addChatMessage("No such module: " + args[1].toLowerCase());
                    return true;
                }
                for (Setting setting : module1.getSettings()) {
                    if (setting.getName().equalsIgnoreCase(args[2])) {
                        if (setting instanceof SettingButton) {
                            setting.setValue(Boolean.parseBoolean(args[3]));
                            return true;
                        } else if (setting instanceof SettingSlider) {
                            try {
                                setting.setValue(Double.parseDouble(args[3]));
                                return true;
                            } catch (NumberFormatException ignored) {
                                return false;
                            }
                        } else if (setting instanceof SettingModes) {
                            int pos = Utils.elementPosition(((SettingModes) setting).getModes(), args[3]);
                            if (pos != -1) {
                                setting.setValue(pos);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
                ClientMain.addChatMessage("No such setting: " + args[2].toLowerCase());
                break;
        }
        return true;
    }

    private static String arrayToString(String[] array) {
        String finalString = "";
        for (String string : array) {
            finalString += string + "|";
        }
        return finalString.substring(0, finalString.length() - 1);
    }
}
