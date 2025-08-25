package met.freehij.kareliq.module;

public class SettingButton extends Setting {
    private final Setting[] subSettings;

    public SettingButton(String name, boolean toggled, Setting[] subSettings) {
        super(name, toggled);
        this.subSettings = subSettings;
    }

    public Setting[] getSubSettings() {
        return subSettings;
    }

    @Override
    public void switchValue() {
        this.setValue(!this.getBoolean());
    }
}
