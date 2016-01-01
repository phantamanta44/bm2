package io.github.phantamanta44.bm2.spvp;

import io.github.phantamanta44.bm2.core.BrawlServer;
import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.util.IPropertyMap;
import io.github.phantamanta44.bm2.spvp.gui.HudInfo;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;

public class SPvPModule extends BM2Module {

	public static final String MOD_ID = "core_spvp";
	
	public SPvPModule(IPropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		EventManager.registerHandler(MOD_ID, new HudInfo(Minecraft.getMinecraft()));
	}
	
	@Override
	public Predicate<String> getEnablementConditions() {
		return ip -> ip.matches(BrawlServer.PVP.getIpRegex());
	}

}
