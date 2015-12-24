package io.github.phantamanta44.bm2.etc.event.chat;

import io.github.phantamanta44.bm2.core.event.IListener;
import io.github.phantamanta44.bm2.core.lang.Lang;
import io.github.phantamanta44.bm2.core.notification.NotificationManager;
import io.github.phantamanta44.bm2.core.notification.TextNotification;
import io.github.phantamanta44.bm2.etc.EtcLang;
import io.github.phantamanta44.bm2.etc.friend.FriendManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatHandler implements IListener {

	private static final Pattern friendAdd = Pattern.compile("([A-Za-z0-9_]*) has been added to your friend list!");
	private static final Pattern friendRm = Pattern.compile("([A-Za-z0-9_]*) has been removed from your friend list");
	private static final Pattern friendLogin = Pattern.compile(". ([A-Za-z0-9_]*) has logged onto Brawl");
	private static final Pattern friendJoin = Pattern.compile(". ([A-Za-z0-9_]*) has logged into (.*)");
	
	@ListenTo
	public void onChat(ClientChatReceivedEvent event) {
		String msg = event.message.getUnformattedText();
		
		if (friendAdd.matcher(msg).matches() || friendRm.matcher(msg).matches())
			FriendManager.updateFriendList();
		
		Matcher loginMatcher = friendLogin.matcher(msg);
		if (loginMatcher.matches()) {
			NotificationManager.putNotification(new TextNotification(
				String.format(Lang.get(EtcLang.FRIEND_LOGIN), loginMatcher.group(1))
			));
		}
		
		Matcher joinMatcher = friendJoin.matcher(msg);
		if (joinMatcher.matches()) {
			NotificationManager.putNotification(new TextNotification(
				String.format(Lang.get(EtcLang.FRIEND_JOIN), joinMatcher.group(1), joinMatcher.group(2))
			));
		}
	}
	
}
