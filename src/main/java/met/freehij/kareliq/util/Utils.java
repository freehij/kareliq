package met.freehij.kareliq.util;

import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.ClassMappings;

public class Utils {
    public static int elementPosition(String[] array, String element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(element)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean containsValue(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static Reflector createScaledResolution(Reflector mc) throws Exception {
        String signature = "L" + ClassMappings.get("GameSettings") + ";II";
        return InjectionHelper.getClazz("ScaledResolution").newInstance(
                signature,
                mc.getField("gameSettings").get(),
                mc.getField("displayWidth").get(),
                mc.getField("displayHeight").get()
        );
    }
}
