package met.freehij.kareliq.module;

public abstract class Module {
    public static Module INSTANCE;
    private final String name;
    private boolean toggled = false;
    private int keyBind;

    protected Module(String name, int keyBind) {
        this.name = name;
        this.keyBind = keyBind;
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
}
