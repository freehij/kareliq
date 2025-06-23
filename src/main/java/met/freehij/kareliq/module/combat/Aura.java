package met.freehij.kareliq.module.combat;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class Aura extends Module {
    public static final Aura INSTANCE = new Aura();

    protected Aura() {
        super("Aura", Keyboard.KEY_R);
    }
}
