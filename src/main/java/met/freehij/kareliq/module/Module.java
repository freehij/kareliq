package met.freehij.kareliq.module;

public abstract class Module {
    public static Module INSTANCE;
    private final String name;
    private boolean toggled = false;
    private int keyBind;
    private final Category category;

    protected Module(String name, int keyBind, Category category) {
        this.name = name;
        this.keyBind = keyBind;
        this.category = category;
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

    public enum Category {
        CLIENT(0xffffff),
        COMBAT(0xfc0317),
        MISC(0xb603fc),
        MOVEMENT(0xfcf803),
        PLAYER(0xa5fc03),
        RENDER(0x03bafc),
        WORLD(0xfc00d2);

        private final int color;

        Category(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
}
