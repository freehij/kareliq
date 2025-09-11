package met.freehij.kareliq.injection;

import met.freehij.kareliq.util.BackgroundUtils;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.opengl.GL11;

@Injection("GuiScreen")
public class GuiScreenInjection {
	@Inject(method = "drawBackground")
	public static void drawBackground(InjectionHelper helper) {
		if (!BackgroundUtils.fileName.equals("/gui/background.png")) {
			int height = helper.getSelf().getField("height").getInt();
			int width = helper.getSelf().getField("width").getInt();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, BackgroundUtils.textureId);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, height);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(width, height);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(width, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glEnd();

			helper.setCancelled(true);
		}
	}
}
