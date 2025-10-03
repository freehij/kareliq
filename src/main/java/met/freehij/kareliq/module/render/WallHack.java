package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class WallHack extends Module {
    public static WallHack INSTANCE = new WallHack();

    protected WallHack() {
        super("WallHack", Keyboard.KEY_NONE, Category.RENDER, new Setting[0]);
    }
}
