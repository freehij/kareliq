package met.freehij.kareliq.module.misc;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class FastBreak extends Module {
    public static final FastBreak INSTANCE = new FastBreak();

    public FastBreak() {
        super("FastBreak", Keys.KEY_H);
    }
}
