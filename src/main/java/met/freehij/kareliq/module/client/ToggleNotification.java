package met.freehij.kareliq.module.client;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingButton;
import met.freehij.kareliq.module.SettingSlider;
import org.lwjgl.input.Keyboard;

public class ToggleNotification extends Module {
    public static ToggleNotification INSTANCE = new ToggleNotification();

    protected ToggleNotification() {
        super("ToggleNotification", Keyboard.KEY_NONE, Category.CLIENT, new Setting[] {
                new SettingButton("Chat", false, new Setting[0]),
                new SettingButton("Notification", true, new Setting[0]),
                //TODO: subsettings
                new SettingSlider("DecayTime", 3.d, 1.d, 10.d, 0.5d)
        });
    }
}
