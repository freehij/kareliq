package met.freehij.kareliq.module;

public class SettingModes extends Setting {
    private final String[] modes;

    public SettingModes(String name, String[] modes) {
        super(name, 0);
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }

    public String getCurrentMode() {
        return this.modes[this.getInt()];
    }

    @Override
    public void switchValue() {
        if ((int) this.getValue() > modes.length - 2) {
            this.setValue(0);
        } else {
            this.setValue((int) this.getValue() + 1);
        }
    }
}
