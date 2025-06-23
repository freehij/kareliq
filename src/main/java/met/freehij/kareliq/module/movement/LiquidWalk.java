package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class LiquidWalk extends Module {
    public static final LiquidWalk INSTANCE = new LiquidWalk();

    public LiquidWalk() {
        super("LiquidWalk", Keyboard.KEY_J);
    }
}
