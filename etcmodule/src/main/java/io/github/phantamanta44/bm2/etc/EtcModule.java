package io.github.phantamanta44.bm2.etc;

import io.github.phantamanta44.bm2.core.event.EventManager;
import io.github.phantamanta44.bm2.core.module.BM2Module;
import io.github.phantamanta44.bm2.core.util.IPropertyMap;
import io.github.phantamanta44.bm2.etc.command.NameLookupCommand;
import io.github.phantamanta44.bm2.etc.event.chat.ChatHandler;
import io.github.phantamanta44.bm2.etc.friend.FriendManager;
import io.github.phantamanta44.bm2.etc.gui.HudInterceptor;
import net.minecraftforge.client.ClientCommandHandler;

public class EtcModule extends BM2Module {

	public static final String MOD_ID = "core.etc";
	
	public EtcModule(IPropertyMap data) {
		super(data);
	}
	
	@Override
	public void onLoad() {
		EventManager.registerHandler(MOD_ID, new ChatHandler());
		EventManager.registerHandler(MOD_ID, new HudInterceptor());
		ClientCommandHandler.instance.registerCommand(new NameLookupCommand());
	}
	
	@Override
	public void onUnload() {
		FriendManager.disableUpdates();
	}
	
	@Override
	public void onEnable() {
		FriendManager.enableUpdates();
		FriendManager.updateFriendList();
	}
	
	@Override
	public void onDisable() {
		FriendManager.disableUpdates();
	}

}
