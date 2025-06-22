package met.freehij.kareliq.utils.mappings;

public class MethodMappings {
    public static final MappingUtils.MethodEntry[] GET_BLOCK_BRIGHTNESS = new MappingUtils.MethodEntry[] {
            new MappingUtils.MethodEntry("getBlockBrightness", MappingUtils.MappingType.MCP),
            new MappingUtils.MethodEntry("d", MappingUtils.MappingType.MOJANG)
    };
    public static final MappingUtils.MethodEntry[] RENDER_GAME_OVERLAY = new MappingUtils.MethodEntry[] {
            new MappingUtils.MethodEntry("checkKeyForMovementInput", MappingUtils.MappingType.MCP),
            new MappingUtils.MethodEntry("a", MappingUtils.MappingType.MOJANG)
    };
}
