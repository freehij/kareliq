package met.freehij.kareliq.module.client;

import met.freehij.kareliq.module.*;
import org.lwjgl.input.Keyboard;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    protected ModuleList() {
        super("ModuleList", Keyboard.KEY_NONE, Category.CLIENT, new Setting[] {
                new SettingModes("Colors", new String[] { "Rainbow", "Classic" }),
                new SettingModes("Style", new String[] { "Frame", "Flux", "FluxReverse", "None" }),
                new SettingButton("Background", true, new Setting[0])
        });
    }
}
