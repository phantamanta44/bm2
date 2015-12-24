package io.github.phantamanta44.bm2.etc.gui;

import io.github.phantamanta44.bm2.core.event.IListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class HudInterceptor implements IListener {
	
	@ListenTo
	public void onRender(RenderGameOverlayEvent.Pre event) {
		if (event.type != ElementType.EXPERIENCE)
			return;
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	}
	
}
