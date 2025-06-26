package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class OreViewer extends Module {
    public static final OreViewer INSTANCE = new OreViewer();

    protected OreViewer() {
        super("OreViewer", Keyboard.KEY_NONE, Category.RENDER);
    }
}
