package io.github.phantamanta44.bm2.war;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.BrawlServer;
import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.util.IPropertyMap;
import io.github.phantamanta44.bm2.war.event.chat.ChatListener;
import io.github.phantamanta44.bm2.war.gui.HudInterceptor;

import java.util.function.Predicate;

public class WarModule extends BM2Module {

	public static final String MOD_ID = "core_war";
	
	public WarModule(IPropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		BM2.info("Lol the war module loaded ok");
		EventManager.registerHandler(MOD_ID, new ChatListener());
		EventManager.registerHandler(MOD_ID, new HudInterceptor());
	}
	
	@Override
	public Predicate<String> getEnablementConditions() {
		return ip -> ip.matches(BrawlServer.WAR.getIpRegex());
	}

}
