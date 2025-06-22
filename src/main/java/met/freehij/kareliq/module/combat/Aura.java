package met.freehij.kareliq.module.combat;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class Aura extends Module {
    public static final Aura INSTANCE = new Aura();

    protected Aura() {
        super("Aura", Keys.KEY_R);
    }
}
