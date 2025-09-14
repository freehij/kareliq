package met.freehij.loader.util.mappings;

import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.util.MethodMapping;

import java.util.HashMap;
import java.util.Map;

public class MethodMappings {
    private static final Map<String, MethodMapping> methodRefmap = new HashMap<>();

    public static void initRefmap() {
        add(ClassMappings.get("MovementInputFromOptions"), "checkKeyForMovementInput", "(IZ)V",
                "m_7792523", "checkKeyForMovementInput", "a");
        add(ClassMappings.get("GuiIngame"), "renderGameOverlay", "(FZII)V",
                "m_1283885", "renderGameOverlay", "a");
        add(ClassMappings.get("FontRenderer"), "drawStringWithShadow", "(Ljava/lang/String;III)V",
                "m_1950885", "drawStringWithShadow", "a");
        add(ClassMappings.get("MovementInputFromOptions"), "updatePlayerMoveState",
                "(L" + ClassMappings.get("EntityPlayer") + ";)V",
                "m_6793679", "updatePlayerMoveState", "a");
        add(ClassMappings.get("ScaledResolution"), "getScaledWidth", "()I",
                "getScaledWidth", "a");
        add(ClassMappings.get("FontRenderer"), "getStringWidth", "(Ljava/lang/String;)I",
                "getStringWidth", "a");
        add(ClassMappings.get("Gui"), "drawRect", "(IIIII)V",
                "drawRect", "a");
        add(ClassMappings.get("Block"), "getBlockBrightness", "(L" + ClassMappings.get("IBlockAccess") + ";III)F",
                "getBlockBrightness", "d");
        add(ClassMappings.get("BlockFluid"), "getBlockBrightness", "(L" + ClassMappings.get("IBlockAccess") + ";III)F",
                "getBlockBrightness", "d");
        add(ClassMappings.MINECRAFT, "lineIsCommand", "(Ljava/lang/String;)Z",
                "lineIsCommand", "b");
        add(ClassMappings.get("GuiIngame"), "addChatMessage", "(Ljava/lang/String;)V",
                "addChatMessage", "a");
        add(ClassMappings.get("RenderGlobal"), "loadRenderers", "()V",
                "loadRenderers", "a");
        add(ClassMappings.get("World"), "getLightBrightness", "(III)F",
                "getLightBrightness", "c");
        add(ClassMappings.get("EntityLiving"), "jump", "()V",
                "jump", "R");
        add(ClassMappings.get("Packet10Flying"), "writePacketData", "(Ljava/io/DataOutputStream;)V",
                "writePacketData", "a");
        add(ClassMappings.MINECRAFT, "runTick", "()V",
                "runTick", "k");
        add(ClassMappings.MINECRAFT, "displayGuiScreen", "(L" + ClassMappings.get("GuiScreen") + ";)V",
                "displayGuiScreen", "a");
        add(ClassMappings.MINECRAFT, "isMultiplayerWorld", "()Z",
                "isMultiplayerWorld", "l");
        add(ClassMappings.get("EntityPlayerSP"), "onLivingUpdate", "()V",
                "onLivingUpdate", "o");
        add(ClassMappings.get("World"), "getEntitiesWithinAABBExcludingEntity",
                "(L" + ClassMappings.get("Entity") + ";L" + ClassMappings.get("AxisAlignedBB") + ";)Ljava/util/List;",
                "getEntitiesWithinAABBExcludingEntity", "b");
        add(ClassMappings.get("AxisAlignedBB"), "expand", "(DDD)L" + ClassMappings.get("AxisAlignedBB") + ";",
                "expand", "b");
        add(ClassMappings.get("PlayerController"), "attackEntity",
                "(L" + ClassMappings.get("EntityPlayer") + ";L" + ClassMappings.get("Entity") + ";)V",
                "attackEntity", "b");
        add(ClassMappings.get("EntityPlayer"), "swingItem", "()V",
                "swingItem", "J");
        add(ClassMappings.get("NetClientHandler"), "func_6498_a",
                "(L" + ClassMappings.get("Packet28EntityVelocity") + ";)V",
                "func_6498_a", "a");
        add(ClassMappings.get("Block"), "shouldSideBeRendered", "(L" + ClassMappings.get("IBlockAccess") + ";IIII)Z",
                "shouldSideBeRendered", "b");
        add(ClassMappings.get("World"), "getBlockId", "(III)I",
                "getBlockId", "a");
        add(ClassMappings.get("BlockFluid"), "getCollisionBoundingBoxFromPool",
                "(L" + ClassMappings.get("World") + ";III)L" + ClassMappings.get("AxisAlignedBB") + ";",
                "getCollisionBoundingBoxFromPool", "d");
        add(ClassMappings.get("AxisAlignedBB"), "getBoundingBoxFromPool",
                "(DDDDDD)L" + ClassMappings.get("AxisAlignedBB") + ";",
                "getBoundingBoxFromPool", "b");
        add(ClassMappings.get("Entity"), "isInWater", "()Z",
                "isInWater", "ag");
        add(ClassMappings.get("EntityPlayer"), "jump", "()V",
                "jump", "R");
        add(ClassMappings.get("PlayerControllerMP"), "clickBlock", "(IIII)V",
                "clickBlock", "a");
        add(ClassMappings.get("GuiTextField"), "textboxKeyTyped", "(CI)V",
                "textboxKeyTyped", "a");
        add(ClassMappings.get("GuiScreen"), "getClipboardString", "()Ljava/lang/String;",
                "getClipboardString", "d");
        add(ClassMappings.get("GuiScreen"), "initGui", "()V",
                "initGui", "b");
        add(ClassMappings.get("GuiMainMenu"), "initGui", "()V",
                "initGui", "b");
        add(ClassMappings.get("GuiScreen"), "actionPerformed", "(L" + ClassMappings.get("GuiButton") +";)V",
                "actionPerformed", "a");
        add(ClassMappings.get("GuiMainMenu"), "actionPerformed", "(L" + ClassMappings.get("GuiButton") +";)V",
                "actionPerformed", "a");
        add(ClassMappings.get("GuiScreen"), "onGuiClosed", "()V",
                "onGuiClosed", "h");
        add(ClassMappings.get("GuiScreen"), "drawScreen", "(IIF)V",
                "drawScreen", "a");
        add(ClassMappings.get("GuiScreen"), "drawDefaultBackground", "()V",
                "drawDefaultBackground", "i");
        add(ClassMappings.get("GuiTextField"), "drawTextBox", "()V",
                "drawTextBox", "c");
        add(ClassMappings.get("GuiButton"), "drawButton", "(L" + ClassMappings.MINECRAFT + ";II)V",
                "drawButton", "a");
        add(ClassMappings.get("GuiScreen"), "mouseClicked", "(III)V",
                "mouseClicked", "a");
        add(ClassMappings.get("GuiScreen"), "keyTyped", "(CI)V",
                "keyTyped", "a");
        add(ClassMappings.get("GuiTextField"), "textboxKeyTyped", "(CI)V",
                "textboxKeyTyped", "a");
        add(ClassMappings.get("GuiTextField"), "mouseClicked", "(III)V",
                "mouseClicked", "a");
        add(ClassMappings.get("GuiTextField"), "setMaxStringLength", "(I)V",
                "setMaxStringLength", "a");
        add(ClassMappings.get("GuiTextField"), "setText", "(Ljava/lang/String;)V",
                "setText", "a");
        add(ClassMappings.get("GuiTextField"), "getText", "()Ljava/lang/String;",
                "getText", "a");
        add(ClassMappings.get("GuiScreen"), "drawBackground", "(I)V",
                "drawBackground", "b");
        add(ClassMappings.MINECRAFT, "startGame", "()V",
                "startGame", "a");
        add(ClassMappings.get("GuiScreen"), "updateScreen", "()V",
                "updateScreen", "a");
        add(ClassMappings.get("GuiTextField"), "updateCursorCounter", "()V",
                "updateCursorCounter", "b");
        add(ClassMappings.get("Gui"), "drawTexturedModalRect", "(IIIIII)V",
                "drawTexturedModalRect", "b");
    }

    private static void add(String classReference, String methodReference, String descriptor, String... entries) {
        methodRefmap.put(classReference + ":" + methodReference, new MethodMapping(MappingResolver.resolveMethod(
                classReference, descriptor, entries), null, descriptor));
    }

    public static MethodMapping get(String classReference, String methodReference) {
        return methodRefmap.get(classReference + ":" + methodReference);
    }
}
