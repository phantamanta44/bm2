package io.github.phantamanta44.bm2.warz.event;

import io.github.phantamanta44.bm2.core.event.chat.CommandSwallower;
import io.github.phantamanta44.bm2.warz.gui.HudInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldJoinHandler {
	
	private static final String IP_PATTERN = "^Server Address: ((\\d+\\.)?(\\S*\\.)+\\w{2,4})$";
	
	private Minecraft mc;
	private String currentIp;
	
	public WorldJoinHandler(Minecraft minecraft) {
		mc = minecraft;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			if (((EntityPlayer)event.entity).getUniqueID().equals(this.mc.thePlayer.getUniqueID()))
				HudInfo.getZone();
		}
		new CommandSwallower("/ip", IP_PATTERN).promise(r -> {
			Matcher m = Pattern.compile(IP_PATTERN).matcher(r[0]);
			m.matches();
			String newIp = m.group(1);
			if (currentIp != newIp)
				TPTimer.clear();
			currentIp = newIp;
		});
	}
	
}
