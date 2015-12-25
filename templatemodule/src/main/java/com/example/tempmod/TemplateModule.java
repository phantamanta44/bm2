package com.example.tempmod;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.BrawlServer;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.notification.NotificationManager;
import io.github.phantamanta44.bm2.core.notification.TextNotification;
import io.github.phantamanta44.bm2.core.util.PropertyMap;

import java.util.function.Predicate;

public class TemplateModule extends BM2Module {

	public static final String MOD_ID = "tempmod";
	
	public TemplateModule(PropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		BM2.info("The template module was loaded!");
	}
	
	@Override
	public void onEnable() {
		NotificationManager.putNotification(new TextNotification("You are on the lobby!"));
	}
	
	@Override
	public void onDisable() {
		NotificationManager.putNotification(new TextNotification("You are no longer on the lobby!"));
	}
	
	@Override
	public Predicate<String> getEnablementConditions() {
		return ip -> ip.matches(BrawlServer.LOBBY.getIpRegex());
	}

}
