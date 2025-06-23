package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class GuiWalk extends Module {
    public static final GuiWalk INSTANCE = new GuiWalk();

    public GuiWalk() {
        super("GuiWalk", Keyboard.KEY_L);
    }
}
