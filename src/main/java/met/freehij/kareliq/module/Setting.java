package met.freehij.kareliq.module;

public abstract class Setting {
    private final String name;
    private Object value;

    public Setting(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean getBoolean() {
        return (boolean) value;
    }

    public int getInt() {
        return (int) value;
    }

    public double getDouble() {
        return (double) value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void switchValue() {}
}
