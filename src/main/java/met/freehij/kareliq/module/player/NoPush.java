package met.freehij.kareliq.module.player;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class NoPush extends Module {
    public static NoPush INSTANCE = new NoPush();

    protected NoPush() {
        super("NoPush", Keyboard.KEY_NONE, Category.PLAYER, new Setting[0]);
    }
}
