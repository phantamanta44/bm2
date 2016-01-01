package io.github.phantamanta44.bm2.core.notification;

import io.github.phantamanta44.bm2.core.BM2Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Simple textual implementation of {@link io.github.phantamanta44.bm2.core.notification.INotification INotification}.
 * @author Phanta
 */
public class TextNotification implements INotification {

	private static final ResourceLocation NOT_BG = new ResourceLocation(BM2Const.MOD_ID, "textures/gui/textNot.png");
	
	private final String text;
	private final int ttl;
	private final Minecraft mc;
	private final int colour;
	
	/**
	 * Creates a TextNotification with the given string to display.
	 * @param text	Text to display.
	 */
	public TextNotification(String text) {
		this(text, 0xFFFFFF);
	}
	
	/**
	 * Creates a TextNotification with the given string to display.
	 * @param text		Text to display.
	 * @param colour	Text colour.
	 */
	public TextNotification(String text, int colour) {
		this(text, colour, 4200);
	}

	/**
	 * Creates a TextNotification with the given string to display.
	 * @param text		Text to display.
	 * @param colour	Text colour (defaults to white).
	 * @param ttl		Time for this notification to last. Should be at least 1400 ms.
	 */
	public TextNotification(String text, int colour, int ttl) {
		this.text = text;
		this.colour = colour;
		this.ttl = ttl;
		this.mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean tick() {
		return false;
	}

	@Override
	public int getTTL() {
		return this.ttl;
	}

	@Override
	public int getHeight() {
		return this.mc.fontRendererObj.FONT_HEIGHT + 8;
	}

	@Override
	public void render(int y, float fade) {
		FontRenderer fr = this.mc.fontRendererObj;
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, fade);
		this.mc.getTextureManager().bindTexture(NOT_BG);
		wr.startDrawingQuads();
		wr.addVertexWithUV(0D, (double)y + this.getHeight(), -90D, 0D, 1D);
		wr.addVertexWithUV(102D, (double)y + this.getHeight(), -90D, 1D, 1D);
		wr.addVertexWithUV(102D, (double)y, -90D, 1D, 0D);
		wr.addVertexWithUV(0D, (double)y, -90D, 0D, 0D);
		tess.draw();
		fr.drawStringWithShadow(this.text, 4F, (float)(y + 4), colour | ((int)Math.max(1F, fade * 255F) << 24));
		GL11.glPopMatrix();
	}

}
