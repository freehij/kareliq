package met.freehij.kareliq.module.player;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class NoFallDamage extends Module {
    public static final NoFallDamage INSTANCE = new NoFallDamage();

    protected NoFallDamage() {
        super("NoFallDamage", Keyboard.KEY_NONE, Category.PLAYER);
    }
}
