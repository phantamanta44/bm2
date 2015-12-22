package io.github.phantamanta44.bm2.core.event.world;

import io.github.phantamanta44.bm2.core.event.chat.CommandSwallower;
import io.github.phantamanta44.bm2.core.module.ModuleManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;




public class WorldChangeListener {

	@SubscribeEvent
	public void onWorldJoin(EntityJoinWorldEvent event) {
		new CommandSwallower("/ip", "Server Address: (\\d+\\.)?\\w*\\.\\w{2,4}").promise(response -> {
			ModuleManager.ipUpdate(response[0].substring(16));
		});
	}
	
}
