package met.freehij.kareliq.module.combat;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class NoKnockBack extends Module {
    public static final NoKnockBack INSTANCE = new NoKnockBack();

    protected NoKnockBack() {
        super("NoKnockBack", Keys.KEY_N);
    }
}
