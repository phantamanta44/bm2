package io.github.phantamanta44.bm2.war;

import io.github.phantamanta44.bm2.core.BM2;
import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.util.PropertyMap;
import io.github.phantamanta44.bm2.war.event.chat.ChatListener;

public class WarModule extends BM2Module {

	public static final String MOD_ID = "core.war";
	
	public WarModule(PropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		BM2.info("Lol the war module loaded ok");
		EventManager.registerHandler(MOD_ID, new ChatListener());
	}

}
