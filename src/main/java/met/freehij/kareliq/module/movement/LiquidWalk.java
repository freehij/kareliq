package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class LiquidWalk extends Module {
    public static final LiquidWalk INSTANCE = new LiquidWalk();

    public LiquidWalk() {
        super("LiquidWalk", Keys.KEY_J);
    }
}
