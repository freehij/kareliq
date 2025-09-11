package met.freehij.kareliq;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.command.commands.*;
import met.freehij.kareliq.injection.BlockInjection;
import met.freehij.kareliq.injection.GuiIngameInjection;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.client.TabGui;
import met.freehij.kareliq.module.combat.Aura;
import met.freehij.kareliq.module.combat.NoKnockBack;
import met.freehij.kareliq.module.world.FastBreak;
import met.freehij.kareliq.module.movement.GuiWalk;
import met.freehij.kareliq.module.player.NoFallDamage;
import met.freehij.kareliq.module.movement.Flight;
import met.freehij.kareliq.module.world.WaterWalking;
import met.freehij.kareliq.module.render.Brightness;
import met.freehij.kareliq.module.client.ModuleList;
import met.freehij.kareliq.module.render.OreViewer;
import met.freehij.kareliq.util.BackgroundUtils;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class ClientMain {
    public static boolean loaded = false;
    public static String name = "kareliq";
    public static String version = "1.0dev";
    public static String note = "This is note. You can remove it with ;unnote and set with ;note";
    public static final String configDir = "kareliq/configurations/";
    public static String configName = "default";

    public static Module[] modules;
    public static Command[] commands;

    public static void startClient() {
        modules = new Module[] {
                NoFallDamage.INSTANCE,
                NoKnockBack.INSTANCE,
                WaterWalking.INSTANCE,
                Brightness.INSTANCE,
                FastBreak.INSTANCE,
                ModuleList.INSTANCE,
                OreViewer.INSTANCE,
                GuiWalk.INSTANCE,
                TabGui.INSTANCE,
                Flight.INSTANCE,
                Aura.INSTANCE
        };
        commands = new Command[] {
                new Bind(),
                new Help(),
                new ClientName(),
                new Note(),
                new UnNote(),
                new SettingCommand(),
                new Config(),
        };
        loadJsonConfig();
        loaded = true;
        new Thread(() -> {
            while (loaded) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
                saveJsonConfig();
            }
        }).start();
        BackgroundUtils.generateClass();
    }

    public static void handleKeypress(int key) {
        if(key == Keyboard.KEY_NONE) return;
        for (Module module : ClientMain.modules) {
            if (module.getKeyBind() == key) module.toggle();
        }
        GuiIngameInjection.handleKeyPress(key);
    }

    public static Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    public static boolean handleCommand(String message) {
        String[] split = message.split(" ");
        String command = split[0];
        if (!command.startsWith(";")) return false;
        command = command.substring(1);
        for (Command cmd : commands) {
            if (cmd.getName().equals(command)) {
                String[] args = new String[split.length - 1];
                System.arraycopy(split, 1, args, 0, split.length - 1);
                if (!cmd.execute(args)) { addChatMessage("Usage: " + cmd.getUsage()); return true; }
                return true;
            }
        }
        addChatMessage("Unknown command: " + command);
        return true;
    }

    public static void saveBackgroundPath() {
        try {
            Files.write(Paths.get(configDir + "backgroundPath.txt"), BackgroundUtils.fileName.getBytes());
        } catch (Exception ignored) {}
    }

    public static void saveJsonConfig() {
        JsonObject config = new JsonObject();
        config.addProperty("name", name);
        config.addProperty("note", note);
        JsonObject modules1 = new JsonObject();
        for (Module module : modules) {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("toggled", module.isToggled());
            moduleJson.addProperty("keyBind", Keyboard.getKeyName(module.getKeyBind()));

            JsonObject settings = new JsonObject();
            for (met.freehij.kareliq.module.Setting setting : module.getSettings()) {
                if (setting.getValue() instanceof Boolean) {
                    settings.addProperty(setting.getName(), (Boolean) setting.getValue());
                } else if (setting.getValue() instanceof Integer) {
                    settings.addProperty(setting.getName(), (Integer) setting.getValue());
                } else if (setting.getValue() instanceof Double) {
                    settings.addProperty(setting.getName(), (Double) setting.getValue());
                }
            }
            moduleJson.add("settings", settings);

            modules1.add(module.getName(), moduleJson);
        }
        config.add("modules", modules1);
        try {
            Path configPath = Paths.get(configDir + configName.toLowerCase() + ".json");
            Files.createDirectories(configPath.getParent());
            Files.write(
                    configPath,
                    config.toString().getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            System.out.println("Could not save config!");
            e.printStackTrace();
        }
    }

    public static void loadBackgroundPath() {
        try {
            BackgroundUtils.fileName = Files.readAllLines(Paths.get(configDir + "backgroundPath.txt")).get(0);
            if (!BackgroundUtils.loadTexture()) {
                BackgroundUtils.fileName = "/gui/background.png";
            }
        } catch (Exception ignored) {}
    }

    public static void loadJsonConfig() {
        try {
            JsonObject config = JsonParser.parseString(new String(Files.readAllBytes(
                    Paths.get(configDir + configName.toLowerCase() + ".json")))).getAsJsonObject();
            name = config.get("name").getAsString();
            note = config.get("note").getAsString();
            JsonObject modules = config.getAsJsonObject("modules");
            Iterator<String> keys = modules.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                Module module = getModuleByName(key);
                if (module == null) continue;
                JsonObject moduleJson = modules.getAsJsonObject(key);
                if (moduleJson.get("toggled").getAsBoolean() && !module.isToggled()) {
                    module.toggle();
                }
                module.setKeyBind(Keyboard.getKeyIndex(moduleJson.get("keyBind").getAsString()));
                for (met.freehij.kareliq.module.Setting setting : module.getSettings()) {
                    JsonElement value = moduleJson.getAsJsonObject("settings").get(setting.getName());
                    if (value == null) continue;
                    if (setting.getValue() instanceof Boolean) {
                        setting.setValue(value.getAsBoolean());
                    } else if (setting.getValue() instanceof Integer) {
                        setting.setValue(value.getAsInt());
                    } else if (setting.getValue() instanceof Double) {
                        setting.setValue(value.getAsDouble());
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("Could not load config! Saving current configuration...");
            saveJsonConfig();
        }
    }

    public static void addChatMessage(String message) {
        InjectionHelper.getMinecraft().getField("ingameGUI").invoke("addChatMessage", message);
    }
}
