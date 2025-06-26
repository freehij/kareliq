package met.freehij.kareliq.module.world;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class LiquidWalk extends Module {
    public static final LiquidWalk INSTANCE = new LiquidWalk();

    protected LiquidWalk() {
        super("LiquidWalk", Keyboard.KEY_NONE, Category.WORLD);
    }
}
