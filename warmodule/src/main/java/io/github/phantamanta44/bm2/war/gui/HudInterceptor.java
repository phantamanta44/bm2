package io.github.phantamanta44.bm2.war.gui;

import io.github.phantamanta44.bm2.core.event.IListener;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import com.google.common.collect.Sets;

public class HudInterceptor implements IListener {

	private static final Set<ElementType> toCancel = Sets.newHashSet(
		
	);
	
	@ListenTo
	public void onRender(RenderGameOverlayEvent.Pre event) {
		if (!event.isCancelable())
			return;
		if (toCancel.contains(event.type)) {
			event.setCanceled(true);
			return;
		}
		if (event.type != ElementType.EXPERIENCE)
			return;
		
		event.setCanceled(true);
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		fr.drawString("YOU ARE ON WAR", 2, 2, 0xAA0000);
	}
	
}
