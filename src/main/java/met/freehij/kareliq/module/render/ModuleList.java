package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    protected ModuleList() {
        super("ModuleList", Keyboard.KEY_Y);
    }
}
