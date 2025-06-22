package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class NoFallDamage extends Module {
    public static final NoFallDamage INSTANCE = new NoFallDamage();

    protected NoFallDamage() {
        super("NoFallDamage", Keys.KEY_M);
    }
}
