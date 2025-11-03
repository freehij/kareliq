package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingButton;
import met.freehij.kareliq.module.SettingSlider;
import org.lwjgl.input.Keyboard;

public class FastFall extends Module {
    public static FastFall INSTANCE = new FastFall();

    protected FastFall() {
        super("FastFall", Keyboard.KEY_NONE, Category.MOVEMENT, new Setting[] {
                new SettingButton("Jump", true, new Setting[0]),
                new SettingSlider("DownMotion", 5.d, 1.d, 10.d, 0.5d)
        });
    }
}
