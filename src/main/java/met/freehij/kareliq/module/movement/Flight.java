package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class Flight extends Module {
    public static final Flight INSTANCE = new Flight();

    protected Flight() {
        super("Flight", Keys.KEY_Z);
    }
}
