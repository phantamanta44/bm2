package io.github.phantamanta44.bm2.warz.gui;

import io.github.phantamanta44.bm2.core.event.IListener;
import io.github.phantamanta44.bm2.core.event.chat.CommandSwallower;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

public class HudInfo implements IListener {
	
	private Minecraft mc;
	
	public static String currentZone;
	
	public HudInfo(Minecraft minecraft) {
		this.mc = minecraft;
	}
	
	@ListenTo
	public void preRender(RenderGameOverlayEvent.Pre event) {
		if (event.isCancelable() && event.type == ElementType.EXPERIENCE)
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
		String formattedTime = secToString(this.mc.thePlayer.experienceLevel);
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		fr.drawString(formattedTime, 2.4F, 2.4F, 0xff1d1d1d, false);
		fr.drawString(formattedTime, 2, 2, 0xffffffff);
		GlStateManager.scale(0.25F, 0.25F, 0.25F);
		fr.drawStringWithShadow("To Next Restock", 92, 8, 0xffffffff);
		GlStateManager.popMatrix();
		
		List<String> warn = new ArrayList<String>();
		
		if (this.mc.thePlayer.experience <= 0.26)
			warn.add("Thirst Low!");
		
		if (this.mc.thePlayer.getFoodStats().getFoodLevel() <= 9)
			warn.add("Hunger Low!");
		
		PotionEffect pe;
		
		if ((pe = this.mc.thePlayer.getActivePotionEffect(Potion.hunger)) != null)
			warn.add("Infected! §7[" + secToString((int)((float)pe.getDuration() / 20)) + "]");
		
		if ((pe = this.mc.thePlayer.getActivePotionEffect(Potion.blindness)) != null) {
			warn.add("Blinded! §7[" + secToString((int)((float)pe.getDuration() / 20)) + "]");
		}
		
		if (this.mc.thePlayer.isBurning()) {
			String warning = "Ignited!";
			if (this.mc.thePlayer.getEntityData().hasKey("Fire"))
				warning += " §7[" + secToString((int)((float)this.mc.thePlayer.getEntityData().getShort("Fire") / 20)) + "]";
			warn.add(warning);
		}
		
		this.renderWarns(warn, xBounds, fr);
		
		fr.drawStringWithShadow("Zone " + getZone(), 4, yBounds - fr.FONT_HEIGHT - 4, 0xffececec);
	}
	
	private void renderWarns(List<String> w, int xBounds, FontRenderer fr) {
		int y = 4;
		GlStateManager.pushMatrix();
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		GL11.glTranslatef(xBounds * 0.323F, 1.5F, 0.0F);
		for (String s : w) {
			fr.drawStringWithShadow(s, xBounds - (float)fr.getStringWidth(s), (float)y, 0xffff2323);
			y += 12;
		}
		GlStateManager.popMatrix();
	}
	
	public String secToString(int seconds) {
		int sec = seconds % 60;
		int min = (seconds - sec) / 60;
		return String.format("%d:%02d", min, sec);
	}
	
	public static String getZone() {
		if (currentZone == null) {
			new CommandSwallower("/zone", "(Now entering|You are in) zone: \\d").promise(r -> {
				Matcher m = Pattern.compile("(Now entering|You are in) zone: (\\d)").matcher(r[0]);
				m.matches();
				currentZone = m.group(2);
			});
			return "Unknown";
		}
		return currentZone;
	}

}
