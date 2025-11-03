package met.freehij.kareliq.module.player;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingSlider;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    public static final Step INSTANCE = new Step();

    protected Step() {
        super("Step", Keyboard.KEY_NONE, Category.PLAYER, new Setting[] {
                new SettingSlider("Height", 1.D, 0.D, 2.D, 0.1D)
        });
    }
}
