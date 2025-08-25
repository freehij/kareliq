package met.freehij.kareliq.command;

import met.freehij.loader.util.InjectionHelper;

public abstract class Command {
    private final String name;
    private final String usage;

    public Command(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public abstract boolean execute(String[] args);

    public String getName() {
        return name;
    }

    public String getUsage() {
        return ";" + this.getName() + " " + this.usage;
    }

    public String argsToString(String[] args) {
        String result = "";
        for (String arg : args) {
            result += arg + " ";
        }
        return result.substring(0, result.length() - 1);
    }
}
