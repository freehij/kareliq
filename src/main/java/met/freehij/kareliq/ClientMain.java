package met.freehij.kareliq;

import met.freehij.kareliq.command.Command;
import met.freehij.kareliq.command.commands.*;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.combat.Aura;
import met.freehij.kareliq.module.combat.NoKnockBack;
import met.freehij.kareliq.module.misc.ClickGui;
import met.freehij.kareliq.module.misc.FastBreak;
import met.freehij.kareliq.module.movement.GuiWalk;
import met.freehij.kareliq.module.movement.NoFallDamage;
import met.freehij.kareliq.module.movement.Flight;
import met.freehij.kareliq.module.movement.LiquidWalk;
import met.freehij.kareliq.module.render.FullBright;
import met.freehij.kareliq.module.render.ModuleList;
import met.freehij.kareliq.module.render.OreViewer;
import met.freehij.kareliq.utils.ReflectionHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ClientMain {
    public static boolean loaded = false;
    public static String name = "kareliq";
    public static String version = "1.0dev";
    public static String note = "This is note. You can remove it with ;unnote and set with ;note";
    public static String configFile = "kareliq/config.liza";

    public static Module[] modules;
    public static Command[] commands;

    public static void startClient() {
        modules = new Module[] {
                NoFallDamage.INSTANCE,
                NoKnockBack.INSTANCE,
                FastBreak.INSTANCE,
                ModuleList.INSTANCE,
                OreViewer.INSTANCE,
                FullBright.INSTANCE,
                LiquidWalk.INSTANCE,
                ClickGui.INSTANCE,
                GuiWalk.INSTANCE,
                Flight.INSTANCE,
                Aura.INSTANCE
        };
        commands = new Command[] {
                new Bind(),
                new Help(),
                new ClientName(),
                new Note(),
                new UnNote()
        };
        loadConfig();
        loaded = true;
        new Thread(() -> {
            while (loaded) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
                saveConfig();
            }
        }).start();
    }
    
    public static void handleKeypress(int key, boolean pressed) {
    	if (pressed) {
            for (Module module : ClientMain.modules) {
                if (module.getKeyBind() == key) module.toggle();
            }
        }
    }

    /*
    roughly equivalent to:
    this.mc.fontRenderer.drawStringWithShadow();

    int i = 2;
    net.minecraft.src.ScaledResolution scaledResolution = new net.minecraft.src.ScaledResolution (
        this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight
    );
    for (met.freehij.kareliq.module.Module module : met.freehij.kareliq.ClientMain.modules) {
        if (module.isToggled()) {
            this.mc.fontRenderer.drawStringWithShadow(
                module.getName(),
                scaledResolution.getScaledWidth() - this.mc.fontRenderer.getStringWidth(module.getName()),
                i, Integer.MAX_VALUE
            );
            i += 10;
        }
    }
    */
    
    public static void ClickGUI_initGui(Object dis) {
    	//initGui
    	int x = 2;
    	int y = 20;
    	int wid = 80;
    	int hei = 20;
    	ArrayList controls = ReflectionHelper.GuiScreen_controlList(dis);
    	
    	for(int i = 0; i < modules.length; ++i) {
    		Object button = ReflectionHelper.createButton(i, x, y, wid, hei, modules[i].getName());
    		controls.add(button);
    		y += 24;
    	}
    }
    
    public static void ClickGUI_actionPerformed(Object dis, Object butt) {
    	int id = ReflectionHelper.GuiButton_id(butt);
    	modules[id].toggle();
    }
    
    public static void ClickGUI_drawScreen(Object dis, int x, int y, float f) {
    	ReflectionHelper.FontRenderer_drawString("hi ur currently in gui ^-^", 2, 2, 0xff0000);
    	ReflectionHelper.FontRenderer_drawString("we hav the follwoin modules:", 2, 12, 0xff0000);
    }
    
    public static void renderGuiIngame(Object guiIngame, Object scaledResolution) {
    	ReflectionHelper.FontRenderer_drawString("§6" + name + " §f" + version, 2, 2, Integer.MAX_VALUE);
        ReflectionHelper.FontRenderer_drawString(note, 2, 12, Integer.MAX_VALUE);
        if (!ModuleList.INSTANCE.isToggled()) return;
    	int i = 2;
    	for (Module module : ClientMain.modules) {
    		if(module.isToggled()) {
    			String s = module.getName();
    			int swid = ReflectionHelper.FontRenderer_getStringWidth(s);
    			int x = ReflectionHelper.ScaledResolution_getScaledWidth(scaledResolution) - swid;
    			ReflectionHelper.FontRenderer_drawString(s, x, i, Integer.MAX_VALUE); //used same method in bytecode as drawString, but the roughly equivalent to shows drawStringWithShadow?
    			i += 10;
    		}
    	}
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
                if (!cmd.execute(args)) ReflectionHelper.addChatMessage("Usage: " + cmd.getUsage());
                return true;
            }
        }
        ReflectionHelper.addChatMessage("Unknown command: " + command);
        return true;
    }

    public static void saveConfig() { //TODO: better config format
        String finalText = name.replace(":", ";") + ":" + version + ":" + note.replace(":", ";") + "\n";
        for (Module module : modules) {
            finalText += module.getName() + ":" + module.isToggled() + ":" + module.getKeyBind() + "\n";
        }
        try {
            Path configPath = Paths.get(configFile);
            Files.createDirectories(configPath.getParent());
            Files.write(
                    configPath,
                    finalText.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            System.out.println("Could not save config!");
            e.printStackTrace();
        }
    }

    public static void loadConfig() { //TODO: better config format
        try {
            String[] content = new String(Files.readAllBytes(Paths.get(configFile))).split("\n");
            String[] textVariables = content[0].split(":");
            name = textVariables[0];
            note = textVariables[2];
            String[] moduleInfo = new String[content.length - 1];
            System.arraycopy(content, 1, moduleInfo, 0, content.length - 1);
            for (String info : moduleInfo) {
                String[] split = info.split(":");
                Module module = getModuleByName(split[0]);
                if (module == null) continue;
                if (Boolean.parseBoolean(split[1])) module.toggle();
                module.setKeyBind(Integer.parseInt(split[2]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not load config! Saving current configuration...");
            saveConfig();
        }
    }
}
