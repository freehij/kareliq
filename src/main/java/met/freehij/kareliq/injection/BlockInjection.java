package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.render.Brightness;
import met.freehij.kareliq.module.render.OreViewer;
import met.freehij.kareliq.util.Utils;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.mappings.ClassMappings;
import met.freehij.loader.util.mappings.FieldMappings;

@Injection("Block")
public class BlockInjection {
    private static final int[] oreViewerBlocks = new int[] { 7, 14, 15, 16, 21, 56, 74, 74 };
    private static String blockID = null;

    @Inject(method = "getBlockBrightness")
    public static void getBlockBrightness(InjectionHelper helper) {
        if (Brightness.INSTANCE.isToggled()) {
            helper.setReturnValue((float) Brightness.INSTANCE.getSettings()[0].getDouble());
            helper.setCancelled(true);
        }
    }

    @Inject(method = "shouldSideBeRendered")
    public static void shouldSideBeRendered(InjectionHelper helper) {
        if (blockID == null) blockID = FieldMappings.get(ClassMappings.get("Block"), "blockID");
        if (OreViewer.INSTANCE.isToggled()) {
            if (Utils.containsValue(oreViewerBlocks, (int) helper.getSelf().getFieldRaw(blockID).get())) {
                helper.setReturnValue(true);
            } else {
                helper.setReturnValue(false);
            }
            helper.setCancelled(true);
        }
    }
}
