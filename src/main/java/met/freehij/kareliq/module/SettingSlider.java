package met.freehij.kareliq.module;

public class SettingSlider extends Setting {
    private final double min;
    private final double max;
    private final double step;

    public SettingSlider(String name, double value, double min, double max, double step) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }
}
