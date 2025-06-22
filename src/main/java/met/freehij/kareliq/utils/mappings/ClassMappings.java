package met.freehij.kareliq.utils.mappings;

public class ClassMappings {
    public static final MappingUtils.ClassEntry[] BLOCK = new MappingUtils.ClassEntry[] {
            new MappingUtils.ClassEntry("net/minecraft/src/Block", MappingUtils.MappingType.MCP),
            new MappingUtils.ClassEntry("un", MappingUtils.MappingType.MOJANG)
    };
    public static final MappingUtils.ClassEntry[] BLOCK_FLUID = new MappingUtils.ClassEntry[] {
            new MappingUtils.ClassEntry("net/minecraft/src/BlockFluid", MappingUtils.MappingType.MCP),
            new MappingUtils.ClassEntry("rk", MappingUtils.MappingType.MOJANG)
    };
    public static final MappingUtils.ClassEntry[] BLOCK_ACCESS = new MappingUtils.ClassEntry[] {
            new MappingUtils.ClassEntry("net/minecraft/src/IBlockAccess", MappingUtils.MappingType.MCP),
            new MappingUtils.ClassEntry("xg", MappingUtils.MappingType.MOJANG)
    };
    public static final MappingUtils.ClassEntry[] MOVEMENT_INPUT_FROM_OPTIONS = new MappingUtils.ClassEntry[] {
            new MappingUtils.ClassEntry("net/minecraft/src/MovementInputFromOptions", MappingUtils.MappingType.MCP),
            new MappingUtils.ClassEntry("ln", MappingUtils.MappingType.MOJANG)
    };
}
