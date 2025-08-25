package met.freehij.kareliq.module;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    public static Module INSTANCE;
    private final String name;
    private boolean toggled = false;
    private int keyBind;
    private final Category category;
    private final Setting[] settings;

    protected Module(String name, int keyBind, Category category, Setting[] settings) {
        this.name = name;
        this.keyBind = keyBind;
        this.category = category;
        this.category.addModule(this);
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !this.toggled;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public Category getCategory() {
        return category;
    }

    public Setting[] getSettings() {
        return settings;
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    public void onSettingChange(Setting setting) {}

    public enum Category {
        CLIENT(0xb603fc, "Client"),
        COMBAT(0xfc0317, "Combat"),
        MOVEMENT(0xfcf803, "Movement"),
        PLAYER(0xa5fc03, "Player"),
        RENDER(0x03bafc, "Render"),
        WORLD(0xfc00d2, "World"),;

        private final int color;
        private final String name;
        private final List<Module> modules = new ArrayList<Module>();

        Category(int color, String name) {
            this.color = color;
            this.name = name;
        }

        public int getColor() {
            return color;
        }

        public String getName() {
            return name;
        }

        public void addModule(Module module) {
            modules.add(module);
        }

        public Module[] getModules() {
            return modules.toArray(new Module[0]);
        }
    }
}
