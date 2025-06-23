package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {
    public static final Flight INSTANCE = new Flight();

    protected Flight() {
        super("Flight", Keyboard.KEY_Z);
    }
}
