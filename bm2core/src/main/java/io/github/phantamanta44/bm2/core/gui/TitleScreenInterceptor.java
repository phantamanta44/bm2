package io.github.phantamanta44.bm2.core.gui;

import io.github.phantamanta44.bm2.core.BM2Const;
import io.github.phantamanta44.bm2.core.lang.Lang;
import io.github.phantamanta44.bm2.core.module.ModuleManager;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TitleScreenInterceptor {
	
	@SubscribeEvent
	public void drawingGuiScreen(DrawScreenEvent.Post event) {
		if (event.gui instanceof GuiMainMenu) {
			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
			String textLine1 = String.format(Lang.get(Lang.MM_VERSION), BM2Const.MOD_VERS.toString());
			String textLine2 = String.format(Lang.get(Lang.MM_MODCOUNT), ModuleManager.getModuleCount());
			float hue = (System.currentTimeMillis() % 1750) / 1750F;
			int colour = Color.HSBtoRGB(hue, 0.8F, 1F);
			event.gui.drawString(fr, textLine1, 2, 2, colour);
			event.gui.drawString(fr, textLine2, 2, 4 + fr.FONT_HEIGHT, -1);
		}
	}

}
