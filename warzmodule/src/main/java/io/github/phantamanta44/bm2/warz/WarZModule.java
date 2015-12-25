package io.github.phantamanta44.bm2.warz;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.BrawlServer;
import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.util.IPropertyMap;
import io.github.phantamanta44.bm2.warz.event.ChatHandler;
import io.github.phantamanta44.bm2.warz.event.TPTimer;
import io.github.phantamanta44.bm2.warz.gui.HudCompass;
import io.github.phantamanta44.bm2.warz.gui.HudInfo;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;

public class WarZModule extends BM2Module {

	public static final String MOD_ID = "core.warz";
	
	public WarZModule(IPropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		BM2.info("The template module was loaded!");
		Minecraft mc = Minecraft.getMinecraft();
		EventManager.registerHandler(MOD_ID, new HudCompass(mc));
		EventManager.registerHandler(MOD_ID, new HudInfo(mc));
		EventManager.registerHandler(MOD_ID, new ChatHandler());
	}
	
	@Override
	public void onEnable() {
		TPTimer.start();
	}
	
	@Override
	public void onDisable() {
		TPTimer.stop();
	}
	
	@Override
	public Predicate<String> getEnablementConditions() {
		return ip -> ip.matches(BrawlServer.WARZ.getIpRegex());
	}

}
