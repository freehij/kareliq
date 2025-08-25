package met.freehij.kareliq.module.world;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public static final FastBreak INSTANCE = new FastBreak();

    protected FastBreak() {
        super("FastBreak", Keyboard.KEY_NONE, Category.WORLD, new Setting[0]);
    }
}
