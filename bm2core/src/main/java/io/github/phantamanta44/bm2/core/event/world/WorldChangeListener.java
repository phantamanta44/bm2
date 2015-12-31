package io.github.phantamanta44.bm2.core.event.world;

import io.github.phantamanta44.bm2.core.BrawlServer;
import io.github.phantamanta44.bm2.core.event.chat.CommandSwallower;
import io.github.phantamanta44.bm2.core.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldChangeListener {

	private boolean checkPending = false;
	
	@SubscribeEvent
	public void onWorldJoin(EntityJoinWorldEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (!checkPending && event.entity.equals(mc.thePlayer)) {
			if (!BrawlServer.isBrawlServer(mc.getCurrentServerData().serverIP))
				ModuleManager.ipUpdate(mc.getCurrentServerData().serverIP);
			else {
				checkPending = true;
				new CommandSwallower("/ip", "^Server Address: (\\d+\\.)?(\\S*\\.)+\\w{2,4}$").promise(response -> {
					checkPending = false;
					ModuleManager.ipUpdate(response[0].substring(16));				
				}).dispatch();
			}
		}
	}
	
}
