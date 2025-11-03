package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.command.Command;
import met.freehij.loader.util.InjectionHelper;

public class YawCommand extends Command {
    public YawCommand() {
        super("yaw", "<value>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        try {
            InjectionHelper.getMinecraft().getField("thePlayer").setField("rotationYaw", Float.parseFloat(args[0]));
        } catch (NumberFormatException ignored) {
            return false;
        }
        return true;
    }
}
