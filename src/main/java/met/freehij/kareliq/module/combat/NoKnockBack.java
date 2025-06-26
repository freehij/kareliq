package met.freehij.kareliq.module.combat;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class NoKnockBack extends Module {
    public static final NoKnockBack INSTANCE = new NoKnockBack();

    protected NoKnockBack() {
        super("NoKnockBack", Keyboard.KEY_NONE, Category.COMBAT);
    }
}
