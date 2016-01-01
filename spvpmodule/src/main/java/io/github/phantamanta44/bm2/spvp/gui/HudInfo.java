package io.github.phantamanta44.bm2.spvp.gui;

import io.github.phantamanta44.bm2.core.event.IListener;
import io.github.phantamanta44.bm2.core.util.Mutable;
import io.github.phantamanta44.bm2.spvp.SPvPModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

@SuppressWarnings("unchecked")
public class HudInfo implements IListener {
	
	private static final ResourceLocation INV_BG = new ResourceLocation("minecraft", "textures/gui/container/inventory.png");
	private static final ResourceLocation SPVP_WIDGETS = new ResourceLocation(SPvPModule.MOD_ID, "textures/gui/widgets.png");
	private static final float DEG_TO_RAD = (float)(Math.PI / 180D);
	
	private Minecraft mc;
	
	public HudInfo(Minecraft minecraft) {
		this.mc = minecraft;
	}
	
	@ListenTo
	public void onPreRender(RenderGameOverlayEvent.Pre event) {
		if (event.type == ElementType.BOSSHEALTH && event.isCancelable())
			event.setCanceled(true);
	}

	@ListenTo
	public void draw(RenderGameOverlayEvent.Post event) {
		if (event.type != ElementType.TEXT)
			return;
		
		ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int xBounds = sr.getScaledWidth();
		int yBounds = sr.getScaledHeight();
		FontRenderer fr = this.mc.fontRendererObj;
		GuiIngame gig = this.mc.ingameGUI;
		EntityPlayerSP pl = this.mc.thePlayer;
		
		BlockPos pos = pl.getPosition();
		float rot = (630F - pl.rotationYaw) % 360F;
		String xFacing = Math.cos(rot * DEG_TO_RAD) > 0 ? "+" : "-";
		String zFacing = Math.sin(rot * DEG_TO_RAD) > 0 ? "-" : "+";
		fr.drawStringWithShadow(String.format("[%s%s, %s, %s%s]", pos.getX(), xFacing, pos.getY(), pos.getZ(), zFacing), 2, 2, 0xffffffff);
		
		Mutable<Integer> potY = new Mutable<>(2);
		final int yOff = 18 / 2 - fr.FONT_HEIGHT / 2;
		this.mc.thePlayer.getActivePotionEffects().forEach(o -> {
			PotionEffect pe = ((PotionEffect)o);
			String time = secToString((int)((float)pe.getDuration() / 20));
			int iconInd = Potion.potionTypes[pe.getPotionID()].getStatusIconIndex();
			fr.drawStringWithShadow(time, xBounds - 26 - fr.getStringWidth(time), potY.getValue() + yOff, 0xFFFFFF);
			this.mc.getTextureManager().bindTexture(INV_BG);
            gig.drawTexturedModalRect(xBounds - 20, potY.getValue(), 0 + iconInd % 8 * 18, 198 + iconInd / 8 * 18, 18, 18);
            potY.setValue(potY.getValue() + 20);
		});
		
		this.mc.getTextureManager().bindTexture(SPVP_WIDGETS);
		this.drawDirectionHud(xBounds, yBounds, this.mc.thePlayer, rot, gig);
	}
	
	private void drawDirectionHud(int width, int height, EntityPlayer pl, float deg, GuiIngame gig) {
		short barWidth = 182;
		
		float relN = (deg + 630F) % 360F;
		int shift = (int)((float)barWidth * (relN / 180F));
		int shiftN = shift >= barWidth ? shift - 2 * barWidth : shift;
		int widthN = Math.abs(shift - barWidth);
		int shiftS = shift - barWidth;
		int widthS = barWidth - Math.abs(shift - barWidth);
		
		GlStateManager.enableBlend();
		int top = 3;
		int left = width / 2 - 91;
		gig.drawTexturedModalRect(Math.max(left, left + shiftN), top, -Math.min(shiftN, 0), 27, widthN, 4);
		gig.drawTexturedModalRect(Math.max(left, left + shiftS), top, -Math.min(shiftS, 0), 36, widthS, 4);
		GlStateManager.disableBlend();
	}
	
	public static String secToString(int seconds) {
		int sec = seconds % 60;
		int min = (seconds - sec) / 60;
		return String.format("%d:%02d", min, sec);
	}

}
