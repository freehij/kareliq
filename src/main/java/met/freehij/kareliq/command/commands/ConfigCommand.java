package met.freehij.kareliq.command.commands;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.command.Command;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config", "<load|new> <name>");
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length > 1) {
            switch (args[0].toLowerCase()) {
                case "load":
                    Path configPath = Paths.get(ClientMain.configDir + ClientMain.configName.toLowerCase() + ".json");
                    if (Files.exists(configPath)) {
                        ClientMain.configName = args[1];
                        ClientMain.loadJsonConfig();
                        ClientMain.addChatMessage("Loaded config " + ClientMain.configName);
                    } else {
                        ClientMain.addChatMessage("Config with this name does not exist");
                    }
                    return true;
                case "new":
                    ClientMain.configName = args[1];
                    ClientMain.addChatMessage("Created new config " + ClientMain.configName);
                    return true;
            }
        }
        return false;
    }
}
