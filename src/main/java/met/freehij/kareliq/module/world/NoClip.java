package met.freehij.kareliq.module.world;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.input.Keyboard;

public class NoClip extends Module {
    public static NoClip INSTANCE = new NoClip();

    protected NoClip() {
        super("NoClip", Keyboard.KEY_NONE, Category.WORLD, new Setting[0]);
    }
}
