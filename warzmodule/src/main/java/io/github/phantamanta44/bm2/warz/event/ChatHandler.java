package io.github.phantamanta44.bm2.warz.event;

import io.github.phantamanta44.bm2.core.event.IListener;
import io.github.phantamanta44.bm2.core.notification.NotificationManager;
import io.github.phantamanta44.bm2.core.notification.TextNotification;
import io.github.phantamanta44.bm2.warz.gui.HudInfo;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import com.mojang.realmsclient.gui.ChatFormatting;

public class ChatHandler implements IListener {
	
	@ListenTo
	public void onChatMessage(ClientChatReceivedEvent event) {
		String msg = event.message.getFormattedText();
		String rawMsg = event.message.getUnformattedText();
		
		if (msg.matches(".*" + ChatFormatting.DARK_GRAY + "(You are in|Now entering) zone.*")) {
			HudInfo.currentZone = rawMsg.split(":")[1].trim();
			if (event.isCancelable())
				event.setCanceled(true);
			NotificationManager.putNotification(new TextNotification("Entering zone " + HudInfo.currentZone));
		}
		
		if (rawMsg.matches("^Request sent to .*"))
			TPTimer.requestSent();
		
		if (msg.matches(".*" + ChatFormatting.ITALIC + ".*Welcome to"))
			HudInfo.getZone();
		
	}
	
}